package net.myspring.future.modules.api.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.api.domain.CarrierProduct;
import net.myspring.util.cahe.annotation.CacheInput;

/**
 * Created by wangzm on 2017/7/5.
 */
public class CarrierProductDto extends DataDto<CarrierProduct>{
    private String name;
    private String productId;
    private String mallProductTypeName;
    @CacheInput(inputKey = "products",inputInstance = "productId",outputInstance = "name")
    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

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
