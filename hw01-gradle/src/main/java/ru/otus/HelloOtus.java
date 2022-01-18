package ru.otus;

import com.google.common.collect.ComparisonChain;

public class HelloOtus {

    public static void main(String[] args) {
        int number1 = 10;
        int number2 = 20;

        var result =  ComparisonChain.start().compare(number1, number2).result() >= 0 ? "Yes" : "No";

        System.out.println("Is first number greater then second one? " + result);
    }
}
