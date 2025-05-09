package com.xaiver.emotion.utils;

import com.mysql.cj.MysqlType;
import com.xaiver.emotion.model.EntitySchema;
import com.xaiver.emotion.model.MapperSchema;
import com.xaiver.emotion.model.TableSchema;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.xaiver.emotion.constants.CommonConstants.*;
import static com.xaiver.emotion.constants.MapperConstants.*;


public class MapperUtils {
    public static String interfaceName(String table){
        return CamelCaseUtils.convertToCamelCase(table, true) +NAME_POST;
    }
    public static String insertFunName(EntitySchema entitySchema){
        return new StringBuffer().append(VOID).append(SPACE)
                .append(INSERT).append(BRACKET0)
                    .append(AN_PARAM).append(BRACKET0).append(QUOTATION2).append(CamelCaseUtils.lowerFirst(entitySchema.getName())).append(QUOTATION2).append(BRACKET1).append(SPACE)
                    .append(entitySchema.getName()).append(SPACE)
                    .append(CamelCaseUtils.lowerFirst(entitySchema.getName()))
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

    public static String updateFunName(EntitySchema entitySchema) {
        return new StringBuffer().append(VOID).append(SPACE)
                .append(UPDATE).append(BRACKET0)
                    .append(AN_PARAM).append(BRACKET0).append(QUOTATION2).append(CamelCaseUtils.lowerFirst(entitySchema.getName())).append(QUOTATION2).append(BRACKET1).append(SPACE)
                    .append(entitySchema.getName()).append(SPACE)
                    .append(CamelCaseUtils.lowerFirst(entitySchema.getName()))
                .append(BRACKET1)
                .append(SEMICOLON)
                .toString();
    }

    public static String selectFunName(EntitySchema entitySchema, TableSchema primary) throws ClassNotFoundException {
        return new StringBuffer().append(entitySchema.getName()).append(SPACE)
                .append(SELECT).append(BRACKET0)
                    .append(AN_PARAM).append(BRACKET0).append(QUOTATION2).append(primary.getProperty()).append(QUOTATION2).append(BRACKET1).append(SPACE)
                    .append(ClassWordUtils.getClassName(primary)).append(SPACE)
                    .append(primary.getProperty())
                .append(BRACKET1)
                .append(SEMICOLON)
                .toString();
    }

    public static void write(MapperSchema schema){
        File mapperFile = file(schema);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(mapperFile))) {
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

            for (MapperSchema.MethodSchema method : schema.getMethods()) {
                String sqlLine = new StringBuffer()
                        .append(TAB2)
                        .append(method.getSqlType().getValue())
                        .append(BRACKET0)
                        .append(method.getSql().toString())
                        .append(BRACKET1)
                        .toString();
                String methodLine = new StringBuffer()
                        .append(TAB2)
                        .append(method.getDeclare())
                        .toString();
                bw.newLine();
                bw.newLine();
                bw.write(sqlLine);
                bw.newLine();
                bw.write(methodLine);
            }

            bw.write(BRACE1);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File file(MapperSchema schema){
        return new File(GENERATE_OUTPUT +File.separator +schema.getName() +JAVA_POST);
    }
}
