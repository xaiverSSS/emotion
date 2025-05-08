package com.xaiver.emotion.enums;

import lombok.Getter;

@Getter
public enum SQLTypeEnum {

    INSERT("insert"),
    DELETE("delete"),
    UPDATE("update"),
    SELECT("select");

    SQLTypeEnum(String value){
        this.value = value;
    }
    private String value;
}
