package com.xaiver.emotion.enums;

import lombok.Getter;

/**
 * 权限修饰符
 */
@Getter
public enum AccessModifierEnum {

    PRIVATE("private"),
    PUBLIC("public"),
    PROTECTED("protected"),
    DEFAULT("");

    AccessModifierEnum(String value){
        this.value = value;
    }
    private String value;
}
