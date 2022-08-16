package cn.tuyucheng.taketoday.mockito;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.NoInteractionsWanted;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class MockitoVerifyExamplesIntegrationTest {

    @Test
    @DisplayName("givenInteractionWithMockOccurred_whenVerifyingInteraction_thenCorrect")
    void givenInteractionWithMockOccurred_whenVerifyingInteraction_thenCorrect() {
        MyList mockedList = mock(MyList.class);
        mockedList.size();
        verify(mockedList, times(1)).size();
    }

    @Test
    @DisplayName("givenOneInteractionWithMockOccurred_whenVerifyingNumberOfInteractions_thenCorrect")
    void givenOneInteractionWithMockOccurred_whenVerifyingNumberOfInteractions_thenCorrect() {
        MyList mockedList = mock(MyList.class);
        mockedList.size();
        verify(mockedList, times(1)).size();
    }

    @Test
    @DisplayName("givenNoInteractionWithMockOccurred_whenVerifyingInteractions_thenCorrect")
    void givenNoInteractionWithMockOccurred_whenVerifyingInteractions_thenCorrect() {
        MyList mockedList = mock(MyList.class);
        verifyNoInteractions(mockedList);
    }

    @Test
    @DisplayName("givenNoInteractionWithMethodOfMockOccurred_whenVerifyingInteractions_thenCorrect")
    void givenNoInteractionWithMethodOfMockOccurred_whenVerifyingInteractions_thenCorrect() {
        MyList mockedList = mock(MyList.class);
        verify(mockedList, times(0)).size();
    }

    @Test
    @DisplayName("givenUnverifiedInteraction_whenVerifyingNoUnexpectedInteractions_thenFail")
    void givenUnverifiedInteraction_whenVerifyingNoUnexpectedInteractions_thenFail() {
        MyList mockedList = mock(MyList.class);
        mockedList.size();
        mockedList.clear();
        verify(mockedList).size();
        assertThrows(NoInteractionsWanted.class, () -> verifyNoMoreInteractions(mockedList));
    }

    @Test
    @DisplayName("whenVerifyingOrderOfInteractions_thenCorrect")
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

    @Test
    @DisplayName("whenVerifyingAnInteractionHasNotOccurred_thenCorrect")
    void whenVerifyingAnInteractionHasNotOccurred_thenCorrect() {
        MyList mockedList = mock(MyList.class);
        mockedList.size();
        verify(mockedList, never()).clear();
    }

    @Test
    @DisplayName("whenVerifyingAnInteractionHasOccurredAtLeastOnce_thenCorrect")
    void whenVerifyingAnInteractionHasOccurredAtLeastOnce_thenCorrect() {
        MyList mockedList = mock(MyList.class);
        mockedList.size();
        mockedList.size();
        mockedList.size();
        verify(mockedList, atLeast(1)).size();
        verify(mockedList, atMost(10)).size();
    }

    @Test
    @DisplayName("whenVerifyingAnInteractionWithExactArgument_thenCorrect")
    void whenVerifyingAnInteractionWithExactArgument_thenCorrect() {
        MyList mockedList = mock(MyList.class);
        mockedList.add("test");
        verify(mockedList).add("test");
    }

    @Test
    @DisplayName("whenVerifyingAnInteractionWithAnyAugument_thenCorrect")
    void whenVerifyingAnInteractionWithAnyAugument_thenCorrect() {
        MyList mockedList = mock(MyList.class);
        mockedList.add("test");
        verify(mockedList).add(anyString());
    }

    @Test
    @DisplayName("whenVerifyingAnInteractionWithArgumentCapture_thenCorrect")
    void whenVerifyingAnInteractionWithArgumentCapture_thenCorrect() {
        MyList mockedList = mock(MyList.class);
        mockedList.addAll(Lists.newArrayList("someElement"));
        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockedList).addAll(argumentCaptor.capture());
        List<String> capturedArgument = argumentCaptor.getValue();
        assertThat(capturedArgument, hasItem("someElement"));
    }
}