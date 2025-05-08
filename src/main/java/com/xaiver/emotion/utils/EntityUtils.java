package com.xaiver.emotion.utils;

import com.xaiver.emotion.model.TableSchema;
import static com.xaiver.emotion.constants.CommonConstants.*;

public class EntityUtils {

    public static String completeProperty(TableSchema schema) throws ClassNotFoundException {
        return new StringBuffer().append(SPACE)
                .append(PRIVATE).append(SPACE)
                .append(ClassWordUtils.getClassName(schema)).append(SPACE)
                .append(schema.getProperty())
                .append(SEMICOLON)
                .toString();
    }
}
