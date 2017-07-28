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
    private BigDecimal taskPoint;
    private String newParentName;
    private String newName;
    private BigDecimal newTaskPoint;

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

    public BigDecimal getTaskPoint() {
        return taskPoint;
    }

    public void setTaskPoint(BigDecimal taskPoint) {
        this.taskPoint = taskPoint;
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

    public BigDecimal getNewTaskPoint() {
        if(taskPoint!=null&&newTaskPoint!=null){
            this.newTaskPoint=newTaskPoint;
        }
        return newTaskPoint;
    }

    public void setNewTaskPoint(BigDecimal newTaskPoint) {
        this.newTaskPoint = newTaskPoint;
    }
}
