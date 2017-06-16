package net.myspring.future.modules.layout.web.query;

import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class AdGoodsOrderDetailQuery extends BaseQuery {

    private String adGoodsOrderIdStr;
    private String adGoodsOrderCreatedDateRange;
    private String adGoodsOrderBillDateRange;
    private String adGoodsOrderProcessStatus;
    private String adGoodsOrderRemarks;
    private String productId;
    private String adGoodsOrderStoreId;
    private String adGoodsOrderShopId;
    private String adGoodsOrderShopAreaId;
    private String adGoodsOrderBillType;
    private String adGoodsOrderCreatedBy;

    public String getAdGoodsOrderIdStr() {
        return adGoodsOrderIdStr;
    }

    public void setAdGoodsOrderIdStr(String adGoodsOrderIdStr) {
        this.adGoodsOrderIdStr = adGoodsOrderIdStr;
    }

    public String getAdGoodsOrderCreatedDateRange() {
        return adGoodsOrderCreatedDateRange;
    }

    public void setAdGoodsOrderCreatedDateRange(String adGoodsOrderCreatedDateRange) {
        this.adGoodsOrderCreatedDateRange = adGoodsOrderCreatedDateRange;
    }

    public String getAdGoodsOrderBillDateRange() {
        return adGoodsOrderBillDateRange;
    }

    public void setAdGoodsOrderBillDateRange(String adGoodsOrderBillDateRange) {
        this.adGoodsOrderBillDateRange = adGoodsOrderBillDateRange;
    }

    public String getAdGoodsOrderProcessStatus() {
        return adGoodsOrderProcessStatus;
    }

    public void setAdGoodsOrderProcessStatus(String adGoodsOrderProcessStatus) {
        this.adGoodsOrderProcessStatus = adGoodsOrderProcessStatus;
    }

    public String getAdGoodsOrderRemarks() {
        return adGoodsOrderRemarks;
    }

    public void setAdGoodsOrderRemarks(String adGoodsOrderRemarks) {
        this.adGoodsOrderRemarks = adGoodsOrderRemarks;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAdGoodsOrderStoreId() {
        return adGoodsOrderStoreId;
    }

    public void setAdGoodsOrderStoreId(String adGoodsOrderStoreId) {
        this.adGoodsOrderStoreId = adGoodsOrderStoreId;
    }

    public String getAdGoodsOrderShopId() {
        return adGoodsOrderShopId;
    }

    public void setAdGoodsOrderShopId(String adGoodsOrderShopId) {
        this.adGoodsOrderShopId = adGoodsOrderShopId;
    }

    public String getAdGoodsOrderShopAreaId() {
        return adGoodsOrderShopAreaId;
    }

    public void setAdGoodsOrderShopAreaId(String adGoodsOrderShopAreaId) {
        this.adGoodsOrderShopAreaId = adGoodsOrderShopAreaId;
    }

    public String getAdGoodsOrderBillType() {
        return adGoodsOrderBillType;
    }

    public void setAdGoodsOrderBillType(String adGoodsOrderBillType) {
        this.adGoodsOrderBillType = adGoodsOrderBillType;
    }

    public String getAdGoodsOrderCreatedBy() {
        return adGoodsOrderCreatedBy;
    }

    public void setAdGoodsOrderCreatedBy(String adGoodsOrderCreatedBy) {
        this.adGoodsOrderCreatedBy = adGoodsOrderCreatedBy;
    }

    public List<String> getAdGoodsOrderIdList(){
        if(adGoodsOrderIdStr == null){
            return new ArrayList<>();
        }

        return StringUtils.getSplitList(adGoodsOrderIdStr, CharConstant.COMMA);
    }


    public LocalDate getAdGoodsOrderCreatedDateStart() {
        if(StringUtils.isNotBlank(adGoodsOrderCreatedDateRange)) {
            return LocalDateUtils.parse(adGoodsOrderCreatedDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getAdGoodsOrderCreatedDateEnd() {
        if(StringUtils.isNotBlank(adGoodsOrderCreatedDateRange)) {
            return LocalDateUtils.parse(adGoodsOrderCreatedDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

    public LocalDate getAdGoodsOrderBillDateStart() {
        if(StringUtils.isNotBlank(adGoodsOrderBillDateRange)) {
            return LocalDateUtils.parse(adGoodsOrderBillDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getAdGoodsOrderBillDateEnd() {
        if(StringUtils.isNotBlank(adGoodsOrderBillDateRange)) {
            return LocalDateUtils.parse(adGoodsOrderBillDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

}
