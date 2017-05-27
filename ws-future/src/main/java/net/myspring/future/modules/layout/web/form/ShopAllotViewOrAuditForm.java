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


    private String syn;
    private String pass;
    private String auditRemarks;


    private List<ShopAllotDetailForm> shopAllotDetailList = Lists.newArrayList();


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

    public List<ShopAllotDetailForm> getShopAllotDetailList() {
        return shopAllotDetailList;
    }

    public void setShopAllotDetailList(List<ShopAllotDetailForm> shopAllotDetailList) {
        this.shopAllotDetailList = shopAllotDetailList;
    }
}
