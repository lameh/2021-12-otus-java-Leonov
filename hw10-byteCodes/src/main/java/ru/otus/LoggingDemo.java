package ru.otus;

public class LoggingDemo {

    public static void main(String[] args) {
        TestLogging obj = Ioc.createTestLogging();

        obj.calculation(1);
        obj.calculation(1, 2);
        obj.calculation(1, 2, "three");
    }
}
