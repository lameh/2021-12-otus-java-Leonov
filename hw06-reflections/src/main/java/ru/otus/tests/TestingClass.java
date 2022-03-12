package ru.otus.tests;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class TestingClass {
    public TestingClass() {
    }

    @Before
    void firstMethod() {
        System.out.println("Inside the first method");
    }

    @Test
    void secondMethod() {
        System.out.println("Inside the second method");
    }

    @Test
    void thirdMethod() {
        System.out.println("Inside the third method");
    }

    @Test
    void fourthMethod() {
        System.out.println("Inside the fourth method");
        throw new RuntimeException("Exception from fourth method");
    }

    @After
    void fifthMethod() {
        System.out.println("Inside the fifth method");
    }
}
