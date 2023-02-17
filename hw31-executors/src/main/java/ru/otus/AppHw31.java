package ru.otus;

import ru.otus.threads.CommonData;
import ru.otus.threads.NumThread;

public class AppHw31 {

    public static final CommonData COMMON_DATA = new CommonData("NumThread-2");

    public static void main(String[] args) {

        new NumThread("NumThread-1", COMMON_DATA).start();
        new NumThread("NumThread-2", COMMON_DATA).start();
    }
}
