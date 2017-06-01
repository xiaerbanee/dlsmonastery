package net.myspring.future.modules.crm.web.query;

import net.myspring.future.common.query.BaseQuery;

import java.time.LocalDate;
import java.util.List;


public class ProductImeStockReportQuery extends BaseQuery{

    private String sumType;
    private String outType;
    private String areaType;
    private String townType;
    private LocalDate date;
    private Boolean scoreType;
    private List<String> productIds;

    private List<String> sumTypeList;
    private List<String> outTypeList;
    private List<String> areaTypeList;
    private List<String> townTypeList;

    public String getSumType() {

        return sumType;
    }

    public void setSumType(String sumType) {
        this.sumType = sumType;
    }

    public List<String> getSumTypeList() {
        return sumTypeList;
    }

    public void setSumTypeList(List<String> sumTypeList) {
        this.sumTypeList = sumTypeList;
    }

    public String getOutType() {
        return outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    public List<String> getOutTypeList() {
        return outTypeList;
    }

    public void setOutTypeList(List<String> outTypeList) {
        this.outTypeList = outTypeList;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public List<String> getAreaTypeList() {
        return areaTypeList;
    }

    public void setAreaTypeList(List<String> areaTypeList) {
        this.areaTypeList = areaTypeList;
    }

    public String getTownType() {
        return townType;
    }

    public void setTownType(String townType) {
        this.townType = townType;
    }

    public List<String> getTownTypeList() {
        return townTypeList;
    }

    public void setTownTypeList(List<String> townTypeList) {
        this.townTypeList = townTypeList;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getScoreType() {
        return scoreType;
    }

    public void setScoreType(Boolean scoreType) {
        this.scoreType = scoreType;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }
}
