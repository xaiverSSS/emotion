package com.xaiver.emotion.utils;

import com.xaiver.emotion.model.EntitySchema;
import com.xaiver.emotion.model.TableSchema;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.xaiver.emotion.constants.CommonConstants.*;
import static com.xaiver.emotion.constants.EntityConstants.NAME_POST;

public class EntityUtils {

    public static String entityName(String table){
        return CamelCaseUtils.convertToCamelCase(table, true) +NAME_POST;
    }

    public static String completeProperty(TableSchema schema) throws ClassNotFoundException {
        return new StringBuffer().append(SPACE)
                .append(PRIVATE).append(SPACE)
                .append(ClassWordUtils.getClassName(schema)).append(SPACE)
                .append(schema.getProperty())
                .append(SEMICOLON)
                .toString();
    }

    public static void write(EntitySchema schema){
        File entityFile = file(schema);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(entityFile))) {
            String packageLine = PACKAGE +SPACE +schema.getName() +SEMICOLON;
            bw.write(packageLine);
            bw.newLine();
            bw.newLine();

            String declareLine = new StringBuffer().append(CamelCaseUtils.convertToCamelCase(schema.getAccessModifier().getValue(), true)).append(SPACE)
                    .append(CLAZZ).append(SPACE)
                    .append(schema.getName()).append(SPACE)
                    .append(BRACE0).append(SPACE)
                    .toString();
            bw.write(declareLine);
            bw.newLine();
            bw.newLine();

            for (EntitySchema.PropSchema prop : schema.getProps()) {
                String propLine = TAB2 +PRIVATE +SPACE +prop.getJavaClass().getSimpleName() +SPACE +prop.getName() +SEMICOLON;
                bw.write(propLine);
                bw.newLine();
                bw.newLine();
            }

            bw.write(BRACE1);
            bw.flush();
        } catch (IOException e) {
            System.out.println("写入文件时发生错误：" + e.getMessage());
        }
    }

    private static File file(EntitySchema entitySchema){
        return new File(GENERATE_OUTPUT +File.separator +entitySchema.getName() +JAVA_POST);
    }
}
