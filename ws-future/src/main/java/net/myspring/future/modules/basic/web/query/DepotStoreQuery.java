package net.myspring.future.modules.basic.web.query;

import net.myspring.future.common.query.BaseQuery;

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
    private String areaId;
    private String jointLevel;
    private String contator;
    private String mobilePhone;



    public String getJointLevel() {
        return jointLevel;
    }

    public void setJointLevel(String jointLevel) {
        this.jointLevel = jointLevel;
    }

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


    public String getContator() {
        return contator;
    }

    public void setContator(String contator) {
        this.contator = contator;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaId() {
        return areaId;
    }
}
