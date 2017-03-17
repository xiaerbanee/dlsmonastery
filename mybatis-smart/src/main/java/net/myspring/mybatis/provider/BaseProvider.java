package net.myspring.mybatis.provider;

import net.myspring.mybatis.dto.TableDto;

/**
 * Created by liuj on 2016/11/12.
 */
public class BaseProvider {

    protected TableDto getTableDto() {
        return null;
    }

    protected Class getEntityClass() {
        return null;
    }
}
