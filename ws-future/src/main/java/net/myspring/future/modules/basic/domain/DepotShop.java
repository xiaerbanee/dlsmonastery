package net.myspring.future.modules.basic.domain;

import net.myspring.future.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by liuj on 2017/5/12.
 */

@Entity
@Table(name="crm_depot_shop")
public class DepotShop extends CompanyEntity<DepotShop> {
    private String depotId;
    // 地区属性
    private String areaType;
    // 门店类型
    private String type;
    // 价格体系
    private String pricesystemId;
    //寄售对应仓库
    private String delegateStoreId;
    // 额度
    private BigDecimal credit;
    //税务名称
    private String taxName;
    //是否打印价格
    private Boolean printPrice;
    // 经销商
    private String dealerId;
    // 连锁体系
    private String chainId;
    //物料价格体系
    private String adPricesystemId;
    // 打印类型，批量打印时用于分批打印
    private String printType;
    // 快递公司
    private String expressCompanyId;
    // 是否让利
    private Boolean rebate;
    //是否隐藏
    private Boolean isHidden=false;
    private Boolean adShop=false;

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPricesystemId() {
        return pricesystemId;
    }

    public void setPricesystemId(String pricesystemId) {
        this.pricesystemId = pricesystemId;
    }

    public String getDelegateStoreId() {
        return delegateStoreId;
    }

    public void setDelegateStoreId(String delegateStoreId) {
        this.delegateStoreId = delegateStoreId;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public Boolean getPrintPrice() {
        return printPrice;
    }

    public void setPrintPrice(Boolean printPrice) {
        this.printPrice = printPrice;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public String getAdPricesystemId() {
        return adPricesystemId;
    }

    public void setAdPricesystemId(String adPricesystemId) {
        this.adPricesystemId = adPricesystemId;
    }

    public String getPrintType() {
        return printType;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

    public String getExpressCompanyId() {
        return expressCompanyId;
    }

    public void setExpressCompanyId(String expressCompanyId) {
        this.expressCompanyId = expressCompanyId;
    }

    public Boolean getRebate() {
        return rebate;
    }

    public void setRebate(Boolean rebate) {
        this.rebate = rebate;
    }

    public Boolean getHidden() {
        return isHidden;
    }

    public void setHidden(Boolean hidden) {
        isHidden = hidden;
    }

    public Boolean getAdShop() {
        return adShop;
    }

    public void setAdShop(Boolean adShop) {
        this.adShop = adShop;
    }
}
