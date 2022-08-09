## 1. 概述

Spring提供了许多特性来帮助我们测试代码。有时我们需要使用特定的配置属性，以便在我们的测试用例中设置所需的场景。

**在这些情况下，我们可以使用@TestPropertySource注解。
使用此注解，我们可以定义比项目中使用的任何其他配置属性源具有更高优先级的配置源**。

因此，在这个简短的教程中，我们将演示此注解的使用。此外，我们会较少它的默认行为和它支持的主要属性。

要了解有关在Spring Boot中进行测试的更多信息，建议阅读[在Spring Boot中进行测试]()教程。

## 2. maven依赖

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <version>2.6.1</version>
</dependency>
```

## 3. @TestPropertySource的使用

假设我们通过使用@Value注解注入属性来使用它的值：

```java

@Component
public class ClassUsingProperty {

    @Value("${tuyucheng.testpropertysource.one}")
    private String propertyOne;

    public String retrievePropertyOne() {
        return propertyOne;
    }
}
```

然后，我们将使用@TestPropertySource这个类级别注解来定义新的配置源并覆盖该属性的值：

```java

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ClassUsingProperty.class)
@TestPropertySource
public class DefaultTestPropertySourceIntegrationTest {
    @Autowired
    private ClassUsingProperty classUsingProperty;

    @Test
    void givenDefaultTestPropertySource_whenVariableOneRetrieved_thenValueInDefaultFileReturned() {
        String output = classUsingProperty.retrievePropertyOne();
        assertThat(output).isEqualTo("default-value");
    }
}
```

通常，每当我们使用此测试注解时，我们还需要添加@ContextConfiguration注解，以便为测试场景加载和配置ApplicationContext。

**默认情况下，@TestPropertySource注解会尝试加载与声明注解的类相关的属性文件。**

在这种情况下，如果我们的测试类在测试文件夹的cn.tuyucheng.taketoday.testpropertysource包中，
那么我们的类路径中需要cn/tuyucheng/taketoday/testpropertysource/DefaultTest.properties文件。

让我们将它添加到我们的测试目录的resources文件夹中：

```properties
#DefaultTestPropertySourceIntegrationTest.properties
tuyucheng.testpropertysource.one=default-value
```

**此外，我们可以更改默认配置文件的位置，或添加具有更高优先级的额外属性**：

```java

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ClassUsingProperty.class)
@TestPropertySource(locations = "/other-location.properties")
public class LocationTestPropertySourceIntegrationTest {
    @Autowired
    private ClassUsingProperty classUsingProperty;

    @Test
    public void givenDefaultTestPropertySourceWhenVariableOneRetrievedThenValueInDefaultFileReturned() {
        String output = classUsingProperty.retrievePropertyOne();
        assertThat(output).isEqualTo("other-location-value");
    }
}
```

如上@TestPropertySource(locations = "/other-location.properties")，
我们指定了location属性为测试资源目录下的other-location.properties文件。此时属性配置将从此文件加载。

或者，我们可以直接在@TestPropertySource注解中使用properties属性声明属性配置。

```java

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ClassUsingProperty.class)
@TestPropertySource(properties = "tuyucheng.testpropertysource.one=other-properties-value")
public class PropertiesTestPropertySourceIntegrationTest {
    @Autowired
    private ClassUsingProperty classUsingProperty;

    @Test
    public void givenDefaultTestPropertySourceWhenVariableOneRetrievedThenValueInDefaultReturned() {
        String output = classUsingProperty.retrievePropertyOne();
        assertThat(output).isEqualTo("other-properties-value");
    }
}
```

如上，我们直接在@TestPropertySource注解中使用properties属性指定了tuyucheng.testpropertysource.one属性。

此时这里配置的属性的具有加载的最高优先级。

最后，我们可以指定是否要从父类继承locations和properties值。因此，我们可以切换inheritLocations和inheritProperties属性，默认情况下为true。

## 4. 总结

通过这个简单的例子，我们学习了如何有效地使用@TestPropertySource注解。