package net.myspring.future.modules.crm.web.query;

import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.time.LocalDateTimeUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExpressOrderQuery extends BaseQuery {

    private List<String> fromDepotIdList = new ArrayList<>();
    private String toDepotName;
    private String expressCompanyName;
    private String extendBusinessId;
    private String extendBusinessIdStart;

    private String printDateRange;
    private LocalDateTime printDateStart;
    private LocalDateTime printDateEnd;
    private String extendType;
    private List<String> extendTypeList;
    private Boolean outPrint;
    private Boolean expressPrint;

    public String getExtendBusinessIdStart() {
        return extendBusinessIdStart;
    }

    public void setExtendBusinessIdStart(String extendBusinessIdStart) {
        this.extendBusinessIdStart = extendBusinessIdStart;
    }

    public List<String> getFromDepotIdList() {
        return fromDepotIdList;
    }

    public void setFromDepotIdList(List<String> fromDepotIdList) {
        this.fromDepotIdList = fromDepotIdList;
    }

    public String getToDepotName() {
        return toDepotName;
    }

    public void setToDepotName(String toDepotName) {
        this.toDepotName = toDepotName;
    }

    public String getExpressCompanyName() {
        return expressCompanyName;
    }

    public void setExpressCompanyName(String expressCompanyName) {
        this.expressCompanyName = expressCompanyName;
    }

    public String getExtendBusinessId() {
        return extendBusinessId;
    }

    public void setExtendBusinessId(String extendBusinessId) {
        this.extendBusinessId = extendBusinessId;
    }

    public String getPrintDateRange() {
        return printDateRange;
    }

    public void setPrintDateRange(String printDateRange) {
        if(printDateRange!=null){
            String[] tempParamValues = printDateRange.split(" - ");
            this.printDateStart= LocalDateTimeUtils.parse(tempParamValues[0]+ " 00:00:00");
            this.printDateEnd= LocalDateTimeUtils.parse(tempParamValues[1]+ " 00:00:00").plusDays(1);
        }
        this.printDateRange = printDateRange;

    }

    public LocalDateTime getPrintDateStart() {
        return printDateStart;
    }

    public LocalDateTime getPrintDateEnd() {
        return printDateEnd;
    }

    public String getExtendType() {
        return extendType;
    }

    public void setExtendType(String extendType) {
        this.extendType = extendType;
    }

    public List<String> getExtendTypeList() {
        return extendTypeList;
    }

    public void setExtendTypeList(List<String> extendTypeList) {
        this.extendTypeList = extendTypeList;
    }

    public Boolean getOutPrint() {
        return outPrint;
    }

    public void setOutPrint(Boolean outPrint) {
        this.outPrint = outPrint;
    }

    public Boolean getExpressPrint() {
        return expressPrint;
    }

    public void setExpressPrint(Boolean expressPrint) {
        this.expressPrint = expressPrint;
    }
}
