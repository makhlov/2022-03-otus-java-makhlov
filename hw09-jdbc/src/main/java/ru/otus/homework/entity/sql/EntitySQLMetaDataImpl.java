package ru.otus.homework.entity.sql;

import ru.otus.homework.entity.clazz.EntityClassMetaData;

import java.lang.reflect.Field;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private final EntityClassMetaData<T> entityClassMetaData;

    private final String updateAttributes;
    private final String insertAttributes;
    private final String selectAttributes;
    private final String insertValues;
    private final String idField;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;

        updateAttributes = buildAttributesForUpdate();
        insertAttributes = buildAttributesForInsert();
        selectAttributes = buildAttributesForSelect();
        insertValues = buildSubstitutionsForInsert(entityClassMetaData.getFieldsWithoutId().size());
        idField = entityClassMetaData.getIdField().getName();
    }

    /* =========================== */
    /*        Implementation       */
    /* =========================== */

    @Override
    public String getSelectAllSql() {
        return format("select %s from %s",
                selectAttributes,
                entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return format("%s where %s = ? ", 
                getSelectAllSql(), 
                idField);
    }

    @Override
    public String getInsertSql() {
        return format("insert into %s (%s) values (%s)",
                entityClassMetaData.getName(),
                insertAttributes,
                insertValues
        );
    }
    
    @Override
    public String getUpdateSql() {
        return format("update %s set %s where %s = ?",
                entityClassMetaData.getName(),
                updateAttributes,
                idField);
    }

    /* =========================== */

    private String buildAttributesForUpdate() {
        return entityClassMetaData.getFieldsWithoutId()
                .stream()
                .map(field -> format("%s = ?", field.getName()))
                .collect(joining(","));
    }

    private String buildAttributesForInsert() {
        return entityClassMetaData.getFieldsWithoutId()
                .stream()
                .map(Field::getName)
                .collect(joining(","));
    }

    private String buildSubstitutionsForInsert(int amount) {
        return "?" + ",?".repeat(amount - 1);
    }

    private String buildAttributesForSelect() {
        return entityClassMetaData.getAllFields()
                .stream()
                .map(Field::getName)
                .collect(joining(","));
    }
}