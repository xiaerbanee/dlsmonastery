package net.myspring.basic.modules.hr.web.form;

import com.google.common.collect.Lists;

import java.util.List;

public class AccountPositionForm {
    private String id;
    private List<String> positionIdList= Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPositionIdList() {
        return positionIdList;
    }

    public void setPositionIdList(List<String> positionIdList) {
        this.positionIdList = positionIdList;
    }
}
