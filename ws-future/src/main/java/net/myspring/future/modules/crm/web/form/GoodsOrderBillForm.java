package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.dto.ExpressCompanyDto;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.dto.ExpressOrderDto;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GoodsOrderBillForm extends DataForm<GoodsOrder> {


    private Boolean sameDate;
    private String storeId;
    private LocalDateTime billDate;
    private String expressCompanyId;
    private Boolean syn;
    private Boolean takeExpress;
    private String expressContator;
    private String expressAddress;
    private String expressMobilePhone;

    private DepotDto shopDto;
    private DepotDto storeDto;
    private GoodsOrderDto goodsOrderDto;
    private ExpressOrderDto expressOrderDto;
    private List<DepotDto> storeList;
    private List<ExpressCompanyDto> expressCompanyList;

    private List<GoodsOrderDetailForm> detailFormList = new ArrayList<>();

    public DepotDto getShopDto() {
        return shopDto;
    }

    public void setShopDto(DepotDto shopDto) {
        this.shopDto = shopDto;
    }

    public DepotDto getStoreDto() {
        return storeDto;
    }

    public void setStoreDto(DepotDto storeDto) {
        this.storeDto = storeDto;
    }

    public Boolean getTakeExpress() {
        return takeExpress;
    }

    public void setTakeExpress(Boolean takeExpress) {
        this.takeExpress = takeExpress;
    }

    public ExpressOrderDto getExpressOrderDto() {
        return expressOrderDto;
    }

    public void setExpressOrderDto(ExpressOrderDto expressOrderDto) {
        this.expressOrderDto = expressOrderDto;
    }

    public List<GoodsOrderDetailForm> getDetailFormList() {
        return detailFormList;
    }

    public void setDetailFormList(List<GoodsOrderDetailForm> detailFormList) {
        this.detailFormList = detailFormList;
    }

    public Boolean getSameDate() {
        return sameDate;
    }

    public void setSameDate(Boolean sameDate) {
        this.sameDate = sameDate;
    }

    public String getExpressContator() {
        return expressContator;
    }

    public void setExpressContator(String expressContator) {
        this.expressContator = expressContator;
    }

    public String getExpressAddress() {
        return expressAddress;
    }

    public void setExpressAddress(String expressAddress) {
        this.expressAddress = expressAddress;
    }

    public String getExpressMobilePhone() {
        return expressMobilePhone;
    }

    public void setExpressMobilePhone(String expressMobilePhone) {
        this.expressMobilePhone = expressMobilePhone;
    }

    public GoodsOrderDto getGoodsOrderDto() {
        return goodsOrderDto;
    }

    public void setGoodsOrderDto(GoodsOrderDto goodsOrderDto) {
        this.goodsOrderDto = goodsOrderDto;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public List<DepotDto> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<DepotDto> storeList) {
        this.storeList = storeList;
    }

    public LocalDateTime getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDateTime billDate) {
        this.billDate = billDate;
    }

    public String getExpressCompanyId() {
        return expressCompanyId;
    }

    public void setExpressCompanyId(String expressCompanyId) {
        this.expressCompanyId = expressCompanyId;
    }

    public List<ExpressCompanyDto> getExpressCompanyList() {
        return expressCompanyList;
    }

    public void setExpressCompanyList(List<ExpressCompanyDto> expressCompanyList) {
        this.expressCompanyList = expressCompanyList;
    }

    public Boolean getSyn() {
        return syn;
    }

    public void setSyn(Boolean syn) {
        this.syn = syn;
    }





}
