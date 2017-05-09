package net.myspring.basic.modules.sys.web.query;

import net.myspring.basic.common.query.BaseQuery;

/**
 * Created by zhucc on 2017/4/17.
 */
public class CompanyConfigQuery extends BaseQuery {
    private String name;
    private String code;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
