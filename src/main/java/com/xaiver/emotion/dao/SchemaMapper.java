package com.xaiver.emotion.dao;

import com.xaiver.emotion.model.TableSchema;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SchemaMapper {

    @Select("SELECT COL.COLUMN_NAME AS `column`, COL.COLUMN_COMMENT AS `comment` ,COL.DATA_TYPE AS `type` FROM INFORMATION_SCHEMA.COLUMNS COL Where  COL.TABLE_NAME= #{table}")
    List<TableSchema> querySchema(String table);

    @Select("SELECT `COLUMN_NAME` FROM `information_schema`.`COLUMNS` WHERE `TABLE_SCHEMA` = #{database} AND `TABLE_NAME` = #{table} AND `COLUMN_KEY` = 'PRI'")
    String queryPrimaryKey(@Param("database") String database, @Param("table") String table);
}
