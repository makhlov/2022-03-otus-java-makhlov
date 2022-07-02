package ru.otus.homework.entity.clazz;

import ru.otus.homework.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;


public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private String entityClassName;
    private Constructor<?> constructor;
    private List<Field> nonIdFields;
    private Field idMarkedField;

    public EntityClassMetaDataImpl(Class<T> entityClass) {
        requireNonNull(entityClass);

        defineClassName(entityClass);
        defineFields(entityClass);
        defineClassConstructor(entityClass);
    }

    private void defineClassName(final Class<T> entityClass) {
        this.entityClassName = entityClass.getSimpleName();
    }

    private void defineFields(final Class<T> entityClass) {
        nonIdFields = new ArrayList<>();
        Field[] declaredFields = entityClass.getDeclaredFields();

        idMarkedField = stream(declaredFields)
                            .filter(f -> f.isAnnotationPresent(Id.class))
                            .findAny().orElseThrow(() -> new RuntimeException("d"));

        nonIdFields = stream(declaredFields)
                            .filter(f -> !f.isAnnotationPresent(Id.class))
                            .collect(Collectors.toList());

        //Вот так делать не стоит:
        /*stream(entityClass.getDeclaredFields()).forEach(
                f -> {
                    if (f.isAnnotationPresent(Id.class)) {
                        idMarkedField = f;
                    }
                    else {
                        nonIdFields.add(f);
                    }
                }
        );*/
    }

    private void defineClassConstructor(final Class<T> entityClass) {
        Constructor<?>[] constructors = entityClass.getDeclaredConstructors();
        this.constructor = stream(constructors)
                .filter(e -> e.getParameterCount() == nonIdFields.size() + 1)
                .findAny().orElseThrow(() -> new RuntimeException("Can't define correct persistence constructor"));
    }

    @Override
    public String getName() {
        return entityClassName;
    }

    @Override
    public Constructor getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idMarkedField;
    }

    @Override
    public List<Field> getAllFields() {
        List<Field> allFields = new ArrayList<>(nonIdFields);
        allFields.add(idMarkedField);
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return this.nonIdFields;
    }
}
