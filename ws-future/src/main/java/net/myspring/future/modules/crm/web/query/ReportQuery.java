package net.myspring.future.modules.crm.web.query;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by wangzm on 2017/6/7.
 */
public class ReportQuery extends BaseQuery{
    //销售，库存
    private String type;
    //区域，促销，型号
    private String sumType ;
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
    private String officeId;
    private String depotId;
    private Boolean isDetail=false;
    //需要导出的office的下级
    private List<String> officeIds=Lists.newArrayList();
    private String exportType;
    private List<String> productTypeIdList;

    public List<String> getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(List<String> officeIds) {
        this.officeIds = officeIds;
    }

    public String getExportType() {
        return exportType;
    }

    public void setExportType(String exportType) {
        this.exportType = exportType;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public Boolean getIsDetail() {
        return isDetail;
    }

    public void setIsDetail(Boolean detail) {
        isDetail = detail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getOfficeId() {
        if(CollectionUtil.isNotEmpty(officeIds)){
            this.officeId=null;
        }
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getSumType() {
        return sumType;
    }

    public void setSumType(String sumType) {
        this.sumType = sumType;
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
        if(StringUtils.isBlank(dateRange)){
            this.dateRange=LocalDateUtils.format(LocalDateUtils.getFirstDayOfThisMonth(LocalDate.now()))+CharConstant.DATE_RANGE_SPLITTER+LocalDateUtils.format(LocalDate.now());
        }
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
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

    public LocalDate getDateStart() {
        if(StringUtils.isNotBlank(getDateRange())) {
            return LocalDateUtils.parse(getDateRange().split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getDateEnd() {
        if(StringUtils.isNotBlank(getDateRange())) {
            return LocalDateUtils.parse(getDateRange().split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

    public LocalDate getDate() {
        if(date==null){
            this.date=LocalDate.now();
        }else {
            this.date=date.plusDays(1);
        }
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


}
