## 1. 概述

在这个简短的教程中，我们重点介绍如何在Mockito中mock返回值为void的方法。

下面的MyList类将用作测试用例中的mock对象。

本文中我们添加一个新方法：

```java
public class MyList extends AbstractList<String> {

    @Override
    public void add(int index, String element) {

    }
}
```

## 2. 简单的mock和验证

void方法可以与Mockito的doNothing()、doThrow()和doAnswer()方法一起使用，使mock和verify变得直观：

```java
class MockitoVoidMethodsUnitTest {
    @Test
    void whenAddCalledVerified() {
        MyList listMock = mock(MyList.class);
        doNothing().when(listMock).add(isA(Integer.class), isA(String.class));
        listMock.add(0, "");
        verify(listMock, times(1)).add(0, "");
    }
}
```

**但是，doNothing()是Mockito对void方法的默认行为**。

下面的这个测试实际上与上一个实现的功能是一样的：

```java
class MockitoVoidMethodsUnitTest {
    @Test
    public void whenAddCalledVerified() {
        MyList myList = mock(MyList.class);
        myList.add(0, "");
        verify(myList, times(1)).add(0, "");
    }
}
```

doThrow()生成异常：

```java
class MockitoVoidMethodsUnitTest {
    @Test
    void givenNull_addThrows() {
        MyList listMock = mock(MyList.class);
        assertThrows(Exception.class, () -> doThrow().when(listMock).add(isA(Integer.class), isA(String.class)));
        listMock.add(0, null);
    }
}
```

## 3. 参数捕获

**使用doNothing()覆盖默认行为的一个原因是捕获参数**。

在上面的示例中，我们使用verify()方法来检查传递给add()方法的参数。

然而，我们可能需要捕获这些参数，并对它们做更多的处理。

在这些情况下，我们像上面一样使用doNothing()，同时使用到ArgumentCaptor：

```java
class MockitoVoidMethodsUnitTest {
    @Test
    void whenAddCalledValueCpatured() {
        MyList listMock = mock(MyList.class);
        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(listMock).add(any(Integer.class), valueCapture.capture());
        listMock.add(0, "captured");
        assertEquals("captured", valueCapture.getValue());
    }
}
```

## 4. Answer Void调用

一个方法可能会执行比仅仅添加或设置值更复杂的行为。

对于这些情况，我们可以使用Mockito中的Answer来添加我们需要的行为：

```java
class MockitoVoidMethodsUnitTest {
    @Test
    void whenAddCalledAnswered() {
        MyList listMock = mock(MyList.class);
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);
            Object arg1 = invocation.getArgument(1);
            // 这里可对获取到的参数编写自己的逻辑 ...
            assertEquals(3, arg0);
            assertEquals("answer me", arg1);
            return null;
        }).when(listMock).add(any(Integer.class), any(String.class));
        listMock.add(3, "answer me");
    }
}
```

## 5. 部分mock

部分mock也是一种选择。Mockito的doCallRealMethod()可用于void方法：

```java
class MockitoVoidMethodsUnitTest {
    @Test
    void whenAddCalledRealMethodCalled() {
        MyList listMock = mock(MyList.class);
        doCallRealMethod().when(listMock).add(any(Integer.class), any(String.class));
        listMock.add(1, "real");
        verify(listMock, times(1)).add(1, "real");
    }
}
```

**这样，我们就可以调用实际的方法，同时进行验证。**

## 6. 总结

在这篇简短的文章中，我们介绍了在使用Mockito进行测试时处理void方法的四种不同方法。