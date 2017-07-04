package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.GoodsOrderIme;

public class GoodsOrderImeDto extends DataDto<GoodsOrderIme> {

    private String productId;
    private String productName;
    private String productImeIme;
    private String productImeIme2;
    private String productImeMeid;
    private String goodsOrderId;

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
