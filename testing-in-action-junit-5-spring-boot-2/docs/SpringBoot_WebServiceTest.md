## 1. 概述

在本文中，我们将了解如何为使用Spring Boot构建的SOAP Web Service编写集成测试。

在这里，我们专注于使用@WebServiceServerTest仅对Web Service层进行集成测试。

## 2. Spring Web Service测试

在Spring Web Services中，端点是服务器端服务实现的关键概念。专门的@Endpoint注解将带注解的类标记为Web Service端点。
重要的是，**这些端点负责接收XML请求消息，调用所需的业务逻辑，并将结果作为响应消息返回**。

### 2.1 Spring Web Service测试支持

为了测试这样的端点，我们可以通过传入所需的参数或mock来轻松创建单元测试。
然而，主要的缺点是这实际上并没有测试通过网络发送的XML消息的内容。另一种方法是创建确实验证消息的XML内容的集成测试。

Spring Web Services 2.0引入了对此类端点的集成测试的支持。**提供这种支持的核心类是MockWebServiceClient**。
它提供了一个流式的API来将XML消息发送到Spring应用程序上下文中配置的适当端点。
此外，我们可以设置响应预期、验证响应XML并为我们的端点执行完整的集成测试。

但是，这需要调出整个应用程序上下文，这会减慢测试执行速度。这通常是不可取的，尤其是当我们希望为特定的Web
Service端点创建快速和隔离的测试时。

### 2.2 Spring Boot @WebServiceServerTest

Spring Boot 2.6使用@WebServiceServerTest注解扩展了Web Service测试支持。

**我们可以将其用于仅关注Web Service层而不是加载整个应用程序上下文的测试**。
换句话说，我们可以创建一个仅包含所需@Endpoint bean的测试切片，并且我们可以使用@MockBean mock任何依赖项。

这与Spring Boot已经提供的方便的测试切片注解非常相似，例如@WebMvcTest、@DataJpaTest和其他各种。

## 3. 项目构建

### 3.1 Maven依赖

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web-services</artifactId>
    <version>2.6.1</version>
</dependency>
<dependency>
    <groupId>org.springframework.ws</groupId>
    <artifactId>spring-ws-test</artifactId>
    <version>3.1.3</version>
    <scope>test</scope>
</dependency>
```

### 3.2 Web Service案例

接下来，让我们创建一个简单的Service，用于返回指定产品ID的一些Product数据：

```java

@Endpoint
public class ProductEndpoint {

    @Autowired
    private ProductRepository productRepository;

    @ResponsePayload
    public GetProductResponse getProduct(@RequestPayload GetProductRequest request) {
        GetProductResponse response = new GetProductResponse();
        response.setProduct(productRepository.findProduct(request.getId()));
        return response;
    }
}
```

在这里，我们使用@Endpoint对ProductEndpoint组件进行了标注，它注册以处理适当的XML请求。

getProduct()方法接收请求对象并在返回响应之前从Repository中获取Product数据。Repository的实现细节在这里并不重要。
在我们的例子中，我们可以使用一个简单的内存实现来保持应用程序简单并专注于我们的测试策略。

## 4. 端点测试

最后，我们可以创建一个测试切片并验证我们的XML消息在Web Service层的正确处理：

```java

@WebServiceServerTest
class ProductEndpointIntegrationTest {

    private static final Map<String, String> NAMESPACE_MAPPING = createMapping();

    @Autowired
    private MockWebServiceClient client;

    @MockBean
    private ProductRepository productRepository;

    @Test
    void givenXmlRequest_whenServiceInvoked_thenValidResponse() throws IOException {
        Product product = new Product();
        product.setId("1");
        product.setName("Product 1");

        when(productRepository.findProduct("1")).thenReturn(product);

        StringSource request = new StringSource(
                "<bd:getProductRequest xmlns:bd='http://baeldung.com/spring-boot-web-service'>" +
                        "<bd:id>1</bd:id>" +
                        "</bd:getProductRequest>"
        );
        StringSource response = new StringSource(
                "<bd:getProductResponse xmlns:bd='http://baeldung.com/spring-boot-web-service'>" +
                        "<bd:product>" +
                        "<bd:id>1</bd:id>" +
                        "<bd:name>Product 1</bd:name>" +
                        "</bd:product>" +
                        "</bd:getProductResponse>"
        );

        client.sendRequest(withPayload(request))
                .andExpect(noFault())
                .andExpect(validPayload(new ClassPathResource("webservice/products.xsd")))
                .andExpect(xpath("/bd:getProductResponse/bd:product[1]/bd:name", NAMESPACE_MAPPING)
                        .evaluatesTo("Product 1"))
                .andExpect(payload(response));
    }

    private static Map<String, String> createMapping() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("bd", "http://baeldung.com/spring-boot-web-service");
        return mapping;
    }
}
```

在这里，我们只为集成测试配置了应用程序中使用@Endpoint注解的bean。
换句话说，这个测试片创建了一个简化的应用程序上下文。这有助于我们构建有针对性的快速集成测试，而不会因重复加载整个应用程序上下文而导致性能损失。

重要的是，此注解还配置了MockWebServiceClient以及其他相关的自动配置。
因此，我们可以将MockWebServiceClient注入到我们的测试中，并使用它来发送getProductRequest XML请求，然后是各种流式的expect
API。

expect验证响应XML是否针对给定的XSD schema进行验证，并且它与预期的XML响应匹配。我们还可以使用XPath表达式来评估和比较响应XML中的各种值。

### 4.1 端点协作者

在我们的示例中，我们使用@MockBean来mock ProductEndpoint中所需的Repository。
如果没有这个mock，应用程序上下文将无法启动，因为自动配置被完全禁用。
**换句话说，测试框架在测试执行之前不会配置任何@Component、@Service或@Repository bean**。

但是，如果我们确实需要任何实际的协作者而不是mock，那么我们可以使用@Import声明它们。Spring将扫描这些类，然后根据需要将它们注入到端点。

### 4.2 加载整个上下文

如前所述，@WebServiceServerTest不会加载整个应用程序上下文。
如果我们确实需要为测试加载整个应用程序上下文，那么我们应该考虑将@SpringBootTest与@AutoConfigureMockWebServiceClient结合使用。
然后，我们可以以类似的方式使用MockWebServiceClient发送请求并验证响应，如前所示。

## 5. 总结

在本文中，我们介绍了Spring Boot中引入的@WebServiceServerTest注解。

最初，我们讨论了Web Service应用程序中的Spring Boot测试支持。
接下来，我们看到了如何使用此注解为Web Service层创建测试切片，这有助于构建快速且集中的集成测试。