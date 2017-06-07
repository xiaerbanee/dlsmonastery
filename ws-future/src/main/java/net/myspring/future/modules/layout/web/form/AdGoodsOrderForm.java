package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.dto.ExpressOrderDto;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDetailDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by zhangyf on 2017/5/11.
 */
public class AdGoodsOrderForm extends BaseForm<AdGoodsOrder> {

    private String storeId;
    private String outShopId;
    private String shopId;
    private String officeId;
    private String bussinessId;
    private String areaId;
    private BigDecimal amount;
    private String billType;
    private LocalDate billDate = LocalDate.now();
    private String billRemarks;
    private String expressOrderId;

    private ExpressOrderDto expressOrderDto;
    private String employeeId;

    private Boolean pass;
    private String passRemarks;

    private List<AdGoodsOrderDetailDto> adGoodsOrderDetails;

    public String getBussinessId() {
        return bussinessId;
    }

    public void setBussinessId(String bussinessId) {
        this.bussinessId = bussinessId;
    }

    public List<AdGoodsOrderDetailDto> getAdGoodsOrderDetails() {
        return adGoodsOrderDetails;
    }

    public void setAdGoodsOrderDetails(List<AdGoodsOrderDetailDto> adGoodsOrderDetails) {
        this.adGoodsOrderDetails = adGoodsOrderDetails;
    }

    public ExpressOrderDto getExpressOrderDto() {
        return expressOrderDto;
    }

    public void setExpressOrderDto(ExpressOrderDto expressOrderDto) {
        this.expressOrderDto = expressOrderDto;
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

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public Boolean getPass() {
        return pass;
    }

    public void setPass(Boolean pass) {
        this.pass = pass;
    }

    public String getPassRemarks() {
        return passRemarks;
    }

    public void setPassRemarks(String passRemarks) {
        this.passRemarks = passRemarks;
    }
}
