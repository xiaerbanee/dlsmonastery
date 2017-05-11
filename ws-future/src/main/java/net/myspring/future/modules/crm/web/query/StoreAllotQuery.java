package net.myspring.future.modules.crm.web.query;

import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StoreAllotQuery extends BaseQuery {

    private String businessIds;

    public List<String> getBusinessIdList() {
        return businessIdList;
    }

    private List<String> businessIdList;
    private String fromStoreId;
    private String toStoreId;
    private String status;
    private String createdDateRange;
    private LocalDateTime createdDateStart;
    private LocalDateTime createdDateEnd;
    private String outCode;
    private String remarks;
    private List<String> statusList = new ArrayList<String>();

    public String getBusinessIds() {
        return businessIds;
    }

    public void setBusinessIds(String businessIds) {
        this.businessIds = businessIds;
        if(StringUtils.isEmpty(businessIds)) {
            businessIdList = null;
        }else{
            String tmp =  businessIds.replaceAll("\\R" , ",");
            tmp =  tmp.replaceAll("，" , ",");
            String[] result = tmp.split(",", 0);
            businessIdList = new ArrayList<>();
            for(int i =0; i< result.length; i++ ){
                businessIdList.add(StringUtils.trimToEmpty(result[i]));

            }
        }
    }


    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFromStoreId() {
        return fromStoreId;
    }

    public void setFromStoreId(String fromStoreId) {
        this.fromStoreId = fromStoreId;
    }

    public String getToStoreId() {
        return toStoreId;
    }

    public void setToStoreId(String toStoreId) {
        this.toStoreId = toStoreId;
    }

    public void setCreatedDateRange(String createdDateRange) {
        if(createdDateRange!=null){
            String[] tempParamValues = createdDateRange.split(" - ");
            this.createdDateStart= LocalDateTimeUtils.parse(tempParamValues[0]+ " 00:00:00");
            this.createdDateEnd= LocalDateTimeUtils.parse(tempParamValues[1]+ " 00:00:00").plusDays(1);
        }
        this.createdDateRange = createdDateRange;

    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDateRange() {
        return createdDateRange;
    }

    public LocalDateTime getCreatedDateStart() {
        return createdDateStart;
    }

    public LocalDateTime getCreatedDateEnd() {
        return createdDateEnd;
    }

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }



}
