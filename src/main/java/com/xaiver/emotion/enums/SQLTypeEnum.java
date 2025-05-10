package com.xaiver.emotion.enums;

import lombok.Getter;

@Getter
public enum SQLTypeEnum {

    INSERT("@Insert"),
    DELETE("@Delete"),
    UPDATE("@Update"),
    SELECT("@Select");

    SQLTypeEnum(String annotation){
        this.annotation = annotation;
    }
    private String annotation;
}
