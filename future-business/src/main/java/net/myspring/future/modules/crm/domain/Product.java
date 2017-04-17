package net.myspring.future.modules.crm.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="crm_product")
public class Product extends DataEntity<Product> {
    private String name;
    private String code;
    private Boolean hasIme;
    private Boolean allowOrder;
    private Boolean allowBill;
    private Integer version = 0;
    private String netType;
    private String outId;
    private String outGroupId;
    private String outGroupName;
    private LocalDateTime outDate;
    private Boolean visible;
    private BigDecimal price1;
    private BigDecimal price2;
    private BigDecimal retailPrice;
    private BigDecimal depositPrice;
    private String expiryDateRemarks;
    private String image;
    private BigDecimal volume;
    private BigDecimal shouldGet;
    private String oldName;
    private String mappingName;
    private String oldOutId;
    private List<String> carrierProductIdList = Lists.newArrayList();
    private List<String> imooPlantBasicProductIdList = Lists.newArrayList();
    private List<String> imooProductMapIdList = Lists.newArrayList();
    private List<String> oppoPlantAgentProductSelIdList = Lists.newArrayList();
    private List<String> vivoPlantElectronicsnIdList = Lists.newArrayList();
    private List<String> vivoPlantProductsIdList = Lists.newArrayList();
    private List<String> vivoPlantSendimeiIdList = Lists.newArrayList();
    private List<AdApply> adApplyList = Lists.newArrayList();
    private List<String> adApplyIdList = Lists.newArrayList();
    private List<AdGoodsOrderDetail> adGoodsOrderDetailList = Lists.newArrayList();
    private List<String> adGoodsOrderDetailIdList = Lists.newArrayList();
    private List<AdPricesystemChange> adPricesystemChangeList = Lists.newArrayList();
    private List<String> adPricesystemChangeIdList = Lists.newArrayList();
    private List<AdPricesystemDetail> adPricesystemDetailList = Lists.newArrayList();
    private List<String> adPricesystemDetailIdList = Lists.newArrayList();
    private List<AfterSaleStoreAllot> afterSaleStoreAllotList = Lists.newArrayList();
    private List<String> afterSaleStoreAllotIdList = Lists.newArrayList();
    private List<DepotDetail> depotDetailList = Lists.newArrayList();
    private List<String> depotDetailIdList = Lists.newArrayList();
    private List<GoodsOrderDetail> goodsOrderDetailList = Lists.newArrayList();
    private List<String> goodsOrderDetailIdList = Lists.newArrayList();
    private List<GoodsOrderIme> goodsOrderImeList = Lists.newArrayList();
    private List<String> goodsOrderImeIdList = Lists.newArrayList();
    private List<PriceChangeProduct> priceChangeProductList = Lists.newArrayList();
    private List<String> priceChangeProductIdList = Lists.newArrayList();
    private List<PricesystemChange> pricesystemChangeList = Lists.newArrayList();
    private List<String> pricesystemChangeIdList = Lists.newArrayList();
    private List<PricesystemDetail> pricesystemDetailList = Lists.newArrayList();
    private List<String> pricesystemDetailIdList = Lists.newArrayList();
    private ProductType productType;
    private String productTypeId;
    private String carrierProductId;
    private List<ProductIme> productImeList = Lists.newArrayList();
    private List<String> productImeIdList = Lists.newArrayList();
    private List<ShopAllotDetail> shopAllotDetailList = Lists.newArrayList();
    private List<String> shopAllotDetailIdList = Lists.newArrayList();
    private List<StoreAllotDetail> storeAllotDetailList = Lists.newArrayList();
    private List<String> storeAllotDetailIdList = Lists.newArrayList();
    private List<StoreAllotIme> storeAllotImeList = Lists.newArrayList();
    private List<String> storeAllotImeIdList = Lists.newArrayList();
    private List<String> employeePhoneIdList = Lists.newArrayList();
    private List<String> employeePhoneDepositIdList = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getHasIme() {
        return hasIme;
    }

    public void setHasIme(Boolean hasIme) {
        this.hasIme = hasIme;
    }

