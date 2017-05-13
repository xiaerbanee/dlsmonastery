package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.util.cahe.annotation.CacheInput;

/**
 * Created by wangzm on 2017/5/12.
 */
public class ProductImeDto extends DataDto<ProductIme> {
    private String ime;
    private String depotId;
    private String productId;
    @CacheInput(inputKey = "depots",inputInstance = "depotId",outputInstance = "name")
    private String depotName;
    @CacheInput(inputKey = "products",inputInstance = "productId",outputInstance = "name")
    private String productName;

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
