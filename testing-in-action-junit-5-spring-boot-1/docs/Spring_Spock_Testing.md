## 1. 概述

在这个教程中，我们将演示Spring Boot测试框架和Spock框架的结合使用。

## 2. 项目构建

让我们从一个简单的Web应用程序开始，除了应用程序主类，我们使用一个简单的RestController来提供功能：

```java

@RestController
@RequestMapping("/hello")
public class WebController {

    private String name;

    @GetMapping
    public String salutation() {
        return "Hello " + Optional.ofNullable(name).orElse("world") + '!';
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setName(@RequestBody final String name) {
        this.name = name;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetToDefault() {
        this.name = null;
    }
}
```

## 3. Spock和Spring Boot测试的Maven依赖

### 3.1 使用Spring支持添加Spock框架依赖项

对于Spock本身和Spring支持，我们需要两个依赖项：

```
<dependency>
    <groupId>org.spockframework</groupId>
    <artifactId>spock-core</artifactId>
    <version>1.2-groovy-2.4</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.spockframework</groupId>
    <artifactId>spock-spring</artifactId>
    <version>1.2-groovy-2.4</version>
    <scope>test</scope>
</dependency>
```

请注意，指定的版本是对使用的groovy版本的引用。

### 3.2 添加Spring Boot测试

为了使用Spring Boot的测试工具，我们需要以下依赖：

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <version>2.6.1</version>
    <scope>test</scope>
</dependency>
```

### 3.3 Groovy

由于Spock基于Groovy，我们必须添加和配置gmavenplus-plugin以便能够在我们的测试中使用这种语言：

```
<plugin>
    <groupId>org.codehaus.gmavenplus</groupId>
    <artifactId>gmavenplus-plugin</artifactId>
    <version>1.6</version>
    <executions>
        <execution>
            <goals>
                <goal>compileTests</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

请注意，由于我们只需要Groovy进行测试，因此我们将插件goal限制为compileTest。

## 4. 在Spock测试中加载ApplicationContext

一个简单的测试是检查Spring应用程序上下文中的所有Bean是否都已创建：

```groovy
@Title("Application Specification")
@Narrative("Specification which beans are expected")
@SpringBootTest
class LoadContextTests extends Specification {
    @Autowired(required = false)
    private WebController webController

    def "when context is loaded then all expected beans are created"() {
        expect: "the WebController is created"
        webController
    }
}
```

对于这个集成测试，我们需要启动ApplicationContext，@SpringBootTest提供了这个功能。
Spock在我们的测试中使用“when”、“then”或“expect”等关键字提供了部分分隔。

此外，我们可以利用Groovy Truth来检查bean是否为空，作为我们测试的最后一行。

## 5. 在Spock测试中使用WebMvcTest

同样，我们可以测试WebController的行为：

```groovy
@Title("WebController Specification")
@Narrative("The Specification of the behaviour of the WebController. It can greet a person, change the name and reset it to 'world'")
@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
class WebControllerTest extends Specification {

    @Autowired
    private MockMvc mvc

    def "when get is performed then the response has status 200 and content is 'Hello world!'"() {
        expect: "Status is 200 and the response is 'Hello world!'"
        mvc.perform(MockMvcRequestBuilders.get("/hello")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn().response.contentAsString == "Hello world!"
    }

    def "when set and delete are performed then the response has status 204 and content changes as expected"() {
        given: "a new name"
        def NAME = "Emmy"

        when: "the name is set"
        mvc.perform(MockMvcRequestBuilders.put("/hello").content(NAME)).andExpect(MockMvcResultMatchers.status().isNoContent())

        then: "the salutation uses the new name"
        mvc.perform(MockMvcRequestBuilders.get("/hello")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn().response.contentAsString == "Hello $NAME!"

        when: "the name is deleted"
        mvc.perform(MockMvcRequestBuilders.delete("/hello")).andExpect(MockMvcResultMatchers.status().isNoContent())

        then: "the salutation uses the default name"
        mvc.perform(MockMvcRequestBuilders.get("/hello")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn().response.contentAsString == "Hello world!"
    }
}
```

需要注意的是，在我们的Spock测试(或者更确切地说是Specifications)中，我们可以使用我们习惯的Spring Boot测试框架中所有熟悉的注解。

## 6. 总结

在本文中，我们演示了如何设置Maven项目以结合使用Spock和Spring Boot测试框架。