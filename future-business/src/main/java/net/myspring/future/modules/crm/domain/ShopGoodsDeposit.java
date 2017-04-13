package net.myspring.future.modules.crm.domain;


import net.myspring.common.domain.DataEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="crm_shop_goods_deposit")
public class ShopGoodsDeposit extends DataEntity<ShopGoodsDeposit> {
    private String shopId;
    private String type;
    private BigDecimal amount;
    private BigDecimal leftAmount;
    private LocalDateTime billDate;
    private String departMent;
    private String outBillType;
    private String status;
    private Integer version = 0;
    private String outCode;
    private Bank bank;
    private String bankId;
    private String cloudSynId;

    private Depot shop;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getLeftAmount() {
        return leftAmount;
    }

    public void setLeftAmount(BigDecimal leftAmount) {
        this.leftAmount = leftAmount;
    }

    public LocalDateTime getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDateTime billDate) {
        this.billDate = billDate;
    }

    public String getDepartMent() {
        return departMent;
    }

    public void setDepartMent(String departMent) {
        this.departMent = departMent;
    }

    public String getOutBillType() {
        return outBillType;
    }

    public void setOutBillType(String outBillType) {
        this.outBillType = outBillType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getCloudSynId() {
        return cloudSynId;
    }

    public void setCloudSynId(String cloudSynId) {
        this.cloudSynId = cloudSynId;
    }

    public Depot getShop() {
        return shop;
    }

    public void setShop(Depot shop) {
        this.shop = shop;
    }
}
