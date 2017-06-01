package net.myspring.future.modules.basic.web.query;

import com.google.common.collect.Maps;
import net.myspring.future.common.query.BaseQuery;

import java.util.Map;

/**
 * Created by lihx on 2017/4/19.
 */
public class ProductTypeQuery extends BaseQuery {

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