    public Boolean getAllowOrder() {
        return allowOrder;
    }

    public void setAllowOrder(Boolean allowOrder) {
        this.allowOrder = allowOrder;
    }

    public Boolean getAllowBill() {
        return allowBill;
    }

    public void setAllowBill(Boolean allowBill) {
        this.allowBill = allowBill;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getOutGroupId() {
        return outGroupId;
    }

    public void setOutGroupId(String outGroupId) {
        this.outGroupId = outGroupId;
    }

    public String getOutGroupName() {
        return outGroupName;
    }

    public void setOutGroupName(String outGroupName) {
        this.outGroupName = outGroupName;
    }

    public LocalDateTime getOutDate() {
        return outDate;
    }

    public void setOutDate(LocalDateTime outDate) {
        this.outDate = outDate;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public BigDecimal getPrice1() {
        return price1;
    }

    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

    public BigDecimal getPrice2() {
        return price2;
    }

    public void setPrice2(BigDecimal price2) {
        this.price2 = price2;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public BigDecimal getDepositPrice() {
        return depositPrice;
    }

    public void setDepositPrice(BigDecimal depositPrice) {
        this.depositPrice = depositPrice;
    }

    public String getExpiryDateRemarks() {
        return expiryDateRemarks;
    }

    public void setExpiryDateRemarks(String expiryDateRemarks) {
        this.expiryDateRemarks = expiryDateRemarks;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getShouldGet() {
        return shouldGet;
    }

    public void setShouldGet(BigDecimal shouldGet) {
        this.shouldGet = shouldGet;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getMappingName() {
        return mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }

    public String getOldOutId() {
        return oldOutId;
    }

    public void setOldOutId(String oldOutId) {
        this.oldOutId = oldOutId;
    }

    public List<String> getCarrierProductIdList() {
        return carrierProductIdList;
    }

    public void setCarrierProductIdList(List<String> carrierProductIdList) {
        this.carrierProductIdList = carrierProductIdList;
    }

    public List<String> getImooPlantBasicProductIdList() {
        return imooPlantBasicProductIdList;
    }

    public void setImooPlantBasicProductIdList(List<String> imooPlantBasicProductIdList) {
        this.imooPlantBasicProductIdList = imooPlantBasicProductIdList;
    }

    public List<String> getImooProductMapIdList() {
        return imooProductMapIdList;
    }

    public void setImooProductMapIdList(List<String> imooProductMapIdList) {
        this.imooProductMapIdList = imooProductMapIdList;
    }

    public List<String> getOppoPlantAgentProductSelIdList() {
        return oppoPlantAgentProductSelIdList;
    }

    public void setOppoPlantAgentProductSelIdList(List<String> oppoPlantAgentProductSelIdList) {
        this.oppoPlantAgentProductSelIdList = oppoPlantAgentProductSelIdList;
    }

    public List<String> getVivoPlantElectronicsnIdList() {
        return vivoPlantElectronicsnIdList;
    }

    public void setVivoPlantElectronicsnIdList(List<String> vivoPlantElectronicsnIdList) {
        this.vivoPlantElectronicsnIdList = vivoPlantElectronicsnIdList;
    }

    public List<String> getVivoPlantProductsIdList() {
        return vivoPlantProductsIdList;
    }

    public void setVivoPlantProductsIdList(List<String> vivoPlantProductsIdList) {
        this.vivoPlantProductsIdList = vivoPlantProductsIdList;
    }

    public List<String> getVivoPlantSendimeiIdList() {
        return vivoPlantSendimeiIdList;
    }

    public void setVivoPlantSendimeiIdList(List<String> vivoPlantSendimeiIdList) {
        this.vivoPlantSendimeiIdList = vivoPlantSendimeiIdList;
    }

    public List<AdApply> getAdApplyList() {
        return adApplyList;
    }

    public void setAdApplyList(List<AdApply> adApplyList) {
        this.adApplyList = adApplyList;
    }

    public List<String> getAdApplyIdList() {
        return adApplyIdList;
    }

    public void setAdApplyIdList(List<String> adApplyIdList) {
        this.adApplyIdList = adApplyIdList;
    }

    public List<AdGoodsOrderDetail> getAdGoodsOrderDetailList() {
        return adGoodsOrderDetailList;
    }

    public void setAdGoodsOrderDetailList(List<AdGoodsOrderDetail> adGoodsOrderDetailList) {
        this.adGoodsOrderDetailList = adGoodsOrderDetailList;
    }

    public List<String> getAdGoodsOrderDetailIdList() {
        return adGoodsOrderDetailIdList;
    }

    public void setAdGoodsOrderDetailIdList(List<String> adGoodsOrderDetailIdList) {
        this.adGoodsOrderDetailIdList = adGoodsOrderDetailIdList;
    }

    public List<AdPricesystemChange> getAdPricesystemChangeList() {
        return adPricesystemChangeList;
    }

    public void setAdPricesystemChangeList(List<AdPricesystemChange> adPricesystemChangeList) {
        this.adPricesystemChangeList = adPricesystemChangeList;
    }

    public List<String> getAdPricesystemChangeIdList() {
        return adPricesystemChangeIdList;
    }

    public void setAdPricesystemChangeIdList(List<String> adPricesystemChangeIdList) {
        this.adPricesystemChangeIdList = adPricesystemChangeIdList;
    }

    public List<AdPricesystemDetail> getAdPricesystemDetailList() {
        return adPricesystemDetailList;
    }

    public void setAdPricesystemDetailList(List<AdPricesystemDetail> adPricesystemDetailList) {
        this.adPricesystemDetailList = adPricesystemDetailList;
    }

    public List<String> getAdPricesystemDetailIdList() {
        return adPricesystemDetailIdList;
    }

    public void setAdPricesystemDetailIdList(List<String> adPricesystemDetailIdList) {
        this.adPricesystemDetailIdList = adPricesystemDetailIdList;
    }

    public List<AfterSaleStoreAllot> getAfterSaleStoreAllotList() {
        return afterSaleStoreAllotList;
    }

    public void setAfterSaleStoreAllotList(List<AfterSaleStoreAllot> afterSaleStoreAllotList) {
        this.afterSaleStoreAllotList = afterSaleStoreAllotList;
    }

    public List<String> getAfterSaleStoreAllotIdList() {
        return afterSaleStoreAllotIdList;
    }

    public void setAfterSaleStoreAllotIdList(List<String> afterSaleStoreAllotIdList) {
        this.afterSaleStoreAllotIdList = afterSaleStoreAllotIdList;
    }

    public List<DepotDetail> getDepotDetailList() {
        return depotDetailList;
    }

    public void setDepotDetailList(List<DepotDetail> depotDetailList) {
        this.depotDetailList = depotDetailList;
    }

    public List<String> getDepotDetailIdList() {
        return depotDetailIdList;
    }

    public void setDepotDetailIdList(List<String> depotDetailIdList) {
        this.depotDetailIdList = depotDetailIdList;
    }

    public List<GoodsOrderDetail> getGoodsOrderDetailList() {
        return goodsOrderDetailList;
    }

    public void setGoodsOrderDetailList(List<GoodsOrderDetail> goodsOrderDetailList) {
        this.goodsOrderDetailList = goodsOrderDetailList;
    }

    public List<String> getGoodsOrderDetailIdList() {
        return goodsOrderDetailIdList;
    }

    public void setGoodsOrderDetailIdList(List<String> goodsOrderDetailIdList) {
        this.goodsOrderDetailIdList = goodsOrderDetailIdList;
    }

    public List<GoodsOrderIme> getGoodsOrderImeList() {
        return goodsOrderImeList;
    }

    public void setGoodsOrderImeList(List<GoodsOrderIme> goodsOrderImeList) {
        this.goodsOrderImeList = goodsOrderImeList;
    }

    public List<String> getGoodsOrderImeIdList() {
        return goodsOrderImeIdList;
    }

    public void setGoodsOrderImeIdList(List<String> goodsOrderImeIdList) {
        this.goodsOrderImeIdList = goodsOrderImeIdList;
    }

    public List<PriceChangeProduct> getPriceChangeProductList() {
        return priceChangeProductList;
    }

    public void setPriceChangeProductList(List<PriceChangeProduct> priceChangeProductList) {
        this.priceChangeProductList = priceChangeProductList;
    }

    public List<String> getPriceChangeProductIdList() {
        return priceChangeProductIdList;
    }

    public void setPriceChangeProductIdList(List<String> priceChangeProductIdList) {
        this.priceChangeProductIdList = priceChangeProductIdList;
    }

    public List<PricesystemChange> getPricesystemChangeList() {
        return pricesystemChangeList;
    }

    public void setPricesystemChangeList(List<PricesystemChange> pricesystemChangeList) {
        this.pricesystemChangeList = pricesystemChangeList;
    }

    public List<String> getPricesystemChangeIdList() {
        return pricesystemChangeIdList;
    }

    public void setPricesystemChangeIdList(List<String> pricesystemChangeIdList) {
        this.pricesystemChangeIdList = pricesystemChangeIdList;
    }

    public List<PricesystemDetail> getPricesystemDetailList() {
        return pricesystemDetailList;
    }

    public void setPricesystemDetailList(List<PricesystemDetail> pricesystemDetailList) {
        this.pricesystemDetailList = pricesystemDetailList;
    }

    public List<String> getPricesystemDetailIdList() {
        return pricesystemDetailIdList;
    }

    public void setPricesystemDetailIdList(List<String> pricesystemDetailIdList) {
        this.pricesystemDetailIdList = pricesystemDetailIdList;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getCarrierProductId() {
        return carrierProductId;
    }

    public void setCarrierProductId(String carrierProductId) {
        this.carrierProductId = carrierProductId;
    }

    public List<ProductIme> getProductImeList() {
        return productImeList;
    }

    public void setProductImeList(List<ProductIme> productImeList) {
        this.productImeList = productImeList;
    }

    public List<String> getProductImeIdList() {
        return productImeIdList;
    }

    public void setProductImeIdList(List<String> productImeIdList) {
        this.productImeIdList = productImeIdList;
    }

    public List<ShopAllotDetail> getShopAllotDetailList() {
        return shopAllotDetailList;
    }

    public void setShopAllotDetailList(List<ShopAllotDetail> shopAllotDetailList) {
        this.shopAllotDetailList = shopAllotDetailList;
    }

    public List<String> getShopAllotDetailIdList() {
        return shopAllotDetailIdList;
    }

    public void setShopAllotDetailIdList(List<String> shopAllotDetailIdList) {
        this.shopAllotDetailIdList = shopAllotDetailIdList;
    }

    public List<StoreAllotDetail> getStoreAllotDetailList() {
        return storeAllotDetailList;
    }

    public void setStoreAllotDetailList(List<StoreAllotDetail> storeAllotDetailList) {
        this.storeAllotDetailList = storeAllotDetailList;
    }

    public List<String> getStoreAllotDetailIdList() {
        return storeAllotDetailIdList;
    }

    public void setStoreAllotDetailIdList(List<String> storeAllotDetailIdList) {
        this.storeAllotDetailIdList = storeAllotDetailIdList;
    }

    public List<StoreAllotIme> getStoreAllotImeList() {
        return storeAllotImeList;
    }

    public void setStoreAllotImeList(List<StoreAllotIme> storeAllotImeList) {
        this.storeAllotImeList = storeAllotImeList;
    }

    public List<String> getStoreAllotImeIdList() {
        return storeAllotImeIdList;
    }

    public void setStoreAllotImeIdList(List<String> storeAllotImeIdList) {
        this.storeAllotImeIdList = storeAllotImeIdList;
    }

    public List<String> getEmployeePhoneIdList() {
        return employeePhoneIdList;
    }

    public void setEmployeePhoneIdList(List<String> employeePhoneIdList) {
        this.employeePhoneIdList = employeePhoneIdList;
    }

    public List<String> getEmployeePhoneDepositIdList() {
        return employeePhoneDepositIdList;
    }

    public void setEmployeePhoneDepositIdList(List<String> employeePhoneDepositIdList) {
        this.employeePhoneDepositIdList = employeePhoneDepositIdList;
    }
}
