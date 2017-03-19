package net.myspring.mybatis.context;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.mybatis.dto.ColumnDto;
import net.myspring.mybatis.dto.TableDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.*;

import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/3/17.
 */
public class ProviderContextUtils {
    //表结构缓存
    private static Map<String,TableDto> tableDtoMap = Maps.newHashMap();

    public static TableDto getTableDto(Class clazz) {
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
                List<Field> fields = getFields(null, clazz);
                for (Field field : fields) {
                    if (isJdbcColumn(field)) {
                        ColumnDto columnDto = getColumnDto(field);
                        //检查是否是ID
                        if (field.getAnnotation(Id.class) != null) {
                            columnDto.setGeneratedValue(field.getAnnotation(GeneratedValue.class));
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
        }else if (field.getAnnotation(Transient.class) != null || field.getAnnotation(Entity.class) != null) {
            isJdbcColumn = false;
        } else if (Collection.class.isAssignableFrom(fieldClass) || Map.class.isAssignableFrom(fieldClass)) {
            isJdbcColumn = false;
        }
        return isJdbcColumn;
    }

    private static ColumnDto getColumnDto(Field field) {
        ColumnDto columnDto = new ColumnDto();
        columnDto.setJavaInstance(field.getName());
        String jdbcColumn = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,field.getName());
        boolean insertable = true;
        boolean updatable = true;
        boolean nullable = false;
        Column column = field.getAnnotation(Column.class);
        if(column !=null) {
            if(!StringUtils.isEmpty(column.name())) {
                jdbcColumn = column.name();
            }
            insertable = column.insertable();
            updatable = column.updatable();
            nullable = column.nullable();
        }
        columnDto.setJdbcColumn(jdbcColumn);
        columnDto.setInsertable(insertable);
        columnDto.setUpdatable(updatable);
        columnDto.setNullable(nullable);
        return columnDto;
    }


    //递归获取所有Field
    private static List<Field> getFields(List<Field> fields,Class clazz) {
        if(fields == null) {
            fields= Lists.newArrayList();
        }
        for(Field field:clazz.getDeclaredFields()) {
            fields.add(field);
        }
        while (!clazz.getName().equals(Object.class.getName())) {
            getFields(fields,clazz.getSuperclass());
        }
        return fields;
    }
}
