package net.myspring.mybatis.provider;

import net.myspring.mybatis.context.MapperThreadLocal;
import net.myspring.mybatis.context.ProviderContextUtils;
import net.myspring.mybatis.dto.ColumnDto;
import net.myspring.mybatis.dto.TableDto;

import javax.persistence.Column;
import javax.persistence.GenerationType;
import java.io.Serializable;

/**
 * Created by liuj on 2016/11/12.
 */
public class BaseProvider {

    protected Class<? extends Serializable> getIdClass() {
        return MapperThreadLocal.get().getMapperDefinition().getIdClass();
    }

    protected Class<?> getDomainClass() {
        return MapperThreadLocal.get().getMapperDefinition().getDomainClass();
    }

    protected TableDto getTableDto() {
        return ProviderContextUtils.getTableDto(getDomainClass());
    }

    protected Boolean getInsertable(Object entity, ColumnDto columnDto) {
        boolean insertable = columnDto.getInsertable();
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
        return insertable;
    }

    protected Boolean getUpdatable(Object entity, ColumnDto columnDto) {
        boolean updatable = columnDto.getUpdatable();
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
        return updatable;
    }
}
