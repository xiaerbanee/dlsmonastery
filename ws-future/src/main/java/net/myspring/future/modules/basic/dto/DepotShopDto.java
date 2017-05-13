package net.myspring.future.modules.basic.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.DepotShop;
import net.myspring.util.cahe.annotation.CacheInput;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by wangzm on 2017/5/13.
 */
public class DepotShopDto extends DataDto<DepotShop>{

    private String depotId;
    // 地区属性
    private String areaType;
    // 门店类型
    private String type;
    // 价格体系
    private String pricesystemId;
    // 额度
    private BigDecimal credit;
    //税务名称
    private String taxName;
    //是否打印价格
    private Boolean printPrice;
    // 客户
    private String clientId;
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
    @CacheInput(inputKey = "depots",inputInstance = "depotId",outputInstance = "name")
    private String depotName;
    @CacheInput(inputKey = "pricesystems",inputInstance = "pricesystemId",outputInstance = "name")
    private String pricesystemName;
    @CacheInput(inputKey = "adPricesystems",inputInstance = "adPricesystemId",outputInstance = "name")
    private String adPricesystemName;
    @CacheInput(inputKey = "expressCompanys",inputInstance = "expressCompanyId",outputInstance = "name")
    private String expressCompanyName;

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getPricesystemName() {
        return pricesystemName;
    }

    public void setPricesystemName(String pricesystemName) {
        this.pricesystemName = pricesystemName;
    }

    public String getAdPricesystemName() {
        return adPricesystemName;
    }

    public void setAdPricesystemName(String adPricesystemName) {
        this.adPricesystemName = adPricesystemName;
    }

    public String getExpressCompanyName() {
        return expressCompanyName;
    }

    public void setExpressCompanyName(String expressCompanyName) {
        this.expressCompanyName = expressCompanyName;
    }

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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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
