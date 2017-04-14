package net.myspring.basic.modules.hr.web.form;


import net.myspring.basic.modules.hr.domain.Office;
import net.myspring.basic.common.form.DataForm;

/**
 * Created by admin on 2017/4/6.
 */

public class OfficeForm extends DataForm<Office> {

    private String parentId;
    private String name;
    private String type;
    private String joinType;
    private String point;
    private String taskPoint;
    private String sort;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public String getJoinType() {
        return joinType;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getTaskPoint() {
        return taskPoint;
    }

    public void setTaskPoint(String taskPoint) {
        this.taskPoint = taskPoint;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
