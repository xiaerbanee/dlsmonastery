package net.myspring.basic.modules.hr.web.form;


import net.myspring.basic.common.enums.JointTypeEnum;
import net.myspring.basic.modules.hr.domain.Office;
import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.sys.domain.DictMap;

import java.util.List;

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
    private List<DictMap> officeTypes;
    private JointTypeEnum[] jointTypes;

    public List<DictMap> getOfficeTypes() {
        return officeTypes;
    }

    public void setOfficeTypes(List<DictMap> officeTypes) {
        this.officeTypes = officeTypes;
    }

    public JointTypeEnum[] getJointTypes() {
        return jointTypes;
    }

    public void setJointTypes(JointTypeEnum[] jointTypes) {
        this.jointTypes = jointTypes;
    }

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
