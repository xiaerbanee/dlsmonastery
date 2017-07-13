package net.myspring.future.modules.basic.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.basic.domain.DepotShop;

import java.util.List;

/**
 * Created by zhangyf on 2017/7/13.
 */
public class DepotAccountForm extends BaseForm<DepotShop>{

    private String shopId;
    private List<String> accountIds = Lists.newArrayList();

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public List<String> getAccountIds() {
        return accountIds;
    }

    public void setAccountIds(List<String> accountIds) {
        this.accountIds = accountIds;
    }
}
