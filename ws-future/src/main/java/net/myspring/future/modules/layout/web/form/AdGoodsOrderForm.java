package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDetailDto;

import java.util.List;


public class AdGoodsOrderForm extends BaseForm<AdGoodsOrder> {

    private String outShopId;
    private String shopId;
    private String employeeId;
    private String expressCompanyId;
    private String expressOrderAddress;
    private String expressOrderContator;
    private String expressOrderMobilePhone;
    private List<AdGoodsOrderDetailDto> adGoodsOrderDetailList;

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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getExpressCompanyId() {
        return expressCompanyId;
    }

    public void setExpressCompanyId(String expressCompanyId) {
        this.expressCompanyId = expressCompanyId;
    }

    public String getExpressOrderAddress() {
        return expressOrderAddress;
    }

    public void setExpressOrderAddress(String expressOrderAddress) {
        this.expressOrderAddress = expressOrderAddress;
    }

    public String getExpressOrderContator() {
        return expressOrderContator;
    }

    public void setExpressOrderContator(String expressOrderContator) {
        this.expressOrderContator = expressOrderContator;
    }

    public String getExpressOrderMobilePhone() {
        return expressOrderMobilePhone;
    }

    public void setExpressOrderMobilePhone(String expressOrderMobilePhone) {
        this.expressOrderMobilePhone = expressOrderMobilePhone;
    }

    public List<AdGoodsOrderDetailDto> getAdGoodsOrderDetailList() {
        return adGoodsOrderDetailList;
    }

    public void setAdGoodsOrderDetailList(List<AdGoodsOrderDetailDto> adGoodsOrderDetailList) {
        this.adGoodsOrderDetailList = adGoodsOrderDetailList;
    }
}
