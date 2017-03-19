package net.myspring.mybatis.provider;

import com.google.common.collect.Maps;
import net.myspring.mybatis.context.MapperThreadLocal;
import net.myspring.mybatis.context.ProviderContextUtils;
import net.myspring.mybatis.dto.ColumnDto;
import net.myspring.mybatis.dto.TableDto;

import javax.persistence.Column;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by liuj on 2016/11/12.
 */
public class BaseProvider {
    private Map<String,Boolean> insertableMap = Maps.newHashMap();

    private Map<String,Boolean> updatableMap = Maps.newHashMap();


    protected Class<? extends Serializable> getIdClass() {
        return MapperThreadLocal.get().getMapperDefinition().getIdClass();
    }

    protected Class<?> getDomainClass() {
        return MapperThreadLocal.get().getMapperDefinition().getDomainClass();
    }

    protected TableDto getTableDto() {
        return ProviderContextUtils.getTableDto(getDomainClass());
    }

    protected Boolean getInsertable(Object entity, ColumnDto columnDto) throws NoSuchFieldException {
        String key = entity.getClass().getName() + "_" + columnDto.getJdbcColumn();
        if(!insertableMap.containsKey(key)) {
            boolean insertable = columnDto.getInsertable();
            Field field = entity.getClass().getDeclaredField(columnDto.getJavaInstance());
            if(field == null) {
                insertable = false;
            } else {
                if(getTableDto().getIdColumn() != null && getTableDto().getIdColumn().getJdbcColumn().equals(columnDto.getJdbcColumn())) {
                    if (GenerationType.IDENTITY.name().equals(columnDto.getGeneratedValue().strategy().name())) {
                        insertable = false;
                    } else {
                        insertable = true;
                    }
                } else if(getTableDto().getVersionColumn() != null && getTableDto().getVersionColumn().getJdbcColumn().equals(columnDto.getJdbcColumn())) {
                    insertable = true;
                } else if(getTableDto().getCreatedByColumn() != null && getTableDto().getCreatedByColumn().getJdbcColumn().equals(columnDto.getJdbcColumn())) {
                    insertable = true;
                }else if(getTableDto().getCreatedDateColumn() != null && getTableDto().getCreatedDateColumn().getJdbcColumn().equals(columnDto.getJdbcColumn())) {
                    insertable = true;
                } else if(getTableDto().getLastModifiedByColumn() != null && getTableDto().getLastModifiedByColumn().getJdbcColumn().equals(columnDto.getJdbcColumn())) {
                    insertable = true;
                }else if(getTableDto().getLastModifiedDateColumn() != null && getTableDto().getLastModifiedDateColumn().getJdbcColumn().equals(columnDto.getJdbcColumn())) {
                    insertable = true;
                }
            }
            insertableMap.put(key,insertable);
        }
        return insertableMap.get(key);
    }

    protected Boolean getUpdatable(Object entity, ColumnDto columnDto) {
        String key = entity.getClass().getName() + "_" + columnDto.getJdbcColumn();
        boolean updatable = columnDto.getUpdatable();
        if(!updatableMap.containsKey(key)) {
            if(getTableDto().getIdColumn() != null && getTableDto().getIdColumn().getJdbcColumn().equals(columnDto.getJdbcColumn())) {
                updatable = false;
            } else if(getTableDto().getVersionColumn() != null && getTableDto().getVersionColumn().getJdbcColumn().equals(columnDto.getJdbcColumn())) {
                updatable = false;
            } else if(getTableDto().getCreatedByColumn() != null && getTableDto().getCreatedByColumn().getJdbcColumn().equals(columnDto.getJdbcColumn())) {
                updatable = false;
            }else if(getTableDto().getCreatedDateColumn() != null && getTableDto().getCreatedDateColumn().getJdbcColumn().equals(columnDto.getJdbcColumn())) {
                updatable = false;
            } else if(getTableDto().getLastModifiedByColumn() != null && getTableDto().getLastModifiedByColumn().getJdbcColumn().equals(columnDto.getJdbcColumn())) {
                updatable = true;
            }else if(getTableDto().getLastModifiedDateColumn() != null && getTableDto().getLastModifiedDateColumn().getJdbcColumn().equals(columnDto.getJdbcColumn())) {
                updatable = true;
            }
            updatableMap.put(key,updatable);
        }
        return updatableMap.get(key);
    }
}
