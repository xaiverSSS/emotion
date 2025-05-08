package com.xaiver.emotion.service;

import com.xaiver.emotion.dao.SchemaMapper;
import com.xaiver.emotion.enums.AccessModifierEnum;
import com.xaiver.emotion.enums.SQLTypeEnum;
import com.xaiver.emotion.model.EntitySchema;
import com.xaiver.emotion.model.MapperSchema;
import com.xaiver.emotion.model.TableSchema;
import com.xaiver.emotion.utils.CamelCaseUtils;
import com.xaiver.emotion.utils.ClassWordUtils;
import com.xaiver.emotion.utils.EntityUtils;
import com.xaiver.emotion.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import static com.xaiver.emotion.constants.CommonConstants.*;
import static com.xaiver.emotion.constants.SchemaConstants.*;

@Slf4j
@Service
public class SchemaService {
    @Resource
    private SchemaMapper schemaMapper;

    private ThreadLocal<String> databaseTL = new ThreadLocal<>();
    private ThreadLocal<String> tableTL = new ThreadLocal<>();
    private ThreadLocal<TableSchema> primaryTL = new ThreadLocal<>();
    private ThreadLocal<List<TableSchema>> schemasTL = new ThreadLocal<>();
    public void generate(String database, String table) throws ClassNotFoundException {
        try {
            databaseTL.set(database);
            tableTL.set(table);
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

            EntitySchema entitySchema = mkDBEntity();
            Assert.notNull(entitySchema, "Entity Not Found");
            MapperSchema mapperSchema = mkDBMapper(entitySchema);
        }finally {
            databaseTL.remove();
            tableTL.remove();
            primaryTL.remove();
            schemasTL.remove();
        }
    }

    /**
     * 生成DO Entity
     * 类权限修饰符 Public
     * 类名 根据表明生成驼峰形式
     * 属性 字段序列顺序
     * 属性权限修饰符 Private
     * 属性类型 字段类型映射Java Class
     *
     * @throws ClassNotFoundException
     */
    private EntitySchema mkDBEntity() throws ClassNotFoundException {
        EntitySchema entitySchema = new EntitySchema();
        entitySchema.setAccessModifier(AccessModifierEnum.PUBLIC);
        entitySchema.setName(EntityUtils.entityName(tableTL.get()));
        List<EntitySchema.PropSchema> props = new LinkedList<>();
        for (TableSchema e : schemasTL.get()) {
            EntitySchema.PropSchema prop = new EntitySchema.PropSchema();
            prop.setAccessModifier(AccessModifierEnum.PRIVATE);
            prop.setJavaClass(ClassWordUtils.getJavaClass(e));
            prop.setName(e.getProperty());
            props.add(prop);
        }
        entitySchema.setProps(props);
        log.info("entitySchema:{}", entitySchema);
        EntityUtils.write(entitySchema);
        return entitySchema;
    }

    /**
     * 生成Mapper Interface
     * @throws ClassNotFoundException
     */
    private MapperSchema mkDBMapper(EntitySchema entitySchema) throws ClassNotFoundException {
        MapperSchema mapperSchema = new MapperSchema();
        mapperSchema.setName(MapperUtils.interfaceName(tableTL.get()));
        mapperSchema.setAccessModifier(AccessModifierEnum.PUBLIC);

        List<String> columns = packColumns();
        mapperSchema.setColumns(columns);

        List<String> aliasColumns = packAliasColumns(null);
        mapperSchema.setAliasColumns(aliasColumns);

        MapperSchema.MethodSchema insertSchema = new MapperSchema.MethodSchema();
        insertSchema.setSql(packInsert());
        insertSchema.setSqlType(SQLTypeEnum.INSERT);
        insertSchema.setName(MapperUtils.insertFunName(entitySchema));
        insertSchema.setJavaClass(Void.class);
        mapperSchema.getMethods().add(insertSchema);

        MapperSchema.MethodSchema deleteSchema = new MapperSchema.MethodSchema();
        deleteSchema.setSql(packDelete());
        deleteSchema.setSqlType(SQLTypeEnum.DELETE);
        deleteSchema.setName(MapperUtils.deleteFunName(primaryTL.get()));
        deleteSchema.setJavaClass(Void.class);
        mapperSchema.getMethods().add(deleteSchema);

        MapperSchema.MethodSchema selectSchema = new MapperSchema.MethodSchema();
        selectSchema.setSql(packSelect());
        selectSchema.setSqlType(SQLTypeEnum.SELECT);
        selectSchema.setName(MapperUtils.selectFunName(entitySchema, primaryTL.get()));
        selectSchema.setJavaClass(Void.class);
        mapperSchema.getMethods().add(selectSchema);

        MapperSchema.MethodSchema updateSchema = new MapperSchema.MethodSchema();
        updateSchema.setSql(packSelect());
        updateSchema.setSqlType(SQLTypeEnum.UPDATE);
        updateSchema.setName(MapperUtils.updateFunName(entitySchema));
        updateSchema.setJavaClass(Void.class);
        mapperSchema.getMethods().add(updateSchema);
        log.info("mapperSchema:{}", mapperSchema);
        return mapperSchema;
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
    private List<String> packColumns(){
        List<String> columns = new LinkedList<>();
        schemasTL.get().forEach(e-> columns.add(FLAG1+ e.getColumn()+ FLAG1));
        return columns;
    }

    /**
     * 生成带alias的全量字段字符串
     * @return
     */
    private List<String> packAliasColumns(String alias){
        alias = null==alias?Arrays.stream(tableTL.get().split(FLAG4)).map(e-> String.valueOf(e.charAt(0))).collect(Collectors.joining()):alias;
        List<String> columns = new LinkedList<>();
        for (TableSchema e : schemasTL.get()) {
            columns.add(alias + FLAG2 + FLAG1 + e.getColumn() + FLAG1);
        }
        return columns;
    }

    private String packPrimaryWhereClause(){
        return primaryTL.get().getColumn() +SPACE +EQUAL +SPACE +FLAG5 +primaryTL.get().getProperty()+ FLAG6;
    }
}
