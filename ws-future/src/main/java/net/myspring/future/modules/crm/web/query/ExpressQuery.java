package net.myspring.future.modules.crm.web.query;

import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.time.LocalDateTimeUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ExpressQuery extends BaseQuery {



    private String code;
    private String expressOrderExtendType;
    private String expressOrderToDepotId;

    private String expressOrderExtendBusinessId;

    private String expressOrderFromDepotId;
    private String expressOrderExpressCompanyId;
    private String createdDateRange;
    private LocalDateTime createdDateStart;
    private LocalDateTime createdDateEnd;
    private List<String> expressOrderExtendTypeList;


    public String getExpressOrderToDepotId() {
        return expressOrderToDepotId;
    }

    public void setExpressOrderToDepotId(String expressOrderToDepotId) {
        this.expressOrderToDepotId = expressOrderToDepotId;
    }

    public String getExpressOrderFromDepotId() {
        return expressOrderFromDepotId;
    }

    public void setExpressOrderFromDepotId(String expressOrderFromDepotId) {
        this.expressOrderFromDepotId = expressOrderFromDepotId;
    }


    public String getExpressOrderExtendType() {
        return expressOrderExtendType;
    }

    public void setExpressOrderExtendType(String expressOrderExtendType) {
        this.expressOrderExtendType = expressOrderExtendType;
    }


    public String getExpressOrderExtendBusinessId() {
        return expressOrderExtendBusinessId;
    }

    public void setExpressOrderExtendBusinessId(String expressOrderExtendBusinessId) {
        this.expressOrderExtendBusinessId = expressOrderExtendBusinessId;
    }


    public String getExpressOrderExpressCompanyId() {
        return expressOrderExpressCompanyId;
    }

    public void setExpressOrderExpressCompanyId(String expressOrderExpressCompanyId) {
        this.expressOrderExpressCompanyId = expressOrderExpressCompanyId;
    }

    public List<String> getExpressOrderExtendTypeList() {
        return expressOrderExtendTypeList;
    }

    public void setExpressOrderExtendTypeList(List<String> expressOrderExtendTypeList) {
        this.expressOrderExtendTypeList = expressOrderExtendTypeList;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getCreatedDateRange() {
        return createdDateRange;
    }

    public void setCreatedDateRange(String createdDateRange) {
        if(createdDateRange!=null){
            String[] tempParamValues = createdDateRange.split(" - ");
            this.createdDateStart= LocalDateTimeUtils.parse(tempParamValues[0]+ " 00:00:00");
            this.createdDateEnd= LocalDateTimeUtils.parse(tempParamValues[1]+ " 00:00:00").plusDays(1);
        }
        this.createdDateRange = createdDateRange;

    }

    public LocalDateTime getCreatedDateStart() {
        return createdDateStart;
    }


    public LocalDateTime getCreatedDateEnd() {
        return createdDateEnd;
    }



}
