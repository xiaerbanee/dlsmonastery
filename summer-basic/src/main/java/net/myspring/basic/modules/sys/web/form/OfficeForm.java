package net.myspring.basic.modules.sys.web.form;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.basic.common.form.DataForm;
import net.myspring.util.cahe.annotation.CacheInput;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/6.
 */

public class OfficeForm extends DataForm<Office> {

    private String parentId;
    private String name;
    private String type;
    private String jointType;
    private String point;
    private String taskPoint;
    private String sort;
    private Map<String,String>  officeTypeList= Maps.newHashMap();
    private List<String> jointTypeList= Lists.newArrayList();

    @CacheInput(inputKey = "offices",inputInstance = "parentId",outputInstance = "name")
    private String parentName;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Map<String, String> getOfficeTypeList() {
        return officeTypeList;
    }

    public void setOfficeTypeList(Map<String, String> officeTypeList) {
        this.officeTypeList = officeTypeList;
    }

    public List<String> getJointTypeList() {
        return jointTypeList;
    }

    public void setJointTypeList(List<String> jointTypeList) {
        this.jointTypeList = jointTypeList;
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

    public String getJointType() {
        return jointType;
    }

    public void setJointType(String jointType) {
        this.jointType = jointType;
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
