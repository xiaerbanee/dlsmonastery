package net.myspring.future.modules.crm.dto;

import com.google.common.collect.Lists;
import net.myspring.common.dto.DataDto;
import net.myspring.future.common.constant.FormatterConstant;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.IdUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class GoodsOrderDto extends DataDto<GoodsOrder> {
    //门店额度
    private BigDecimal shopCredit;
    //金蝶单号
    private String outCode;
    //门店所在区域
    @CacheInput(inputKey = "offices",inputInstance = "shopAreaId",outputInstance = "name")
    private String shopAreaName;
    private String shopAreaId;
    private String businessId;
    private LocalDate billDate;
    private LocalDateTime shipDate;
    private String status;
    private String shopId;
    @CacheInput(inputKey = "depots",inputInstance = "shopId",outputInstance = "name")
    private String shopName;
    private String storeId;
    @CacheInput(inputKey = "depots",inputInstance = "storeId",outputInstance = "name")
    private String storeName;
    private String shipType;
    //总金额
    private BigDecimal amount;
    //门店应收
    private BigDecimal shopShouldGet;
    private String netType;
    private String clientId;
    @CacheInput(inputKey = "clients",inputInstance = "clientId",outputInstance = "name")
    private String clientName;
    @CacheInput(inputKey = "clients",inputInstance = "clientId",outputInstance = "outId")
    private String clientOutId;

    private String pricesystemId;
    @CacheInput(inputKey = "pricesystems",inputInstance = "pricesystemId",outputInstance = "name")
    private String pricesystemName;
    private String shopType;
    private Boolean enabled;
    private String  rebateRuleRemarks;
    private String expressOrderId;
    private String expressCompanyId;
    private Boolean syn;
    private String pullStatus;
    private String address;
    private String contator;
    private String mobilePhone;
    private String expressOrderExpressCodes;
    private Boolean lxMallOrder;

    private List<GoodsOrderDetailDto> goodsOrderDetailDtoList = Lists.newArrayList();

    private List<GoodsOrderImeDto> goodsOrderImeDtoList = Lists.newArrayList();

    public Boolean getLxMallOrder() {
        return lxMallOrder;
    }

    public void setLxMallOrder(Boolean lxMallOrder) {
        this.lxMallOrder = lxMallOrder;
    }

    public String getClientOutId() {
        return clientOutId;
    }

    public void setClientOutId(String clientOutId) {
        this.clientOutId = clientOutId;
    }

    public String getShopAreaId() {
        return shopAreaId;
    }

    public void setShopAreaId(String shopAreaId) {
        this.shopAreaId = shopAreaId;
    }

    public Boolean getIsUseTicket() {
        return isUseTicket;
    }

    public void setIsUseTicket(Boolean useTicket) {
        isUseTicket = useTicket;
    }

    private Boolean isUseTicket;

    public String getExpressOrderId() {
        return expressOrderId;
    }

    public void setExpressOrderId(String expressOrderId) {
        this.expressOrderId = expressOrderId;
    }

    public BigDecimal getShopCredit() {
        return shopCredit;
    }

    public void setShopCredit(BigDecimal shopCredit) {
        this.shopCredit = shopCredit;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public String getShopAreaName() {
        return shopAreaName;
    }

    public void setShopAreaName(String shopAreaName) {
        this.shopAreaName = shopAreaName;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public LocalDateTime getShipDate() {
        return shipDate;
    }

    public void setShipDate(LocalDateTime shipDate) {
        this.shipDate = shipDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getShopShouldGet() {
        return shopShouldGet;
    }

    public void setShopShouldGet(BigDecimal shopShouldGet) {
        this.shopShouldGet = shopShouldGet;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPricesystemId() {
        return pricesystemId;
    }

    public void setPricesystemId(String pricesystemId) {
        this.pricesystemId = pricesystemId;
    }

    public String getPricesystemName() {
        return pricesystemName;
    }

    public void setPricesystemName(String pricesystemName) {
        this.pricesystemName = pricesystemName;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    public String getRebateRuleRemarks() {
        return rebateRuleRemarks;
    }

    public void setRebateRuleRemarks(String rebateRuleRemarks) {
        this.rebateRuleRemarks = rebateRuleRemarks;
    }

    public List<GoodsOrderDetailDto> getGoodsOrderDetailDtoList() {
        return goodsOrderDetailDtoList;
    }

    public void setGoodsOrderDetailDtoList(List<GoodsOrderDetailDto> goodsOrderDetailDtoList) {
        this.goodsOrderDetailDtoList = goodsOrderDetailDtoList;
    }

    public List<GoodsOrderImeDto> getGoodsOrderImeDtoList() {
        return goodsOrderImeDtoList;
    }

    public void setGoodsOrderImeDtoList(List<GoodsOrderImeDto> goodsOrderImeDtoList) {
        this.goodsOrderImeDtoList = goodsOrderImeDtoList;
    }

    public String getExpressCompanyId() {
        return expressCompanyId;
    }

    public void setExpressCompanyId(String expressCompanyId) {
        this.expressCompanyId = expressCompanyId;
    }

    public Boolean getSyn() {
        return syn;
    }

    public void setSyn(Boolean syn) {
        this.syn = syn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPullStatus() {
        return pullStatus;
    }

    public void setPullStatus(String pullStatus) {
        this.pullStatus = pullStatus;
    }

    public String getExpressOrderExpressCodes() {
        return expressOrderExpressCodes;
    }

    public void setExpressOrderExpressCodes(String expressOrderExpressCodes) {
        this.expressOrderExpressCodes = expressOrderExpressCodes;
    }

    public BigDecimal getShopShouldGetAfterBill(){
        if(amount !=null && shopShouldGet != null){
            return amount.add(shopShouldGet);
        }
        if(amount == null){
            return shopShouldGet;
        }

        if(shopShouldGet == null){
            return amount;
        }
        return null;

    }
}
