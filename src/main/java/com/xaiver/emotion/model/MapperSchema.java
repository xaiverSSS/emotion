package com.xaiver.emotion.model;

import com.xaiver.emotion.enums.AccessModifierEnum;
import com.xaiver.emotion.enums.SQLTypeEnum;
import lombok.Data;
import org.apache.ibatis.jdbc.SQL;

import java.util.LinkedList;
import java.util.List;

@Data
public class MapperSchema {

    private AccessModifierEnum accessModifier;

    private String name;

    private List<String> columns;
    private List<String> aliasColumns;

    private List<MapperSchema.MethodSchema> methods = new LinkedList<>();

    @Data
    public static class MethodSchema {
        private SQLTypeEnum sqlType;
        private SQL sql;
        private Class javaClass;
        private String name;
    }
}
