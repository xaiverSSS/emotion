package com.xaiver.emotion.utils;

import com.mysql.cj.MysqlType;
import com.xaiver.emotion.model.TableSchema;

import java.lang.reflect.Field;
import java.sql.JDBCType;

import static com.xaiver.emotion.constants.MapperConstants.*;

public class MapperUtils {
    public static String insertFunName(String dbDOName){
        return new StringBuffer().append(VOID).append(SPACE)
                .append(INSERT).append(FLAG1)
                    .append(AN_PARAM).append(FLAG1).append(FLAG4).append(CamelCaseUtils.lowerFirst(dbDOName)).append(FLAG4).append(FLAG2).append(SPACE)
                    .append(dbDOName).append(SPACE)
                    .append(CamelCaseUtils.lowerFirst(dbDOName))
                .append(FLAG2)
                .append(FLAG3)
                .toString();
    }

    public static String deleteFunName(TableSchema primary) throws ClassNotFoundException {
        return new StringBuffer().append(VOID).append(SPACE)
                .append(DELETE).append(FLAG1)
                    .append(AN_PARAM).append(FLAG1).append(FLAG4).append(primary.getProperty()).append(FLAG4).append(FLAG2).append(SPACE)
                    .append(Class.forName(MysqlType.getByName(primary.getType()).getClassName()).getSimpleName()).append(SPACE)
                    .append(primary.getProperty())
                .append(FLAG2)
                .append(FLAG3)
                .toString();
    }

    public static String updateFunName(String dbDOName) {
        return new StringBuffer().append(VOID).append(SPACE)
                .append(UPDATE).append(FLAG1)
                    .append(AN_PARAM).append(FLAG1).append(FLAG4).append(CamelCaseUtils.lowerFirst(dbDOName)).append(FLAG4).append(FLAG2).append(SPACE)
                    .append(dbDOName).append(SPACE)
                    .append(CamelCaseUtils.lowerFirst(dbDOName))
                .append(FLAG2)
                .append(FLAG3)
                .toString();
    }

    public static String selectFunName(String dbDOName, TableSchema primary) throws ClassNotFoundException {
        return new StringBuffer().append(dbDOName).append(SPACE)
                .append(SELECT).append(FLAG1)
                    .append(AN_PARAM).append(FLAG1).append(FLAG4).append(primary.getProperty()).append(FLAG4).append(FLAG2).append(SPACE)
                    .append(Class.forName(MysqlType.getByName(primary.getType()).getClassName()).getSimpleName()).append(SPACE)
                    .append(primary.getProperty())
                .append(FLAG2)
                .append(FLAG3)
                .toString();
    }
}
