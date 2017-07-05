package net.myspring.future.modules.api.dto;

/**
 * Created by wangzm on 2017/7/5.
 */
public class CarrierProductDto {
    private String name;
    private String productId;

    private String mallProductTypeName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getMallProductTypeName() {
        return mallProductTypeName;
    }

    public void setMallProductTypeName(String mallProductTypeName) {
        this.mallProductTypeName = mallProductTypeName;
    }
}
