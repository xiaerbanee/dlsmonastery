package net.myspring.cloud.modules.kingdee.domain;

import java.math.BigDecimal;

/**
 * 总盘报表
 * Created by lihx on 2016/11/30.
 */
public class TotalDiscReport {
    //部门名称
    private String departmentName;
    //年
    private Integer year;
    //月份
    private Integer period;
    //项目
    private String name;
    //项目编码
    private String code;
    //金额
    private BigDecimal amount;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
