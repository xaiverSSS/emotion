package com.xaiver.emotion.service;

import com.xaiver.emotion.dao.SchemaMapper;
import com.xaiver.emotion.model.TableSchema;
import com.xaiver.emotion.utils.CamelCaseUtils;
import com.xaiver.emotion.utils.EntityUtils;
import com.xaiver.emotion.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static com.xaiver.emotion.constants.CommonConstants.*;
import static com.xaiver.emotion.constants.SchemaConstants.*;

@Slf4j
@Service
public class SchemaService {
    @Resource
    private SchemaMapper schemaMapper;

    private ThreadLocal<String> tableTL = new ThreadLocal<>();
    private ThreadLocal<String> databaseTL = new ThreadLocal<>();
    private ThreadLocal<TableSchema> primaryTL = new ThreadLocal<>();
    private ThreadLocal<List<TableSchema>> schemasTL = new ThreadLocal<>();
    private ThreadLocal<String> dbDONameTL = new ThreadLocal<>();
    public void generate(String database, String table) throws ClassNotFoundException {
        try {
            databaseTL.set(database);
            tableTL.set(table);
            dbDONameTL.set(CamelCaseUtils.convertToCamelCase(table, true));
            String primary = schemaMapper.queryPrimaryKey(database, table);

            List<TableSchema> schemas = schemaMapper.querySchema(table);
            schemas.forEach(e->{
                if(primary.equalsIgnoreCase(e.getColumn())){
                    e.setProperty(CamelCaseUtils.convertToCamelCase(e.getColumn()));
                    primaryTL.set(e);
                }
            });
            schemasTL.set(schemas);
            if(schemas.isEmpty()){
                return;
            }
            schemas.forEach(e->e.setProperty(CamelCaseUtils.convertToCamelCase(e.getColumn())));

            mkDBEntity();

            mkMapperInterface();

        }finally {
            databaseTL.remove();
            tableTL.remove();
            primaryTL.remove();
            schemasTL.remove();
            dbDONameTL.remove();
        }
    }

    /**
     * 生成DO Entity
     */
    private void mkDBEntity() throws ClassNotFoundException {
        List<String> props = new ArrayList<>();
        for (TableSchema e : schemasTL.get()) {
            props.add(EntityUtils.completeProperty(e));
        }
        log.info("props:{}", props);
    }

    /**
     * 生成Mapper Interface
     * @throws ClassNotFoundException
     */
    private void mkMapperInterface() throws ClassNotFoundException {
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
        String insertFunName = MapperUtils.insertFunName(dbDONameTL.get());
        log.info("insertFunName:{}", insertFunName);
        String deleteFunName = MapperUtils.deleteFunName(primaryTL.get());
        log.info("deleteFunName:{}", deleteFunName);
        String updateFunName = MapperUtils.updateFunName(dbDONameTL.get());
        log.info("updateFunName:{}", updateFunName);
        String selectFunName = MapperUtils.selectFunName(dbDONameTL.get(), primaryTL.get());
        log.info("selectFunName:{}", selectFunName);
    }

    /**
     * 生成Update语句
     * @return
     */
    private SQL packUpdate() {
        SQL sql = new SQL();
        sql.UPDATE(tableTL.get());
        schemasTL.get().forEach(e-> sql.SET(FLAG1+ e.getColumn() +FLAG1 +SPACE +EQUAL +SPACE +FLAG5+ e.getProperty() +FLAG6));
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
            sql.INTO_COLUMNS(FLAG1+ e.getColumn() +FLAG1);
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
        schemasTL.get().forEach(e-> sql.append(FLAG1).append(e.getColumn()).append(FLAG1).append(COMMA).append(SPACE));
        return sql.substring(0, sql.length()-2);
    }

    /**
     * 生成带alias的全量字段字符串
     * @return
     */
    private String packAliasColumns(String alias){
        String alias2 = null==alias?Arrays.stream(tableTL.get().split(FLAG4)).map(e-> String.valueOf(e.charAt(0))).collect(Collectors.joining()):alias;
        StringBuffer sql = new StringBuffer();
        schemasTL.get().forEach(e-> sql.append(alias2).append(FLAG2).append(FLAG1).append(e.getColumn()).append(FLAG1).append(COMMA).append(SPACE));
        return sql.substring(0, sql.length()-2);
    }

    private String packPrimaryWhereClause(){
        return primaryTL.get().getColumn() +SPACE +EQUAL +SPACE +FLAG5 +primaryTL.get().getProperty()+ FLAG6;
    }
}
