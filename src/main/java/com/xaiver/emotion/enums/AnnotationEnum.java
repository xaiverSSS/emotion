package com.xaiver.emotion.enums;

import lombok.Getter;

@Getter
public enum AnnotationEnum {

    INSERT("@Insert", "org.apache.ibatis.annotations.Insert"),
    DELETE("@Delete", "org.apache.ibatis.annotations.Delete"),
    UPDATE("@Update", "org.apache.ibatis.annotations.Update"),
    SELECT("@Select", "org.apache.ibatis.annotations.Select"),
    PARAM("@Param", "org.apache.ibatis.annotations.Param");

    AnnotationEnum(String anno, String file){
        this.anno = anno;
        this.file = file;
    }
    private final String anno;
    private final String file;
}
