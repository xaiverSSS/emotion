package com.xaiver.emotion.utils;

import com.mysql.cj.MysqlType;
import com.xaiver.emotion.model.TableSchema;

public class ClassWordUtils {

    /**
     * 更具schema获取Java Class 类型名
     * @param schema
     * @return
     * @throws ClassNotFoundException
     */
    public static String getClassName(TableSchema schema) throws ClassNotFoundException {
        return getJavaClass(schema).getSimpleName();
    }

    public static Class getJavaClass(TableSchema schema) throws ClassNotFoundException {
        return Class.forName(MysqlType.getByName(schema.getType()).getClassName());
    }
}
