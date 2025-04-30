package com.xaiver.emotion.dao;

import com.xaiver.emotion.model.TableSchema;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SchemaMapper {

    @Select("SELECT COL.COLUMN_NAME AS `column`, COL.COLUMN_COMMENT AS `comment` ,COL.DATA_TYPE AS `type` FROM INFORMATION_SCHEMA.COLUMNS COL Where  COL.TABLE_NAME= #{table}")
    List<TableSchema> querySchema(String table);
}
