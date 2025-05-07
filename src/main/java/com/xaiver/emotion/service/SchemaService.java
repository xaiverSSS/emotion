package com.xaiver.emotion.service;

import com.xaiver.emotion.dao.SchemaMapper;
import com.xaiver.emotion.model.TableSchema;
import com.xaiver.emotion.utils.CamelCaseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.xaiver.emotion.constants.SchemaConstants.*;

@Slf4j
@Service
public class SchemaService {
    @Resource
    private SchemaMapper schemaMapper;

    ThreadLocal<String> tableTL = new ThreadLocal<>();
    ThreadLocal<String> databaseTL = new ThreadLocal<>();
    ThreadLocal<String> primaryTL = new ThreadLocal<>();
    ThreadLocal<List<TableSchema>> schemasTL = new ThreadLocal<>();
    public void generate(String database, String table){
        try {
            databaseTL.set(database);
            tableTL.set(table);
            String primary = schemaMapper.queryPrimaryKey(database, table);
            primaryTL.set(primary);
            List<TableSchema> schemas = schemaMapper.querySchema(table);
            schemasTL.set(schemas);
            if(null==schemas || schemas.isEmpty()){
                return;
            }
            schemas.forEach(e->e.setProperty(CamelCaseUtils.convertToCamelCase(e.getColumn())));

            String columns = packColumns();
            log.info("columns:{}", columns);
            String aliasColumns = packAliasColumns(null);
            log.info("aliasColumns:{}", aliasColumns);
            SQL insertSQL = packInsert();
            log.info("insertSQL:{}", insertSQL);
            SQL deleteSQL = packDelete();
            log.info("deleteSQL:{}", deleteSQL);
            SQL selectSQL = packSelect();
            log.info("selectSQL:{}", selectSQL);
            SQL updateSQL = packUpdate();
            log.info("updateSQL:{}", updateSQL);
        }finally {
            databaseTL.remove();
            tableTL.remove();
            primaryTL.remove();
            schemasTL.remove();
        }
    }

    /**
     * 生成Update语句
     * @return
     */
    private SQL packUpdate() {
        SQL sql = new SQL();
        sql.UPDATE(tableTL.get());
        schemasTL.get().forEach(e-> sql.SET(e.getColumn() +" = "+FLAG5+ e.getProperty() +FLAG6));
        sql.WHERE(packPrimaryWhereClause());
        return sql;
    }
    /**
     * 生成Select语句
     * @return
     */
    private SQL packSelect() {
        SQL sql = new SQL();
        schemasTL.get().forEach(e-> sql.SELECT(e.getColumn()));
        sql.FROM(tableTL.get());
        sql.WHERE(packPrimaryWhereClause());
        return sql;
    }

    /**
     * 生成delete语句
     * @return
     */
    private SQL packDelete() {
        SQL sql = new SQL();
        sql.DELETE_FROM(tableTL.get());
        sql.WHERE(packPrimaryWhereClause());
        return sql;
    }

    /**
     * 生成insert语句
     * @return
     */
    private SQL packInsert() {
        SQL sql = new SQL();
        sql.INSERT_INTO(tableTL.get());
        schemasTL.get().forEach(e->{
            sql.INTO_COLUMNS(e.getColumn());
            sql.INTO_VALUES(FLAG5+ e.getProperty() +FLAG6);
        });
        return sql;
    }

    /**
     * 生成不带alias的全量字段字符串
     * @return
     */
    private String packColumns(){
        StringBuffer sql = new StringBuffer();
        schemasTL.get().forEach(e-> sql.append(FLAG1).append(e.getColumn()).append(FLAG1).append(FLAG3).append(SPACE));
        return sql.substring(0, sql.length()-2);
    }

    /**
     * 生成带alias的全量字段字符串
     * @return
     */
    private String packAliasColumns(String alias){
        String alias2 = null==alias?Arrays.stream(tableTL.get().split(FLAG4)).map(e-> String.valueOf(e.charAt(0))).collect(Collectors.joining()):alias;
        StringBuffer sql = new StringBuffer();
        schemasTL.get().forEach(e-> sql.append(alias2).append(FLAG2).append(FLAG1).append(e.getColumn()).append(FLAG1).append(FLAG3).append(SPACE));
        return sql.substring(0, sql.length()-2);
    }

    private String packPrimaryWhereClause(){
        return primaryTL.get() +" = "+ FLAG5 +primaryTL.get()+ FLAG6;
    }
}
