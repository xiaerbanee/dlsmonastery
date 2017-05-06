package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.StoreAllotIme;
import net.myspring.util.cahe.annotation.CacheInput;

public class StoreAllotImeDto extends DataDto<StoreAllotIme> {



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

    public String getProductIme() {
        return productIme;
    }

    public void setProductIme(String productIme) {
        this.productIme = productIme;
    }

    public String getProductMeid() {
        return productMeid;
    }

    public void setProductMeid(String productMeid) {
        this.productMeid = productMeid;
    }

    @CacheInput(inputKey = "products",inputInstance = "productId",outputInstance = "name")
    private String productName;
    private String productIme;
    private String productMeid;
    private String productId;




}
