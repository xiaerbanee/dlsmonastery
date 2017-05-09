package net.myspring.cloud.modules.sys.web.query;

import net.myspring.cloud.common.query.BaseQuery;

import java.util.List;

/**
 * Created by lihx on 2017/4/12.
 */
public class KingdeeBookQuery extends BaseQuery {
    private String companyId;
    private String name;
    private String type;

    private List<String> nameList;
    private List<String> typeList;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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

    public List<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }

    public List<String> getNameList() {
        return nameList;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }
}
