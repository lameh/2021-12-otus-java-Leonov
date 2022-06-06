package ru.otus.jdbc.mapper;

import ru.otus.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<?> clazz;

    private final Field id;

    private final List<Field> allFields;

    private final List<Field> fieldsHasNoId;

    private final Constructor<T> constructor;

    public EntityClassMetaDataImpl(Class<?> clazz) {
        this.clazz = clazz;
        this.id = Arrays.stream(clazz.getDeclaredFields()).filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst().orElseThrow();
        this.allFields = List.of(clazz.getDeclaredFields());
        this.fieldsHasNoId = Arrays.stream(clazz.getDeclaredFields()).filter(f -> !f.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
        try {
            this.constructor = (Constructor<T>) clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException();
        }
    }


    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return id;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsHasNoId;
    }
}
