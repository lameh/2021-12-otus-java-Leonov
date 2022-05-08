package ru.otus.handler.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.homework.ProcessorAtTimeException;
import ru.otus.processor.homework.TimeProvider;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessorAtTimeExceptionTest {

    @Test
    @DisplayName("Тест исключения в чётную секунду")
    void atTimeThrowException() {
        Message message = new Message.Builder(1L).field1("field1").field2("field2").field3("field3").build();
        TimeProvider timeProvider = mock(TimeProvider.class);
        ProcessorAtTimeException processor = new ProcessorAtTimeException(timeProvider);
        int seconds = 2;

        when(timeProvider.getTime())
                .thenReturn(LocalDateTime.of(2022, 4, 29, 0, 0, seconds));

        assertThrows(RuntimeException.class, () -> processor.process(message));
    }

    @Test
    @DisplayName("Тест исключения в нечётную секунду")
    void atTimeNotThrowException() {
        Message message = new Message.Builder(1L).field1("field1").field2("field2").field3("field3").build();
        TimeProvider timeProvider = mock(TimeProvider.class);
        ProcessorAtTimeException processor = new ProcessorAtTimeException(timeProvider);
        int seconds = 1;

        when(timeProvider.getTime())
                .thenReturn(LocalDateTime.of(2022, 4, 29, 0, 0, seconds));

        assertDoesNotThrow(() -> processor.process(message));
    }
}
