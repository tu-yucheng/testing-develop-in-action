## 1. 概述

在本教程中，我们将分析使用非抽象方法对抽象类进行单元测试的各种用例和可能的替代解决方案。

请注意，测试抽象类几乎总是应该通过具体实现的公共API，因此除非你确定自己在做什么，否则不要应用以下技术。

## 2. Maven依赖

```
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <version>5.8.1</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>4.0.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.powermock</groupId>
    <artifactId>powermock-module-junit4</artifactId>
    <version>1.7.4</version>
    <scope>test</scope>
    <exclusions>
        <exclusion>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.powermock</groupId>
    <artifactId>powermock-api-mockito2</artifactId>
    <version>1.7.4</version>
    <scope>test</scope>
</dependency>
```

Junit 5不完全支持Powermock。此外，powermock-module-junit4仅用于第5节中介绍的一个示例。

## 3. 独立非抽象方法

让我们考虑一个例子，我们有一个抽象类，该类有一个公共的非抽象方法：

```java
public abstract class AbstractIndependent {

    public abstract int abstractFunc();

    public String defaultImpl() {
        return "DEFAULT-1";
    }
}
```

如果我们想测试defaultImpl()方法，有两种可能的解决方案 - 使用具体的实现类，或者使用Mockito。

### 3.1 使用具体的类

创建一个继承AbstractIndependent类的具体实现类，并使用它用于测试：

```java
public class ConcreteImpl extends AbstractIndependent {

    @Override
    public int abstractFunc() {
        return 4;
    }
}
```

```java
class AbstractIndependentUnitTest {
    @Test
    void givenNonAbstractMethod_whenConcreteImple_testCorrectBehaviour() {
        ConcreteImpl conClass = new ConcreteImpl();
        String actual = conClass.defaultImpl();
        assertEquals("DEFAULT-1", actual);
    }
}
```

这种解决方案的缺点是需要创建包含所有抽象方法虚拟实现的具体类。

### 3.2 使用Mockito

或者，我们可以使用Mockito创建一个mock：

```java
class AbstractIndependentUnitTest {
    @Test
    void givenNonAbstractMethod_whenMockitoMock_testCorrectBehaviour() {
        AbstractIndependent absClass = Mockito.mock(AbstractIndependent.class, Mockito.CALLS_REAL_METHODS);
        assertEquals("DEFAULT-1", absClass.defaultImpl());
    }
}
```

**这里最重要的部分是mock的创建，我们使用Mockito.CALLS_REAL_METHODS指定调用方法时使用真实代码**。

## 4. 从非抽象方法调用抽象方法

在这种情况下，非抽象方法定义了全局执行流程，而抽象方法可以根据场景的不同以不同的方式实现：

```java
public abstract class AbstractMethodCalling {

    public abstract String abstractFunc();

    public String defaultImpl() {
        String res = abstractFunc();
        return (res == null) ? "Default" : (res + " Default");
    }
}
```

为了测试这段代码，我们可以使用与之前相同的两种方法 - 要么创建一个具体的实现类，要么使用Mockito创建一个mock：

```java
class AbstractMethodCallingUnitTest {
    private AbstractMethodCalling abstractMethodCalling;

    @BeforeEach
    public void setup() {
        abstractMethodCalling = Mockito.mock(AbstractMethodCalling.class);
    }

    @Test
    void givenDefaultImpl_whenMockAbstractFunc_thenExpectedBehaviour() {
        Mockito.when(abstractMethodCalling.abstractFunc()).thenReturn("Abstract");
        Mockito.doCallRealMethod().when(abstractMethodCalling).defaultImpl();
        assertEquals("Abstract Default", abstractMethodCalling.defaultImpl());

        Mockito.doReturn(null).when(abstractMethodCalling).abstractFunc();
        assertEquals("Default", abstractMethodCalling.defaultImpl());
    }
}
```

在这里，abstractFunc()使用我们指定的测试返回值进行stub。这意味着当我们调用非抽象方法defaultImpl()时，它将使用这个stub。

