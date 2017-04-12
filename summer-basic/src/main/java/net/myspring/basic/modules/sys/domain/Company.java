package net.myspring.basic.modules.sys.domain;

import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangzm on 2017/4/12.
 */

@Entity
@Table(name="sys_company")
public class Company extends DataEntity<Company>{
    private String name;
    private String code;
    private String outDbId;
    private String outDbName;
    private String brand;

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

    public String getOutDbId() {
        return outDbId;
    }

    public void setOutDbId(String outDbId) {
        this.outDbId = outDbId;
    }

    public String getOutDbName() {
        return outDbName;
    }

    public void setOutDbName(String outDbName) {
        this.outDbName = outDbName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
