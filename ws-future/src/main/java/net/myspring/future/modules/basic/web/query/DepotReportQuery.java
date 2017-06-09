package net.myspring.future.modules.basic.web.query;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.List;

public class DepotReportQuery {
    private boolean detail;
    private String type;
    //电子保卡，核销
    private String outType;
    //区域类型
    private String areaType;
    //日期范围
    private String dateRange;
    private LocalDate date;
    //乡镇类型
    private String townType;
    //打分型号：是，否
    private boolean scoreType;
    //货品
    private String depotId;
    private List<String> productTypeIdList= Lists.newArrayList();
    private String officeId;
    private List<String> officeIdList=Lists.newArrayList();
    private List<String> depotIdList=Lists.newArrayList();
    private List<String> shopIdList=Lists.newArrayList();

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public boolean getDetail() {
        return detail;
    }

    public void setDetail(boolean detail) {
        this.detail = detail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getShopIdList() {
        return shopIdList;
    }

    public void setShopIdList(List<String> shopIdList) {
        this.shopIdList = shopIdList;
    }

    public String getOutType() {
        return outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTownType() {
        return townType;
    }

    public void setTownType(String townType) {
        this.townType = townType;
    }

    public boolean getScoreType() {
        return scoreType;
    }

    public void setScoreType(boolean scoreType) {
        this.scoreType = scoreType;
    }

    public List<String> getProductTypeIdList() {
        return productTypeIdList;
    }

    public void setProductTypeIdList(List<String> productTypeIdList) {
        this.productTypeIdList = productTypeIdList;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public List<String> getOfficeIdList() {
        return officeIdList;
    }

    public void setOfficeIdList(List<String> officeIdList) {
        this.officeIdList = officeIdList;
    }

    public LocalDate getDateStart() {
        if(StringUtils.isNotBlank(getDateRange())) {
            return LocalDateUtils.parse(getDateRange().split(CharConstant.WAVE_LINE)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getDateEnd() {
        if(StringUtils.isNotBlank(getDateRange())) {
            return LocalDateUtils.parse(getDateRange().split(CharConstant.WAVE_LINE)[1]).plusDays(1);
        } else {
            return null;
        }
    }
}
