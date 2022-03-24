package ru.otus;

import ru.otus.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ioc {

    private Ioc() {}

    static TestLogging createTestLogging() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLoggingImpl());
        return (TestLogging) Proxy.newProxyInstance(LoggingDemo.class.getClassLoader(),
                new Class<?>[]{TestLogging.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final Object entity;
        private final List<String> methods = new ArrayList<>();

        DemoInvocationHandler(Object entity) {
            this.entity = entity;

            for(Method method : entity.getClass().getDeclaredMethods()){
                if(method.isAnnotationPresent(Log.class)) {
                    methods.add(method.getName() + Arrays.toString(method.getParameterTypes()));
                }
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String needle = method.getName() + Arrays.toString(method.getParameterTypes());

            if (methods.contains(needle)) {
                System.out.println("Executed method: " + method.getName() + ", params: " + args.length);
            }
            return method.invoke(entity, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "Entity = " + entity +
                    '}';
        }
    }
}
