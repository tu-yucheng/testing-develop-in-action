## 1. 概述

在本教程中，我们将介绍@DirtiesContext注解。

## 2. @DirtiesContext

**@DirtiesContext是一个Spring测试注解**，它表示关联的测试或类修改了ApplicationContext。它告诉测试框架关闭并为以后的测试重新创建上下文。

我们可以使用该注解标注一个测试方法或整个类。**通过设置MethodMode或ClassMode，我们可以控制Spring何时将上下文标记为关闭**。

如果我们将@DirtiesContext放在一个类上，则注解将应用于具有给定ClassMode的类中的每个方法。

## 3. 在不清除Spring上下文的情况下进行测试

假设我们有一个User类：

```java
public class User {
    String firstName;
    String lastName;
}
```

我们还有一个非常简单的UserCache：

```java

@Component
public class UserCache {

    @Getter
    private final Set<String> userList = new HashSet<>();

    public void addUser(String user) {
        userList.add(user);
    }

    public boolean removeUser(String user) {
        return userList.remove(user);
    }

    public void printUserList(String message) {
        System.out.println(message + ": " + userList);
    }
}
```

我们创建一个集成测试来加载和测试整个应用程序：

```java

@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringDataRestApplication.class)
class DirtiesContextIntegrationTest {

    @Autowired
    protected UserCache userCache;
    // ...
}
```

第一个方法addJaneDoeAndPrintCache()向缓存中添加一个数据：

```java
class DirtiesContextIntegrationTest {
    @Test
    @Order(1)
    void addJaneDoeAndPrintCache() {
        userCache.addUser("Jane Doe");
        userCache.printUserList("addJaneDoeAndPrintCache");
    }
}
```

将用户添加到缓存后，它会打印缓存的内容：

```
addJaneDoeAndPrintCache: [Jane Doe]
```

接下来，printCache()再次打印userCache：

```java
class DirtiesContextIntegrationTest {
    @Test
    @Order(2)
    void printCache() {
        userCache.printUserList("printCache");
    }
}
```

它包含在上一个测试方法中添加的数据：

```
printCache: [Jane Doe]
```

假设后面的测试方法依赖于空缓存来进行一些断言，以前插入的数据可能会导致不期望的行为。

## 4. 使用@DirtiesContext注解

现在，我们介绍@DirtiesContext默认的MethodMode.AFTER_METHOD。这意味着在相应的测试方法完成后，Spring将标记上下文为关闭。

为了隔离对测试的更改，我们添加@DirtiesContext注解，让我们看看它是如何工作的。

addJohnDoeAndPrintCache()测试方法将“John Doe”添加到缓存中。我们还添加了@DirtiesContext注解，该注解表示上下文应该在测试方法结束时关闭：

```java
class DirtiesContextIntegrationTest {
    @Test
    @Order(3)
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    void addJohnDoeAndPrintCache() {
        userCache.addUser("John Doe");
        userCache.printUserList("addJohnDoeAndPrintCache");
    }
}
```

现在打印userCache的输出是：

```
addJohnDoeAndPrintCache: [John Doe, Jane Doe]
```

最后，printCacheAgain()再次打印userCache：

```java
class DirtiesContextIntegrationTest {
    @Test
    @Order(4)
    void printCacheAgain() {
        userCache.printUserList("printCacheAgain");
    }
}
```

在运行完整的测试类时，我们看到Spring上下文在addJohnDoeAndPrintCache()和printCacheAgain()方法之间重新加载。因此缓存重新初始化，输出为空：

```
printCacheAgain: []
```

## 5. 其他受支持的测试阶段

上述示例演示了MethodMode.AFTER_METHOD，让我们快速总结一下各个阶段：

### 5.1 类级别

测试类的ClassMode选项定义了重置上下文的时间：

+ BEFORE_CLASS：在当前测试类之前
+ BEFORE_EACH_TEST_METHOD：在当前测试类中的每个测试方法之前
+ AFTER_EACH_TEST_METHOD：在当前测试类中的每个测试方法之后
+ AFTER_CLASS：在当前测试类之后

### 5.2 方法级别

单个方法的MethodMode选项定义了重置上下文的时间：

+ BEFORE_METHOD：在当前测试方法之前
+ AFTER_METHOD：在当前测试方法之后