package net.myspring.basic.modules.hr.dto;

import net.myspring.basic.modules.hr.domain.OfficeChange;
import net.myspring.common.dto.DataDto;
import net.myspring.util.text.StringUtils;

import java.math.BigDecimal;

/**
 * Created by wangzm on 2017/7/20.
 */
public class OfficeChangeFormDto extends DataDto<OfficeChange>{
    private String id;
    private String parentName;
    private String name;
    private String type;
    private BigDecimal point;
    private String newParentName;
    private String newName;
    private BigDecimal newPoint;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNewParentName() {
        if(StringUtils.isNotBlank(parentName)&&StringUtils.isBlank(newParentName)){
            this.newParentName=parentName;
        }
        return newParentName;
    }

    public void setNewParentName(String newParentName) {
        this.newParentName = newParentName;
    }

    public String getNewName() {
        if(StringUtils.isNotBlank(name)&&StringUtils.isBlank(newName)){
            this.newName=name;
        }
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public BigDecimal getPoint() {
        return point;
    }

    public void setPoint(BigDecimal point) {
        this.point = point;
    }

    public BigDecimal getNewPoint() {
        if(point!=null&&newPoint!=null){
            this.newPoint=newPoint;
        }
        return newPoint;
    }

    public void setNewPoint(BigDecimal newPoint) {
        this.newPoint = newPoint;
    }
}
