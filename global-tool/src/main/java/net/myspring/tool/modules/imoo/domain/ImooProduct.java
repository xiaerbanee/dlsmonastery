package net.myspring.tool.modules.imoo.domain;


import net.myspring.tool.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="imoo_product_map")
public class ImooProduct extends CompanyEntity<ImooProduct> {
    private String imooPlantBasicProductId;
    private String productId;
    private LocalDateTime createdTime;
    private String version;
    private String companyId;


    public String getImooPlantBasicProductId() {
        return imooPlantBasicProductId;
    }

    public void setImooPlantBasicProductId(String imooPlantBasicProductId) {
        this.imooPlantBasicProductId = imooPlantBasicProductId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
