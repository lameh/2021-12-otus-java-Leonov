package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final String updateQuery;

    private final String insertQuery;

    private final String selectAllQuery;

    private final String selectByIdQuery;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        String clazzName = entityClassMetaData.getName();
        String idName = entityClassMetaData.getIdField().getName();
        List<Field> fieldNames = entityClassMetaData.getFieldsWithoutId();

        this.updateQuery = "UPDATE " + clazzName + " SET "
                + fieldNames.stream().map(f -> f.getName() + " = ?").collect(Collectors.joining(","))
                + " WHERE ID = " + idName;

        this.insertQuery = "INSERT INTO " + clazzName
                + fieldNames.stream().map(Field::getName).collect(Collectors.joining(",","(",")"))
                + " VALUES (?" + (fieldNames.size() > 1 ? ", ?".repeat(fieldNames.size() - 1) + ")" : ")");

        this.selectAllQuery = "SELECT * FROM " + clazzName;

        this.selectByIdQuery = "SELECT * FROM " + clazzName + " WHERE " + idName + " = ?";
    }

    @Override
    public String getSelectAllSql() {
        return selectAllQuery;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdQuery;
    }

    @Override
    public String getInsertSql() {
        return insertQuery;
    }

    @Override
    public String getUpdateSql() {
        return updateQuery;
    }
}
