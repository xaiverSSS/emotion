package com.xaiver.emotion.enums;

public enum AnnotationEnum {

    INSERT("@Insert", "org.apache.ibatis.annotations.Insert"),
    DELETE("@Delete", "org.apache.ibatis.annotations.Delete"),
    UPDATE("@Update", "org.apache.ibatis.annotations.Update"),
    SELECT("@Select", "org.apache.ibatis.annotations.Select");

    AnnotationEnum(String name, String absolutePackage){
        this.name = name;
        this.absolutePackage = absolutePackage;
    }
    private final String name;
    private final String absolutePackage;
}
