package com.xaiver.emotion.service;

import com.xaiver.emotion.dao.SchemaMapper;
import com.xaiver.emotion.model.TableSchema;
import com.xaiver.emotion.utils.CamelCaseUtils;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.xaiver.emotion.constants.SchemaConstants.*;

@Service
public class SchemaService {
    @Resource
    private SchemaMapper schemaMapper;

    public void generate(String table){
        List<TableSchema> schemas = schemaMapper.querySchema(table);

        String columns = packColumns(schemas);
        String aliasColumns = packAliasColumns(schemas, null, table);
        String properties = packProperties(schemas);
        SQL insertSQL = packInsert(schemas, table);
    }

    /**
     * 生成insert语句
     * @param table
     * @return
     */
    private SQL packDelete(String table) {
        Assert.notNull(table, "table name must not null");
        SQL sql = new SQL();
        sql.DELETE_FROM(table);
        sql.WHERE();
        return sql;
    }

    /**
     * 生成insert语句
     * @param schemas
     * @param table
     * @return
     */
    private SQL packInsert(List<TableSchema> schemas, String table) {
        if(null==schemas || schemas.size()<=0){
            return null;
        }
        SQL sql = new SQL();
        sql.INSERT_INTO(table);
        schemas.forEach(e->{
            sql.INTO_COLUMNS(e.getColumn());
            sql.INTO_VALUES(CamelCaseUtils.convertToCamelCase(e.getColumn()));
        });
        return sql;
    }

    /**
     * 生成不带alias的全量字段字符串
     * @param schemas
     * @return
     */
    private String packColumns(List<TableSchema> schemas){
        if(null==schemas || schemas.size()<=0){
            return null;
        }
        StringBuffer sql = new StringBuffer();
        schemas.forEach(e-> sql.append(FLAG1).append(e.getColumn()).append(FLAG1).append(FLAG3).append(SPACE));
        return sql.substring(0, sql.length()-2);
    }

    /**
     * 生成带alias的全量字段字符串
     * @param schemas
     * @return
     */
    private String packAliasColumns(List<TableSchema> schemas, String alias, String table){
        if(null==schemas || schemas.size()<=0){
            return null;
        }
        String alias2 = null==alias?Arrays.stream(table.split(FLAG4)).map(e-> String.valueOf(e.charAt(0))).collect(Collectors.joining()):alias;
        StringBuffer sql = new StringBuffer();
        schemas.forEach(e-> sql.append(alias2).append(FLAG2).append(FLAG1).append(e.getColumn()).append(FLAG1).append(FLAG3).append(SPACE));
        return sql.substring(0, sql.length()-2);
    }
}
