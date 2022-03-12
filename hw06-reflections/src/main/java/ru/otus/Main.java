package ru.otus;

import ru.otus.tests.TestingClass;

public class Main {

    public static void main(String[] args) {
        try {
            TestLauncher.launch(TestingClass.class.getName());
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Error in class name");
            cnfe.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
    }
}
