package net.myspring.basic.modules.sys.domain;

import net.myspring.basic.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by zhucc on 2017/4/17.
 */
@Entity
@Table(name="sys_company_config")
public class CompanyConfig extends CompanyEntity<CompanyConfig>{
    private String name;
    private String code;
    private String value;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
