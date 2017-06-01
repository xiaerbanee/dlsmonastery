package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by haos on 2017/5/12.
 */
public class ProductImeForReportScoreDto extends DataDto<ProductIme> {

    private BigDecimal retailShopAreaPoint;
    private String retailShopId;
    private String retailShopAssessOfficeId;
    @CacheInput(inputKey = "offices",inputInstance = "retailShopAssessOfficeId",outputInstance = "areaId")
    private String retailShopAreaId;

    private String productTypeId;
    private BigDecimal productTypePrice1;

    public BigDecimal getRetailShopAreaPoint() {
        return retailShopAreaPoint;
    }

    public void setRetailShopAreaPoint(BigDecimal retailShopAreaPoint) {
        this.retailShopAreaPoint = retailShopAreaPoint;
    }

    public String getRetailShopId() {
        return retailShopId;
    }

    public void setRetailShopId(String retailShopId) {
        this.retailShopId = retailShopId;
    }

    public String getRetailShopAssessOfficeId() {
        return retailShopAssessOfficeId;
    }

    public void setRetailShopAssessOfficeId(String retailShopAssessOfficeId) {
        this.retailShopAssessOfficeId = retailShopAssessOfficeId;
    }

    public String getRetailShopAreaId() {
        return retailShopAreaId;
    }

    public void setRetailShopAreaId(String retailShopAreaId) {
        this.retailShopAreaId = retailShopAreaId;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    public BigDecimal getProductTypePrice1() {
        return productTypePrice1;
    }

    public void setProductTypePrice1(BigDecimal productTypePrice1) {
        this.productTypePrice1 = productTypePrice1;
    }
}
