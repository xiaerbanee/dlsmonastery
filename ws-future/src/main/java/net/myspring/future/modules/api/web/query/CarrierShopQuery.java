package net.myspring.future.modules.api.web.query;

import com.google.common.collect.Lists;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.future.modules.api.domain.CarrierShop;

import java.util.List;

/**
 * Created by wangzm on 2017/7/5.
 */
public class CarrierShopQuery extends BaseQuery {
    private String name;
    private String code;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
