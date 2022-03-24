package ru.otus;

import ru.otus.annotation.Log;

public class TestLoggingImpl implements TestLogging {

    @Log
    @Override
    public void calculation(int param1) {
        System.out.println("Inside calculation(int) method");
    }

    @Override
    public void calculation(int param1, int param2) {
        System.out.println("Inside calculation(int, int) method");
    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {
        System.out.println("Inside calculation(int, int, String) method");
    }
}
