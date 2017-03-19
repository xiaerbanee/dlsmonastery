package net.myspring.mybatis.provider;

import net.myspring.mybatis.context.MapperThreadLocal;
import net.myspring.mybatis.context.ProviderContextUtils;
import net.myspring.mybatis.dto.ColumnDto;
import net.myspring.mybatis.dto.TableDto;

import javax.persistence.Column;
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
        return true;
    }

    protected Boolean getUpdatable(Object entity, ColumnDto columnDto) {
        return true;
    }
}
