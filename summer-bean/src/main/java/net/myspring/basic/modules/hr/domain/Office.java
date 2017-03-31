package net.myspring.basic.modules.hr.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.TreeEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="hr_office")
public class Office extends TreeEntity<Office> {
    private String name;
    private Integer version = 0;
    private String type;
    private BigDecimal point;
    private String dingId;
    private Integer level;
    private String jointType;
    private String tag;
    private BigDecimal taskPoint;
    private Integer sort;
    private List<Account> accountList = Lists.newArrayList();
    private List<String> accountIdList = Lists.newArrayList();
    private List<OfficeChange> officeChangeList = Lists.newArrayList();
    private List<String> officeChangeIdList = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPoint() {
        return point;
    }

    public void setPoint(BigDecimal point) {
        this.point = point;
    }

    public String getDingId() {
        return dingId;
    }

    public void setDingId(String dingId) {
        this.dingId = dingId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getJointType() {
        return jointType;
    }

    public void setJointType(String jointType) {
        this.jointType = jointType;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public BigDecimal getTaskPoint() {
        return taskPoint;
    }

    public void setTaskPoint(BigDecimal taskPoint) {
        this.taskPoint = taskPoint;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public List<String> getAccountIdList() {
        return accountIdList;
    }

    public void setAccountIdList(List<String> accountIdList) {
        this.accountIdList = accountIdList;
    }

    public List<OfficeChange> getOfficeChangeList() {
        return officeChangeList;
    }

    public void setOfficeChangeList(List<OfficeChange> officeChangeList) {
        this.officeChangeList = officeChangeList;
    }

    public List<String> getOfficeChangeIdList() {
        return officeChangeIdList;
    }

    public void setOfficeChangeIdList(List<String> officeChangeIdList) {
        this.officeChangeIdList = officeChangeIdList;
    }
}
