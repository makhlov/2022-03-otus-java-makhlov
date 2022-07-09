package ru.otus.homework;

import ru.otus.homework.entity.clazz.EntityClassMetaData;
import ru.otus.homework.entity.sql.EntitySQLMetaData;
import ru.otus.necessary.repository.DataTemplate;
import ru.otus.necessary.repository.DataTemplateException;
import ru.otus.necessary.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;

    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    private List getFieldValues(T object, List<Field> fields) {
        return fields.stream()
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        return field.get(object);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Field is unavailable");
                    }
                })
                .collect(toList());
    }

    private T mapToObject(ResultSet rs) throws SQLException {
        List<Object> entityFields = new ArrayList<>();

        /* Получаем ID сущности из RS */
        long idValue = rs.getLong(entityClassMetaData.getIdField().getName());
        entityFields.add(idValue);

        /* Получаем прочие поля сущности из RS */
        entityClassMetaData.getFieldsWithoutId().forEach(
                f -> {
                    try {
                        entityFields.add(rs.getObject(f.getName()));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        /* Пробуем создать сущность по полученным из RS полям */
        try {
            return entityClassMetaData.getConstructor().newInstance(entityFields.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /* =========================== */
    /*        Implementation       */
    /* =========================== */

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return mapToObject(rs);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var list = new ArrayList<T>();
            try {
                while (rs.next()) {
                    list.add(mapToObject(rs));
                }
                return list;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Inability to consume all entities"));
    }

    @Override
    public void update(Connection connection, T object) {
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), getFieldValues(object, entityClassMetaData.getAllFields()));
    }

    @Override
    public long insert(Connection connection, T object) {
        return dbExecutor.executeStatement(
                connection,
                entitySQLMetaData.getInsertSql(),
                getFieldValues(object, entityClassMetaData.getFieldsWithoutId())
        );
    }
}
