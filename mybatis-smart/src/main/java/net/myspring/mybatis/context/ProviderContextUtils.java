package net.myspring.mybatis.context;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.mybatis.dto.ColumnDto;
import net.myspring.mybatis.dto.TableDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by liuj on 2017/3/17.
 */
public class ProviderContextUtils {
    //表结构缓存
    private static Map<String,TableDto> tableDtoMap = Maps.newHashMap();
    //字段缓存
    private static Map<String,ColumnDto> columnDtoMap = Maps.newHashMap();
    //class对应的entityClass
    private static Map<String,Class> entityClassMap= Maps.newHashMap();

    public static TableDto getTableDto(Class clazz) {
        clazz = getEntityClass(clazz);
        String key = clazz.getName();
        if(!tableDtoMap.containsKey(key)) {
            Entity entity = (Entity) clazz.getAnnotation(Entity.class);
            Table table = (Table) clazz.getAnnotation(Table.class);
            if(entity != null && table != null) {
                TableDto tableDto = new TableDto();
                //获取表名
                String jdbcTable = table.name();
                if (StringUtils.isBlank(jdbcTable)) {
                    jdbcTable = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, clazz.getSimpleName());
                }
                tableDto.setJdbcTable(jdbcTable);
                List<Field> fields=Lists.newArrayList();
                 getFields(fields, clazz);
                for (Field field : fields) {
                    if (isJdbcColumn(field)) {
                        ColumnDto columnDto = getColumnDto(field);
                        //检查是否是ID
                        if (field.getAnnotation(Id.class) != null) {
                            tableDto.setIdColumn(columnDto);
                        }
                        if (field.getAnnotation(CreatedBy.class) != null) {
                            tableDto.setCreatedByColumn(columnDto);
                        }
                        if (field.getAnnotation(CreatedDate.class) != null) {
                            tableDto.setCreatedDateColumn(columnDto);
                        }
                        if (field.getAnnotation(LastModifiedBy.class) != null) {
                            tableDto.setLastModifiedByColumn(columnDto);
                        }
                        if (field.getAnnotation(LastModifiedDate.class) != null) {
                            tableDto.setLastModifiedDateColumn(columnDto);
                        }
                        if (field.getAnnotation(Version.class) != null) {
                            tableDto.setVersionColumn(columnDto);
                        }
                        columnDto.setTableDto(tableDto);
                        tableDto.getColumnList().add(columnDto);
                    }
                }
                tableDtoMap.put(key, tableDto);
            }
        }
        return tableDtoMap.get(key);
    }

    private static Boolean isJdbcColumn(Field field) {
        Class fieldClass = field.getType();
        Boolean isJdbcColumn = true;
        if("serialVersionUID".equals(field.getName())) {
            isJdbcColumn = false;
        }else if (field.getAnnotation(Transient.class) != null || fieldClass.getAnnotation(Entity.class) != null) {
            isJdbcColumn = false;
        } else if (Collection.class.isAssignableFrom(fieldClass) || Map.class.isAssignableFrom(fieldClass)) {
            isJdbcColumn = false;
        }
        return isJdbcColumn;
    }

    public static MybatisContext getMybatisContext(Properties properties) {
        MybatisContext mybatisContext = null;
        try {
            mybatisContext  =  (MybatisContext) Class.forName(properties.getProperty("mybatisContext")).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return mybatisContext;
    }

    public static Class getEntityClass(Class clazz) {
        String key = clazz.getName();
        if(!entityClassMap.containsKey(key)) {
            Entity entity = null;
            while (!clazz.getName().equals(Object.class.getName()) && entity==null) {
                entity = (Entity) clazz.getAnnotation(Entity.class);
                if(entity != null) {
                    entityClassMap.put(key,clazz);
                }
                clazz = clazz.getSuperclass();
            }
            if(!entityClassMap.containsKey(key)) {
                entityClassMap.put(key,null);
            }
        }
        return entityClassMap.get(key);
    }


    private static ColumnDto getColumnDto(Field field) {
        String key = field.toString();
        if(!columnDtoMap.containsKey(key)) {
            ColumnDto columnDto = new ColumnDto();
            columnDto.setJavaInstance(field.getName());
            String jdbcColumn = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,field.getName());
            boolean insertable = true;
            boolean updatable = true;
            boolean nullable = false;
            Column column = field.getAnnotation(Column.class);
            if(column !=null) {
                if(StringUtils.isNotBlank(column.name())) {
                    jdbcColumn = column.name();
                }
                insertable = column.insertable();
                updatable = column.updatable();
                nullable = column.nullable();
            }
            if (field.getAnnotation(Id.class) != null) {
                columnDto.setGeneratedValue(field.getAnnotation(GeneratedValue.class));
            }
            columnDto.setJdbcColumn(jdbcColumn);
            columnDto.setInsertable(insertable);
            columnDto.setUpdatable(updatable);
            columnDto.setNullable(nullable);
            columnDtoMap.put(key,columnDto);
        }
        return columnDtoMap.get(key);
    }

    //递归获取所有Field
    private static void getFields(List<Field> fields,Class clazz) {
        if(fields == null) {
            fields= Lists.newArrayList();
        }
        for(Field field:clazz.getDeclaredFields()) {
            fields.add(field);
        }
        clazz = clazz.getSuperclass();
        if (!clazz.getName().equals(Object.class.getName())) {
            getFields(fields,clazz);
        }
    }
}
