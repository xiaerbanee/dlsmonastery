package net.myspring.mybatis.provider;

import java.io.Serializable;

/**
 * Created by liuj on 2016/11/12.
 */
public class BaseProvider {

    protected Class<? extends Serializable> getIdClass() {
        return null;
    }

    protected Class<?> getDomainClass() {
        return null;
    }
}