## 5 具有测试障碍的非抽象方法

在某些情况下，我们要测试的方法调用包含测试阻塞的私有方法。

在测试目标方法之前，我们需要绕过阻碍测试方法：

```java
public abstract class AbstractPrivateMethods {

    public abstract int abstractFunc();

    public String defaultImpl() {
        return getCurrentDateTime() + "DEFAULT-1";
    }

    private String getCurrentDateTime() {
        return LocalDateTime.now().toString();
    }
}
```

在本例中，defaultImpl()方法调用私有方法getCurrentDateTime()。这个私有方法在运行时获取当前时间，在我们的单元测试中应该避免这种情况。

现在，为了mock这个私有方法的标准行为，我们甚至不能使用Mockito，因为它无法控制私有方法。

因此，我们需要使用PowerMock(**请注意，此示例仅适用于JUnit 4，因为JUnit 5不支持此依赖项**)：

```java

@RunWith(PowerMockRunner.class)
@PrepareForTest(AbstractPrivateMethods.class)
public class AbstractPrivateMethodsUnitTest {
    @Test
    public void givenNonAbstractMethodAndCallPrivateMethod_whenMockPrivateMethod_thenVerifyBehaviour() throws Exception {
        AbstractPrivateMethods mockClass = PowerMockito.mock(AbstractPrivateMethods.class);
        PowerMockito.doCallRealMethod().when(mockClass).defaultImpl();
        String dateTime = LocalDateTime.now().toString();
        PowerMockito.doReturn(dateTime).when(mockClass, "getCurrentDateTime");
        String actual = mockClass.defaultImpl();
        assertEquals(dateTime + "DEFAULT-1", actual);
    }
}
```

本例中的几个重要点：

+ @RunWith将PowerMockRunner定义为测试的runner。
+ @PrepareForTest(AbstractPrivateMethods.class)告诉PowerMock准备类以供以后处理。

有趣的是，我们告诉PowerMock stub私有方法getCurrentDateTime()。PowerMock将使用反射来找到它，因为它无法从外部访问。

因此，当我们调用defaultImpl()时，将调用为私有方法创建的stub而不是实际方法。

## 6. 访问实例变量的非抽象方法

抽象类可以具有使用实例变量实现的内部状态。变量的值可能会对测试的方法产生重大影响。

如果一个变量是公共的或受保护的，我们可以很容易地从测试方法中访问它。

但如果它是私有的，我们必须使用PowerMockito：

```java
public abstract class AbstractInstanceFields {
    protected int count;
    private boolean active = false;

    public abstract int abstractFunc();

    public String testFunc() {
        String response;
        if (count > 5)
            response = "Overflow";
        else
            response = active ? "Added" : "Blocked";
        return response;
    }
}
```

在这里，testFunc()方法根据实例变量count和active返回相应的值。

在测试testFunc()时，我们可以通过访问使用Mockito创建的实例来更改count字段的值。

另一方面，要使用私有字段active测试行为，我们需要再次使用PowerMockito及其Whitebox类：

```java
class AbstractInstanceFieldsUnitTest {
    @Test
    void givenNonAbstractMethodAndPrivateField_whenPowerMockitoAndActiveFieldTrue_thenCorrectBehaviour() {
        AbstractInstanceFields instClass = PowerMockito.mock(AbstractInstanceFields.class);
        PowerMockito.doCallRealMethod().when(instClass).testFunc();

        Whitebox.setInternalState(instClass, "active", true);

        // compare the expected result with actual
        Assertions.assertEquals("Added", instClass.testFunc());
    }
}
```

我们使用PowerMockito.mock()创建一个存根类，并且我们使用Whitebox类来控制对象的内部状态。

active字段的值更改为true。

## 7. 总结

在本教程中，我们涵盖许多用例的多个示例。根据所遵循的设计，我们可以在更多场景中使用抽象类。

此外，为抽象类方法编写单元测试与普通类和方法一样重要。我们可以使用不同的技术或可用的不同测试支持库来测试它们中的每一个。