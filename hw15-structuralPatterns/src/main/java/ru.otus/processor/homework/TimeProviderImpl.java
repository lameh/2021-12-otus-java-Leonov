package ru.otus.processor.homework;

import java.time.LocalDateTime;

public class TimeProviderImpl implements TimeProvider{
    @Override
    public LocalDateTime getTime() {
        return LocalDateTime.now();
    }
}
