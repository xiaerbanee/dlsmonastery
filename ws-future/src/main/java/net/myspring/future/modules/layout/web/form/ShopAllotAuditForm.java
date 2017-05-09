package net.myspring.future.modules.layout.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.DataForm;
import net.myspring.future.modules.layout.domain.ShopAllot;

import java.util.List;

/**
 * Created by wangzm on 2017/4/21.
 */
public class ShopAllotAuditForm extends DataForm<ShopAllot> {

    private Boolean syn;
    private Boolean pass;
    private String auditRemarks;

    private List<ShopAllotDetailForm> shopAllotDetailList = Lists.newArrayList();

    public List<ShopAllotDetailForm> getShopAllotDetailList() {
        return shopAllotDetailList;
    }

    public void setShopAllotDetailList(List<ShopAllotDetailForm> shopAllotDetailList) {
        this.shopAllotDetailList = shopAllotDetailList;
    }

    public Boolean getSyn() {
        return syn;
    }

    public void setSyn(Boolean syn) {
        this.syn = syn;
    }

    public Boolean getPass() {
        return pass;
    }

    public void setPass(Boolean pass) {
        this.pass = pass;
    }

    public String getAuditRemarks() {
        return auditRemarks;
    }

    public void setAuditRemarks(String auditRemarks) {
        this.auditRemarks = auditRemarks;
    }


}
