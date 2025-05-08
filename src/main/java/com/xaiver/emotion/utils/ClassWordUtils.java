package com.xaiver.emotion.utils;

import com.mysql.cj.MysqlType;
import com.xaiver.emotion.model.TableSchema;

public class ClassWordUtils {

    public static String getClassName(TableSchema schema) throws ClassNotFoundException {
        return Class.forName(MysqlType.getByName(schema.getType()).getClassName()).getSimpleName();
    }
}
