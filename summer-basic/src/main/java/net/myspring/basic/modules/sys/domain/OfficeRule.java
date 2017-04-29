package net.myspring.basic.modules.sys.domain;

import net.myspring.common.domain.TreeEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangzm on 2017/4/22.
 */
@Entity
@Table(name="sys_office_rule")
public class OfficeRule extends TreeEntity<OfficeRule> {
    private String name;
    private String code;
    private Boolean hasPoint;
    private String type;
    private Integer level;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public boolean getHasPoint() {
        return hasPoint;
    }

    public void setHasPoint(boolean hasPoint) {
        this.hasPoint = hasPoint;
    }
}
