package com.xaiver.emotion.enums;

import lombok.Getter;

@Getter
public enum SQLTypeEnum {

    INSERT("@Insert"),
    DELETE("@Delete"),
    UPDATE("@Update"),
    SELECT("@Select");

    SQLTypeEnum(String value){
        this.value = value;
    }
    private String value;
}
