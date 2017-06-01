package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.ImeAllot;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.util.cahe.annotation.CacheInput;

import java.time.LocalDateTime;

/**
 * Created by haos on 2017/5/12.
 */
public class ImeAllotDto extends DataDto<ImeAllot> {


    private String fromDepotId;
    @CacheInput(inputKey = "depots",inputInstance = "fromDepotId",outputInstance = "name")
    private String fromDepotName;
      private String toDepotId;
    @CacheInput(inputKey = "depots",inputInstance = "toDepotId",outputInstance = "name")
      private String toDepotName;
      private String productImeIme;
    @CacheInput(inputKey = "products",inputInstance = "productImeProductId",outputInstance = "name")
      private String productImeProductName;
      private String productImeProductId;
      private String status;
      private Boolean enabled;

    public String getFromDepotId() {
        return fromDepotId;
    }

    public void setFromDepotId(String fromDepotId) {
        this.fromDepotId = fromDepotId;
    }

    public String getFromDepotName() {
        return fromDepotName;
    }

    public void setFromDepotName(String fromDepotName) {
        this.fromDepotName = fromDepotName;
    }

    public String getToDepotId() {
        return toDepotId;
    }

    public void setToDepotId(String toDepotId) {
        this.toDepotId = toDepotId;
    }

    public String getToDepotName() {
        return toDepotName;
    }

    public void setToDepotName(String toDepotName) {
        this.toDepotName = toDepotName;
    }

    public String getProductImeIme() {
        return productImeIme;
    }

    public void setProductImeIme(String productImeIme) {
        this.productImeIme = productImeIme;
    }

    public String getProductImeProductName() {
        return productImeProductName;
    }

    public void setProductImeProductName(String productImeProductName) {
        this.productImeProductName = productImeProductName;
    }

    public String getProductImeProductId() {
        return productImeProductId;
    }

    public void setProductImeProductId(String productImeProductId) {
        this.productImeProductId = productImeProductId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
