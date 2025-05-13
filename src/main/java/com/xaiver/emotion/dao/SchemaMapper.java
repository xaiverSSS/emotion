package com.xaiver.emotion.dao;

import com.xaiver.emotion.model.TableSchema;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SchemaMapper {

    @Select("SELECT COL.COLUMN_NAME AS `column`, COL.COLUMN_COMMENT AS `comment` ,COL.DATA_TYPE AS `type` FROM INFORMATION_SCHEMA.COLUMNS COL WHERE COL.TABLE_SCHEMA = #{tableSchema} AND COL.TABLE_NAME = #{tableName}")
    List<TableSchema> querySchema(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName);

    @Select("SELECT `COLUMN_NAME` FROM `information_schema`.`COLUMNS` WHERE `TABLE_SCHEMA` = #{database} AND `TABLE_NAME` = #{table} AND `COLUMN_KEY` = 'PRI'")
    String queryPrimaryKey(@Param("database") String database, @Param("table") String table);
}
