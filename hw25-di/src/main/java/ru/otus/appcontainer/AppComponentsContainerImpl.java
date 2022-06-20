package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();
    private final List<Class<?>> configs = new ArrayList<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... appConfig) {
        Arrays.stream(appConfig).filter(meth -> meth.isAnnotationPresent(AppComponentsContainerConfig.class))
                .sorted((cfg1, cfg2) -> {
                    Integer order1 = cfg1.getDeclaredAnnotation(AppComponentsContainerConfig.class).order();
                    Integer order2 = cfg2.getDeclaredAnnotation(AppComponentsContainerConfig.class).order();
                    return order1.compareTo(order2);
                }).forEach(this::processConfig);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        for(Object component : appComponents) {
            if (componentClass.isInstance(component)) {
                return (C) component;
            }
        }
        return null;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        // You code here...
        Constructor<?> constructor = Arrays.stream(configClass.getDeclaredConstructors())
                .filter(constr -> constr.getParameters().length == 0).findFirst()
                .orElseThrow(() -> new RuntimeException("Constructor not found!"));
        try {
            final Object instance = constructor.newInstance();
            List<Method> methods = Arrays.stream(configClass.getDeclaredMethods())
                    .filter(meth -> meth.isAnnotationPresent(AppComponent.class))
                    .sorted((meth1, meth2) -> {
                        Integer order1 = meth1.getDeclaredAnnotation(AppComponent.class).order();
                        Integer order2 = meth2.getDeclaredAnnotation(AppComponent.class).order();
                        return order1.compareTo(order2);
                    }).toList();
            for (Method m : methods) {
                collectComponent(m, instance);
            }
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void collectComponent(Method method, Object instance) {
        Object bean = makeNewBean(method, instance);
        String name = method.getDeclaredAnnotation(AppComponent.class).name();
        appComponents.add(bean);
        appComponentsByName.put(name, bean);
    }

    private Object makeNewBean (Method method, Object instance) {
        try {
            Parameter[] params = method.getParameters();
            Object[] obj = new Object[params.length];
            for (int i = 0; i < obj.length; i++) {
                obj[i] = getAppComponent(params[i].getType());
            }
            return method.invoke(instance, obj);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }
}
