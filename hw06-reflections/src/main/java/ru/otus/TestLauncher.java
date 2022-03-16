package ru.otus;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class TestLauncher {

    public static void launch(String clazzName) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(clazzName);
        Method[] methods = clazz.getDeclaredMethods();
        if (methods.length == 0) {
            System.out.println("Class have no methods");
        }

        List<String> methodsBefore = new ArrayList();
        List<String> methodsTest = new ArrayList();
        List<String> methodsAfter = new ArrayList();

        for(int i = 0; i < methods.length; i++) {
            Annotation[] annotations = methods[i].getDeclaredAnnotations();

            for(int j = 0; j < annotations.length; j++) {
                if (annotations[j] instanceof Before) {
                    methodsBefore.add(methods[i].getName());
                } else if (annotations[j] instanceof Test) {
                    methodsTest.add(methods[i].getName());
                } else if (annotations[j] instanceof After) {
                    methodsAfter.add(methods[i].getName());
                }
            }
        }

        if (methodsTest.isEmpty()) {
            System.out.println("Class has no test methods");
        }

        Map<String, Exception> result = getTestingEntity(methodsTest, methodsBefore, methodsAfter, clazz, clazzName);
        int failed = 0;
        Iterator tempRes = result.entrySet().iterator();

        while(tempRes.hasNext()) {
            Entry<String, Exception> res = (Entry)tempRes.next();
            Exception exception = res.getValue();
            System.out.println("\n" + clazzName + " : " + res.getKey() + (exception == null ? " COMPLETED" : " FAILED " + exception));
            if (res.getValue() != null) {
                ++failed;
            }
        }

        System.out.println("\nTests completed: " + (methodsTest.size() - failed));
        System.out.println("Tests failed: " + failed);
        System.out.println("Total tests: " + methodsTest.size());
    }

    private static HashMap<String, Exception> getTestingEntity(List<String> methodsTest, List<String> methodsBefore, List<String> methodsAfter, Class<?> clazz, String clazzName) {
        HashMap<String, Exception> result = new HashMap();

        for(int k = 0; k < methodsTest.size(); k++) {
            Object obj;
            try {
                obj = ReflectionHelper.instantiate(clazz, new Object[0]);
                System.out.println("\n" + clazzName + " is instance for method " + (String)methodsTest.get(k));
            } catch (RuntimeException re) {
                System.out.println("\n" + clazzName + " instance not created");
                throw new RuntimeException("Object not created");
            }

            RuntimeException testEx = null;

            try {
                for(int j = 0; j < methodsBefore.size(); j++) {
                    ReflectionHelper.callMethod(obj, methodsBefore.get(j));
                }
            } catch (RuntimeException re) {
                System.out.println("@Before can't execute");
                testEx = re;
            }

            try {
                ReflectionHelper.callMethod(obj, methodsTest.get(k));
            } catch (RuntimeException re) {
                System.out.println("@Test " + methodsTest.get(k) + " can't execute");
                testEx = re;
            }

            try {
                for(int j = 0; j < methodsBefore.size(); j++) {
                    ReflectionHelper.callMethod(obj, methodsAfter.get(j));
                }
            } catch (RuntimeException re) {
                System.out.println("@After can't execute");
                testEx = re;
            }

            result.put(methodsTest.get(k), testEx);
        }
        return result;
    }
}
