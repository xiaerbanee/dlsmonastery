package net.myspring.future.modules.crm.web.query;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.List;

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

    private LocalDate dateStart;

    private LocalDate dateEnd;

    private LocalDate date;
    //网络制式
    private String netType;
    //乡镇类型
    private String townType;
    //打分型号：是，否
    private Boolean scoreType;
    //货品
    private String officeId;
    private String depotId;
    private String areaId;
    private String shopName;
    private Boolean isDetail=false;
    //需要导出的office的下级
    private List<String> officeIds=Lists.newArrayList();
    //库存报表分页门店数据
    private List<String> depotIds=Lists.newArrayList();
    private String exportType;
    private List<String> productTypeIdList=Lists.newArrayList();

    public List<String> getDepotIds() {
        return depotIds;
    }

    public void setDepotIds(List<String> depotIds) {
        this.depotIds = depotIds;
    }

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

    public Boolean getScoreType() {
        return scoreType;
    }

    public void setScoreType(Boolean scoreType) {
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
        }
        return null;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        if(StringUtils.isNotBlank(getDateRange())) {
            return LocalDateUtils.parse(getDateRange().split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        }
        return null;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
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

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }
}
