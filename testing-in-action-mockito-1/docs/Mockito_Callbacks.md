## 1. 概述

在这个简短的教程中，我们将重点介绍如何使用测试框架Mockito测试回调。

我们将介绍两种解决方案，首先使用ArgumentCaptor，然后使用更直观的doAnswer()方法。

## 2. 回调简介

**回调是一段作为参数传递给方法的代码，该方法应在给定时间回调(执行)该参数**。

这种执行可能会像在同步回调中一样立即执行，但更常见的是，它可能在稍后的时间执行，就像在异步回调中一样。

**使用回调的一个常见场景是在服务交互期间，当我们需要处理来自服务调用的响应时**。

在本教程中，我们将使用如下所示的Service接口作为测试用例中的协作者(被mock的对象)：

```java
public interface Service {
    void doAction(String request, Callback<Response> callback);
}

public class Response {
    private Data data;
    private boolean isValid = true;
    // getters and setters ...
}

public record Data(String message) {

}
```

在Callback参数中，我们传递一个类，它将使用reply(T response)方法处理响应：

```java
public interface Callback<T> {
    void reply(T response);
}
```

### 2.1 简单的Service

**我们还将使用一个简单的ActionHandler来演示如何传递和调用回调**:

```java
public class ActionHandler {
    private Service service;

    public ActionHandler(Service service) {
        this.service = service;
    }

    public void doAction() {
        service.doAction("our-request", new Callback<Response>() {
            @Override
            public void reply(Response response) {
                handleResponse(response);
            }
        });
    }
}
```

handlerResponse()方法在向Response对象添加一些数据之前检查Response对象是否有效：

```java
public class ActionHandler {
    private void handleResponse(Response response) {
        if (response.isValid())
            response.setData(new Data("Successful data response"));
    }
}
```

**为了清楚起见，我们没有使用Lamda表达式，但service.doAction()调用也可以更简洁地编写**：

```
service.doAction("our-request", this::handleResponse);
```

## 3. 使用ArgumentCaptor

现在让我们看看**如何通过Mockito使用ArgumentCaptor获取Callback对象**：

```java
class ActionHandlerUnitTest {
    @Mock
    private Service service;

    @Captor
    private ArgumentCaptor<Callback<Response>> callbackCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenServiceWithValidResponse_whenCallbackReceived_thenProcessed() {
        ActionHandler handler = new ActionHandler(service);
        handler.doAction();
        verify(service).doAction(anyString(), callbackCaptor.capture());
        Callback<Response> callback = callbackCaptor.getValue();
        Response response = new Response();
        callback.reply(response);
        String expectedMessage = "Successful data response";
        Data data = response.getData();
        assertEquals(expectedMessage, data.message(), "Should receive a successful message: ");
    }
}
```

在这个例子中，我们首先创建一个ActionHandler，然后再调用该handler的doAction()方法。
**这只是Service的doAction()方法调用的包装器，我们在其中调用回调**。

接下来，我们验证是否在mock的Service实例上调用了doAction()，并将anyString()作为第一个参数，
**将callbackCaptor.capture()作为第二个参数传递，这是我们捕获Callback对象的地方**。然后可以使用getValue()方法返回捕获的参数值。

现在我们已经获得了Callback对象，**在直接调用reply()
方法并断言响应数据具有正确的值之前，我们创建了一个默认有效的Response对象**。

## 4. 使用doAnswer()方法

**现在我们来看一个常见的stubbing方法解决方案，
该方法使用Mockito中的Answer对象和doAnswer()方法来stub方法doAction()的回调**：

```java
public class ActionHandlerUnitTest {
    @Test
    void givenServiceWithInvalidResponse_whenCallbackReceived_thenNotProcessed() {
        Response response = new Response();
        response.setIsValid(false);
        doAnswer(invocation -> {
            Callback<Response> callback = invocation.getArgument(1);
            callback.reply(response);
            Data data = response.getData();
            assertNull(data, "No data in invalid response");
            return null;
        }).when(service).doAction(anyString(), any(Callback.class));

        ActionHandler handler = new ActionHandler(service);
        handler.doAction();
    }
}
```

在我们第二个例子中，我们首先创建一个无效的Response对象，该对象将在稍后的测试中使用。

接下来，我们在mock的Service上设置Answer，以便在调用doAction()时，
**我们拦截调用并使用invocation.getArgument(1)获取方法Callback参数**。

最后一步是创建ActionHandler并调用doAction()，从而调用Answer。

## 3. 总结

在这篇简短的文章中，我们介绍了在使用Mockito进行测试时处理测试回调的两种不同方法。