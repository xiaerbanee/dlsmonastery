package net.myspring.future.modules.layout.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.DataForm;
import net.myspring.future.modules.layout.domain.ShopAllot;

import java.util.List;

/**
 * Created by wangzm on 2017/4/21.
 */
public class ShopAllotForm extends DataForm<ShopAllot> {


    private String fromShopId;
    private String toShopId;

    private Boolean success;
    private String message;
    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getFromShopId() {
        return fromShopId;
    }

    public void setFromShopId(String fromShopId) {
        this.fromShopId = fromShopId;
    }

    public String getToShopId() {
        return toShopId;
    }

    public void setToShopId(String toShopId) {
        this.toShopId = toShopId;
    }

    public List<String> getShopAllotDetailIdList() {
        return shopAllotDetailIdList;
    }

    public void setShopAllotDetailIdList(List<String> shopAllotDetailIdList) {
        this.shopAllotDetailIdList = shopAllotDetailIdList;
    }

    public List<ShopAllotDetailForm> getShopAllotDetailFormList() {
        return shopAllotDetailFormList;
    }

    public void setShopAllotDetailFormList(List<ShopAllotDetailForm> shopAllotDetailFormList) {
        this.shopAllotDetailFormList = shopAllotDetailFormList;
    }

    private List<ShopAllotDetailForm> shopAllotDetailFormList = Lists.newArrayList();
    private List<String> shopAllotDetailIdList = Lists.newArrayList();


}
