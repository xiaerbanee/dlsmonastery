package net.myspring.future.modules.api.dto;

import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;

import java.time.LocalDateTime;

public class CarrierOrderExportDetailDto {
    private String formatId;
    private String businessId;
    private String depotName;
    private String productName;
    private String ime;
    private String areaName;

    public String getFormatId() {
        if(StringUtils.isBlank(formatId)&&StringUtils.isNotBlank(businessId)){
            this.formatId= IdUtils.getFormatId(businessId,"XK");
        }
        return formatId;
    }

    public void setFormatId(String formatId) {
        this.formatId = formatId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
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

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }
}
