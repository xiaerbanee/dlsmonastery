package net.myspring.tool.common.domain;


import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class OfficeEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected String id;
    private String name;
    private BigDecimal point;
    private Integer level;
    private String jointType;
    private BigDecimal taskPoint;
    private String sort;
    private String officeRuleId;
    private String type;
    private String areaId;
    private String jointLevel;
    private String parentId;
    private String parentIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public BigDecimal getPoint() {
        return point;
    }

    public void setPoint(BigDecimal point) {
        this.point = point;
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

    public BigDecimal getTaskPoint() {
        return taskPoint;
    }

    public void setTaskPoint(BigDecimal taskPoint) {
        this.taskPoint = taskPoint;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOfficeRuleId() {
        return officeRuleId;
    }

    public void setOfficeRuleId(String officeRuleId) {
        this.officeRuleId = officeRuleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getJointLevel() {
        return jointLevel;
    }

    public void setJointLevel(String jointLevel) {
        this.jointLevel = jointLevel;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }
}
