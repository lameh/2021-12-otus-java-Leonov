package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;

    private final EntityClassMetaData<?> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<?> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    private T createInstance(List<Field> fields, Constructor<?> constructor, ResultSet resultSet) throws SQLException, ReflectiveOperationException {
        Object o = constructor.newInstance();

        for (var f : fields) {
            f.setAccessible(true);
            f.set(o, resultSet.getObject(f.getName()));
        }
        return (T) o;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        final Constructor<?> constructor = entityClassMetaData.getConstructor();
        final List<Field> fields = entityClassMetaData.getAllFields();

        String selectByIdQuery = entitySQLMetaData.getSelectByIdSql();

        return dbExecutor.executeSelect(connection, selectByIdQuery, List.of(id), resultSet -> {
            try {
                T obj = null;
                if (resultSet.next()) {
                    obj = createInstance(fields, constructor, resultSet);
                }
                return obj;
            } catch (SQLException | ReflectiveOperationException ex) {
                throw new DataTemplateException(ex);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        final Constructor<?> constructor = entityClassMetaData.getConstructor();
        final List<Field> fields = entityClassMetaData.getAllFields();

        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), resultSet -> {
           var res = new ArrayList<T>();
           try{
               while (resultSet.next()) {
                   res.add(createInstance(fields, constructor, resultSet));
               }
               return res;
           } catch (SQLException | ReflectiveOperationException ex) {
               throw new DataTemplateException(ex);
           }
        }).orElseThrow(() -> new RuntimeException("Error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        List<Field> fields = entityClassMetaData.getFieldsWithoutId();
        List<Object> params = new ArrayList<>();

        try{
            for (var f : fields) {
                f.setAccessible(true);
                params.add(f.get(client));
            }
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), params);
        } catch (Exception ex) {
            throw new DataTemplateException(ex);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        List<Field> fields = entityClassMetaData.getFieldsWithoutId();
        List<Object> params = new ArrayList<>();

        try {
            for (var f : fields) {
                f.setAccessible(true);
                params.add(f.get(client));
            }
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
        } catch (Exception ex) {
            throw new DataTemplateException(ex);
        }
    }
}
