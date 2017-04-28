package net.myspring.cloud.modules.report.domain;

import net.myspring.cloud.modules.sys.domain.DynamicSubject;

import java.math.BigDecimal;

/**
 * Created by lihx on 2017/2/8.
 */
public class Retail extends DynamicSubject {
    private Integer year;
    private Integer month;
    private String deptNum;
    private String deptName;
    private BigDecimal amount;
    private BigDecimal percent;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    public String getDeptNum() {
        return deptNum;
    }

    public void setDeptNum(String deptNum) {
        this.deptNum = deptNum;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
