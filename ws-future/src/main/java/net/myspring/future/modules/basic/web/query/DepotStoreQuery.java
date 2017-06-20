package net.myspring.future.modules.basic.web.query;

import net.myspring.future.common.query.BaseQuery;

import java.util.List;

/**
 * Created by Lenovo on 2017/5/17.
 */
public class DepotStoreQuery extends BaseQuery {

    private String name;
    private String outType;
    private String type;
    private Boolean ScoreType;
    private String sumType;
    private String officeId;
    private List<String> productTypeIdList;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOutType() {
        return outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getScoreType() {
        return ScoreType;
    }

    public void setScoreType(Boolean scoreType) {
        ScoreType = scoreType;
    }

    public String getSumType() {
        return sumType;
    }

    public void setSumType(String sumType) {
        this.sumType = sumType;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public List<String> getProductTypeIdList() {
        return productTypeIdList;
    }

    public void setProductTypeIdList(List<String> productTypeIdList) {
        this.productTypeIdList = productTypeIdList;
    }
}
