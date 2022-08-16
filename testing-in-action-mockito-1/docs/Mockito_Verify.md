## 1. 概述

本文主要介绍如何在各种用例中**使用Mockito verify**。

**旨在以案例为中心且实用** - 不需要多余的细节和解释。

我们将**mock一个简单的List实现**：

```java
public class MyList extends AbstractList<String> {

    @Override
    public String get(final int index) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
```

## 2. 案例

验证mock上的简单调用：

```java
public class MockitoVerifyExamplesIntegrationTest {
    @Test
    void givenInteractionWithMockOccurred_whenVerifyingInteraction_thenCorrect() {
        MyList mockedList = Mockito.mock(MyList.class);
        mockedList.size();
        verify(mockedList).size();
    }
}
```

验证与mock的交互次数：

```java
public class MockitoVerifyExamplesIntegrationTest {
    @Test
    void givenOneInteractionWithMockOccurred_whenVerifyingNumberOfInteractions_thenCorrect() {
        MyList mockedList = mock(MyList.class);
        mockedList.size();
        verify(mockedList, times(1)).size();
    }
}
```

验证与整个mock没有发生交互：

```java
public class MockitoVerifyExamplesIntegrationTest {
    @Test
    void givenNoInteractionWithMockOccurred_whenVerifyingInteractions_thenCorrect() {
        MyList mockedList = mock(MyList.class);
        verifyNoInteractions(mockedList);
    }
}
```

验证没有与特定方法发生交互：

```java
public class MockitoVerifyExamplesIntegrationTest {
    @Test
    void givenNoInteractionWithMethodOfMockOccurred_whenVerifyingInteractions_thenCorrect() {
        MyList mockedList = mock(MyList.class);
        verify(mockedList, times(0)).size();
    }
}
```

验证没有期望的交互 - 此操作应失败：

```java
public class MockitoVerifyExamplesIntegrationTest {
    @Test
    void givenUnverifiedInteraction_whenVerifyingNoUnexpectedInteractions_thenFail() {
        MyList mockedList = mock(MyList.class);
        mockedList.size();
        mockedList.clear();
        verify(mockedList).size();
        assertThrows(NoInteractionsWanted.class, () -> verifyNoMoreInteractions(mockedList));
    }
}
```

验证交互顺序：

```java
public class MockitoVerifyExamplesIntegrationTest {
    @Test
    void whenVerifyingOrderOfInteractions_thenCorrect() {
        MyList mockedList = mock(MyList.class);
        mockedList.size();
        mockedList.add("a parameter");
        mockedList.clear();

        InOrder inOrder = Mockito.inOrder(mockedList);
        inOrder.verify(mockedList).size();
        inOrder.verify(mockedList).add("a parameter");
        inOrder.verify(mockedList).clear();
    }
}
```

验证没有发生交互：

```java
public class MockitoVerifyExamplesIntegrationTest {
    @Test
    void whenVerifyingAnInteractionHasNotOccurred_thenCorrect() {
        MyList mockedList = mock(MyList.class);
        mockedList.size();
        verify(mockedList, never()).clear();
    }
}
```

验证交互至少/至多发生了一定次数：

```java
public class MockitoVerifyExamplesIntegrationTest {
    @Test
    void whenVerifyingAnInteractionHasOccurredAtLeastOnce_thenCorrect() {
        MyList mockedList = mock(MyList.class);
        mockedList.size();
        mockedList.size();
        mockedList.size();
        verify(mockedList, atLeast(1)).size();
        verify(mockedList, atMost(10)).size();
    }
}
```

验证与确切参数的交互：

```java
public class MockitoVerifyExamplesIntegrationTest {
    @Test
    void whenVerifyingAnInteractionWithExactArgument_thenCorrect() {
        MyList mockedList = mock(MyList.class);
        mockedList.add("test");
        verify(mockedList).add("test");
    }
}
```

验证与任何参数的交互：

```java
public class MockitoVerifyExamplesIntegrationTest {
    @Test
    void whenVerifyingAnInteractionWithAnyAugument_thenCorrect() {
        MyList mockedList = mock(MyList.class);
        mockedList.add("test");
        verify(mockedList).add(anyString());
    }
}
```

验证交互使用参数捕获：

```java
public class MockitoVerifyExamplesIntegrationTest {
    @Test
    void whenVerifyingAnInteractionWithArgumentCapture_thenCorrect() {
        MyList mockedList = mock(MyList.class);
        mockedList.addAll(Lists.newArrayList("someElement"));
        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockedList).addAll(argumentCaptor.capture());
        List<String> capturedArgument = argumentCaptor.getValue();
        assertThat(capturedArgument, hasItem("someElement"));
    }
}
```

## 3. 总结

所有这些案例和代码的实现都可以在我的[GitHub]()上找到。这是一个基于Maven的项目，因此可以直接导入并运行。