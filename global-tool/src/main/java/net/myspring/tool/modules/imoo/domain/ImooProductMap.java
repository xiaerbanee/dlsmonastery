package net.myspring.tool.modules.imoo.domain;


import net.myspring.tool.common.domain.CompanyEntity;
import net.myspring.tool.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="imoo_product_map")
public class ImooProductMap extends CompanyEntity<ImooProductMap> {
    private Integer version;
    private ImooPlantBasicProduct imooPlantBasicProduct;
    private String imooPlantBasicProductId;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ImooPlantBasicProduct getImooPlantBasicProduct() {
        return imooPlantBasicProduct;
    }

    public void setImooPlantBasicProduct(ImooPlantBasicProduct imooPlantBasicProduct) {
        this.imooPlantBasicProduct = imooPlantBasicProduct;
    }

    public String getImooPlantBasicProductId() {
        return imooPlantBasicProductId;
    }

    public void setImooPlantBasicProductId(String imooPlantBasicProductId) {
        this.imooPlantBasicProductId = imooPlantBasicProductId;
    }
}
