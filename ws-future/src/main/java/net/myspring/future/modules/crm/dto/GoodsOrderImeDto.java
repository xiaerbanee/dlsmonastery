package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.GoodsOrderIme;

public class GoodsOrderImeDto extends DataDto<GoodsOrderIme> {

    private String productId;
    private String productName;
    private String productImeIme;
    private String productImeMeid;

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
