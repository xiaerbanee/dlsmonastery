package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.common.constant.FormatterConstant;
import net.myspring.future.modules.crm.domain.GoodsOrderIme;
import net.myspring.util.text.IdUtils;

public class GoodsOrderImeDto extends DataDto<GoodsOrderIme> {

    private String productId;
    private String productName;
    private String productImeIme;
    private String productImeIme2;
    private String productImeMeid;
    private String goodsOrderId;
    private String goodsOrderStatus;
    private String goodsOrderStoreName;
    private String goodsOrderShopName;
    private String goodsOrderBusinessId;
    private String goodsOrderCreatedDate;

    public String getGoodsOrderFormatId(){
        return IdUtils.getFormatId(goodsOrderBusinessId, FormatterConstant.GOODS_ORDER);
    }

    public String getGoodsOrderStatus() {
        return goodsOrderStatus;
    }

    public void setGoodsOrderStatus(String goodsOrderStatus) {
        this.goodsOrderStatus = goodsOrderStatus;
    }

    public String getGoodsOrderStoreName() {
        return goodsOrderStoreName;
    }

    public void setGoodsOrderStoreName(String goodsOrderStoreName) {
        this.goodsOrderStoreName = goodsOrderStoreName;
    }

    public String getGoodsOrderShopName() {
        return goodsOrderShopName;
    }

    public void setGoodsOrderShopName(String goodsOrderShopName) {
        this.goodsOrderShopName = goodsOrderShopName;
    }

    public String getGoodsOrderBusinessId() {
        return goodsOrderBusinessId;
    }

    public void setGoodsOrderBusinessId(String goodsOrderBusinessId) {
        this.goodsOrderBusinessId = goodsOrderBusinessId;
    }

    public String getGoodsOrderCreatedDate() {
        return goodsOrderCreatedDate;
    }

    public void setGoodsOrderCreatedDate(String goodsOrderCreatedDate) {
        this.goodsOrderCreatedDate = goodsOrderCreatedDate;
    }

    public String getGoodsOrderId() {
        return goodsOrderId;
    }

    public void setGoodsOrderId(String goodsOrderId) {
        this.goodsOrderId = goodsOrderId;
    }

    public String getProductImeIme2() {
        return productImeIme2;
    }

    public void setProductImeIme2(String productImeIme2) {
        this.productImeIme2 = productImeIme2;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImeIme() {
        return productImeIme;
    }

    public void setProductImeIme(String productImeIme) {
        this.productImeIme = productImeIme;
    }

    public String getProductImeMeid() {
        return productImeMeid;
    }

    public void setProductImeMeid(String productImeMeid) {
        this.productImeMeid = productImeMeid;
    }
}
