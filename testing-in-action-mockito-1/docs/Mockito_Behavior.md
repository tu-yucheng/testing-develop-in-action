## 1. 概述

本文演示了如何使用Mockito在各种示例和用例中配置行为。

**旨在以案例为中心且实用** - 不需要多余的细节和解释。

我们将mock一个简单的List实现，它与我们在上一篇文章中使用的实现相同：

```java
public class MyList extends AbstractList<String> {

    @Override
    public String get(final int index) {
        return null;
    }

    @Override
    public int size() {
        return 1;
    }
}
```

## 2. 案例

为mock配置简单的返回行为：

```java
class MockitoConfigExamplesIntegrationTest {

    @Test
    void whenMockReturnBehaviorIsConfigured_thenBehaviorIsVerified() {
        MyList listMock = mock(MyList.class);
        when(listMock.add(anyString())).thenReturn(false);
        boolean added = listMock.add(randomAlphabetic(6));
        assertThat(added, is(false));
    }
}
```

以另一种方式配置mock的返回行为：

```java
class MockitoConfigExamplesIntegrationTest {
    @Test
    void whenMockReturnBehaviorIsConfigured2_thenBehaviorIsVerified() {
        MyList listMock = mock(MyList.class);
        doReturn(false).when(listMock).add(anyString());
        boolean added = listMock.add(randomAlphabetic(6));
        assertThat(added, is(false));
    }
}
```

配置mock以在方法调用上抛出异常：

```java
class MockitoConfigExamplesIntegrationTest {
    @Test
    void givenMethodIsConfiguredToThrowException_whenCallingMethod_thenExceptionIsThrown() {
        MyList listMock = mock(MyList.class);
        when(listMock.add(anyString())).thenThrow(IllegalStateException.class);
        assertThrows(IllegalStateException.class, () -> listMock.add(randomAlphabetic(6)));
    }
}
```

配置返回类型为void的方法的行为 - 以引发异常：

```java
class MockitoConfigExamplesIntegrationTest {
    @Test
    void whenMethodHasNoReturnType_whenConfiguringBehaviorOfMethod_thenPossible() {
        MyList listMock = mock(MyList.class);
        doThrow(NullPointerException.class).when(listMock).clear();
        assertThrows(NullPointerException.class, listMock::clear);
    }
}
```

配置多个调用的行为：

```java
class MockitoConfigExamplesIntegrationTest {
    @Test
    void givenMethodIsConfiguredToMultipleCalls_whenCallingMethod_thenExceptionIsThrown() {
        MyList listMock = mock(MyList.class);
        when(listMock.add(anyString())).thenReturn(false).thenThrow(IllegalStateException.class);
        boolean added = listMock.add(randomAlphabetic(6));
        assertThat(added, is(false));
        assertThrows(IllegalStateException.class, () -> listMock.add(randomAlphabetic(6)));
    }
}
```

配置spy的行为：

```java
class MockitoConfigExamplesIntegrationTest {
    @Test
    void givenSpy_whenConfiguringBehaviorOfSpy_thenCorrectlyConfigured() {
        final MyList instance = new MyList();
        final MyList spy = Mockito.spy(instance);
        doThrow(NullPointerException.class).when(spy).size();
        assertThrows(NullPointerException.class, spy::size);
    }
}
```

配置方法以在mock上调用真实的底层方法：

```java
class MockitoConfigExamplesIntegrationTest {
    @Test
    void whenMockMethodCallingIsConfiguredToCallTheRealMethod_thenRealMethodIsCalled() {
        MyList listMock = mock(MyList.class);
        when(listMock.size()).thenCallRealMethod();
        assertThat(listMock.size(), equalTo(1));
    }
}
```

使用自定义Answer配置mock方法调用：

```java
class MockitoConfigExamplesIntegrationTest {
    @Test
    void whenMockMethodCallIsConfiguredWithCustomAnswer_thenRealMethodIsCalled() {
        MyList listMock = mock(MyList.class);
        doAnswer(invocation -> "总是一样").when(listMock).get(anyInt());
        String element = listMock.get(1);
        assertThat(element, is(equalTo("总是一样")));
    }
}
```

## 3. 总结

所有这些案例和代码片段的实现都可以在我的[GitHub]()上找到。
这是一个基于Maven的项目，因此可以直接导入并运行。