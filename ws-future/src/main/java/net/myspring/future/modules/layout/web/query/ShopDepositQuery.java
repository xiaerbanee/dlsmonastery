package net.myspring.future.modules.layout.web.query;

import com.google.common.collect.Lists;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.time.LocalDateTimeUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ShopDepositQuery extends BaseQuery {

    private String shopName;
    private String type;
    private String remarks;

    private String createdDateRange;

    private LocalDateTime createdDateStart;
    private LocalDateTime createdDateEnd;
    private List<String> typeList= Lists.newArrayList();


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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getCreatedDateStart() {
        return createdDateStart;
    }

    public LocalDateTime getCreatedDateEnd() {
        return createdDateEnd;
    }


}
