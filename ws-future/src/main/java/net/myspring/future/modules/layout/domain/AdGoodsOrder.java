package net.myspring.future.modules.layout.domain;

import com.google.common.collect.Lists;
import net.myspring.future.common.domain.CompanyEntity;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.crm.domain.ExpressOrder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name="crm_ad_goods_order")
public class AdGoodsOrder extends CompanyEntity<AdGoodsOrder> {
    private String storeId;
    private String outShopId;
    private String shopId;
    private BigDecimal amount;
    private String outCode;
    private String billType;
    private LocalDate billDate;
    private String billRemarks;
    private Integer smallQty;
    private Integer mediumQty;
    private Integer largeQty;
    private Integer version = 0;
    private String processInstanceId;
    private String processStatus;
    private String businessId;
    private Boolean splitBill;
    private Boolean isUrgent;
    private ExpressOrder expressOrder;
    private String expressOrderId;
    private String employeeId;
    private String processTypeId;
    private String processFlowId;
    private String cloudSynId;
    private List<AdGoodsOrderDetail> adGoodsOrderDetailList = Lists.newArrayList();
    private List<String> adGoodsOrderDetailIdList = Lists.newArrayList();
    private Depot store;
    private Depot shop;
    private Depot outShop;
    private String parentId;
    private AdGoodsOrder parent;
    private String processPositionId;

    public String getProcessPositionId() {
        return processPositionId;
    }

    public void setProcessPositionId(String processPositionId) {
        this.processPositionId = processPositionId;
    }


    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getOutShopId() {
        return outShopId;
    }

    public void setOutShopId(String outShopId) {
        this.outShopId = outShopId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getBillRemarks() {
        return billRemarks;
    }

    public void setBillRemarks(String billRemarks) {
        this.billRemarks = billRemarks;
    }

    public Integer getSmallQty() {
        return smallQty;
    }

    public void setSmallQty(Integer smallQty) {
        this.smallQty = smallQty;
    }

    public Integer getMediumQty() {
        return mediumQty;
    }

    public void setMediumQty(Integer mediumQty) {
        this.mediumQty = mediumQty;
    }

    public Integer getLargeQty() {
        return largeQty;
    }

    public void setLargeQty(Integer largeQty) {
        this.largeQty = largeQty;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Boolean getSplitBill() {
        return splitBill;
    }

    public void setSplitBill(Boolean splitBill) {
        this.splitBill = splitBill;
    }

    public Boolean getUrgent() {
        return isUrgent;
    }

    public void setUrgent(Boolean urgent) {
        isUrgent = urgent;
    }

    public ExpressOrder getExpressOrder() {
        return expressOrder;
    }

    public void setExpressOrder(ExpressOrder expressOrder) {
        this.expressOrder = expressOrder;
    }

    public String getExpressOrderId() {
        return expressOrderId;
    }

    public void setExpressOrderId(String expressOrderId) {
        this.expressOrderId = expressOrderId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getProcessTypeId() {
        return processTypeId;
    }

    public void setProcessTypeId(String processTypeId) {
        this.processTypeId = processTypeId;
    }

    public String getProcessFlowId() {
        return processFlowId;
    }

    public void setProcessFlowId(String processFlowId) {
        this.processFlowId = processFlowId;
    }

    public String getCloudSynId() {
        return cloudSynId;
    }

    public void setCloudSynId(String cloudSynId) {
        this.cloudSynId = cloudSynId;
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

    public Depot getStore() {
        return store;
    }

    public void setStore(Depot store) {
        this.store = store;
    }

    public Depot getShop() {
        return shop;
    }

    public void setShop(Depot shop) {
        this.shop = shop;
    }

    public Depot getOutShop() {
        return outShop;
    }

    public void setOutShop(Depot outShop) {
        this.outShop = outShop;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public AdGoodsOrder getParent() {
        return parent;
    }

    public void setParent(AdGoodsOrder parent) {
        this.parent = parent;
    }
}
