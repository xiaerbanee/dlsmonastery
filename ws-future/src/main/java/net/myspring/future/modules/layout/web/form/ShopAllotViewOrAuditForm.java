package net.myspring.future.modules.layout.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.DataForm;
import net.myspring.future.modules.layout.domain.ShopAllot;
import net.myspring.future.modules.layout.dto.ShopAllotDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wangzm on 2017/4/21.
 */
public class ShopAllotViewOrAuditForm extends DataForm<ShopAllot> {

    private ShopAllotDto shopAllotDto;

    private String syn="1";
    private String pass="0";
    private String auditRemarks;
    private BigDecimal toShopShouldGet;
    private BigDecimal fromShopShouldGet;

    private List<ShopAllotDetailForm> shopAllotDetailList = Lists.newArrayList();

    public ShopAllotDto getShopAllotDto() {
        return shopAllotDto;
    }

    public void setShopAllotDto(ShopAllotDto shopAllotDto) {
        this.shopAllotDto = shopAllotDto;
    }

    public String getSyn() {
        return syn;
    }

    public void setSyn(String syn) {
        this.syn = syn;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getAuditRemarks() {
        return auditRemarks;
    }

    public void setAuditRemarks(String auditRemarks) {
        this.auditRemarks = auditRemarks;
    }

    public BigDecimal getToShopShouldGet() {
        return toShopShouldGet;
    }

    public void setToShopShouldGet(BigDecimal toShopShouldGet) {
        this.toShopShouldGet = toShopShouldGet;
    }

    public BigDecimal getFromShopShouldGet() {
        return fromShopShouldGet;
    }

    public void setFromShopShouldGet(BigDecimal fromShopShouldGet) {
        this.fromShopShouldGet = fromShopShouldGet;
    }

    public List<ShopAllotDetailForm> getShopAllotDetailList() {
        return shopAllotDetailList;
    }

    public void setShopAllotDetailList(List<ShopAllotDetailForm> shopAllotDetailList) {
        this.shopAllotDetailList = shopAllotDetailList;
    }
}
