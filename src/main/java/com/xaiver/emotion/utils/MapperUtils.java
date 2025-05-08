package com.xaiver.emotion.utils;

import com.mysql.cj.MysqlType;
import com.xaiver.emotion.model.TableSchema;
import static com.xaiver.emotion.constants.CommonConstants.*;
import static com.xaiver.emotion.constants.MapperConstants.*;


public class MapperUtils {
    public static String insertFunName(String dbDOName){
        return new StringBuffer().append(VOID).append(SPACE)
                .append(INSERT).append(BRACKET0)
                    .append(AN_PARAM).append(BRACKET0).append(QUOTATION2).append(CamelCaseUtils.lowerFirst(dbDOName)).append(QUOTATION2).append(BRACKET1).append(SPACE)
                    .append(dbDOName).append(SPACE)
                    .append(CamelCaseUtils.lowerFirst(dbDOName))
                .append(BRACKET1)
                .append(SEMICOLON)
                .toString();
    }

    public static String deleteFunName(TableSchema primary) throws ClassNotFoundException {
        return new StringBuffer().append(VOID).append(SPACE)
                .append(DELETE).append(BRACKET0)
                    .append(AN_PARAM).append(BRACKET0).append(QUOTATION2).append(primary.getProperty()).append(QUOTATION2).append(BRACKET1).append(SPACE)
                    .append(Class.forName(MysqlType.getByName(primary.getType()).getClassName()).getSimpleName()).append(SPACE)
                    .append(primary.getProperty())
                .append(BRACKET1)
                .append(SEMICOLON)
                .toString();
    }

    public static String updateFunName(String dbDOName) {
        return new StringBuffer().append(VOID).append(SPACE)
                .append(UPDATE).append(BRACKET0)
                    .append(AN_PARAM).append(BRACKET0).append(QUOTATION2).append(CamelCaseUtils.lowerFirst(dbDOName)).append(QUOTATION2).append(BRACKET1).append(SPACE)
                    .append(dbDOName).append(SPACE)
                    .append(CamelCaseUtils.lowerFirst(dbDOName))
                .append(BRACKET1)
                .append(SEMICOLON)
                .toString();
    }

    public static String selectFunName(String dbDOName, TableSchema primary) throws ClassNotFoundException {
        return new StringBuffer().append(dbDOName).append(SPACE)
                .append(SELECT).append(BRACKET0)
                    .append(AN_PARAM).append(BRACKET0).append(QUOTATION2).append(primary.getProperty()).append(QUOTATION2).append(BRACKET1).append(SPACE)
                    .append(ClassWordUtils.getClassName(primary)).append(SPACE)
                    .append(primary.getProperty())
                .append(BRACKET1)
                .append(SEMICOLON)
                .toString();
    }
}
