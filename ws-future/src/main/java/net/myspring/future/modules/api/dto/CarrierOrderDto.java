package net.myspring.future.modules.api.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.api.domain.CarrierOrder;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;

import java.time.LocalDateTime;

public class CarrierOrderDto extends DataDto<CarrierOrder>{
    private String goodsOrderId;
    private String goodsOrderStatus;
    private String businessId;
    private String areaId;
    private String depotName;
    private String carrierShopName;
    private LocalDateTime shipDate;
    private String code;
    private String status;
    private String formatId;
    @CacheInput(inputKey = "offices",inputInstance = "areaId",outputInstance = "name")
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

    public String getGoodsOrderId() {
        return goodsOrderId;
    }

    public void setGoodsOrderId(String goodsOrderId) {
        this.goodsOrderId = goodsOrderId;
    }

    public String getGoodsOrderStatus() {
        return goodsOrderStatus;
    }

    public void setGoodsOrderStatus(String goodsOrderStatus) {
        this.goodsOrderStatus = goodsOrderStatus;
    }

    public String getBusinessId() {
        if(StringUtils.isBlank(businessId)&&StringUtils.isNotBlank(formatId)){
            return formatId.replace("XK","" );
        }
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getCarrierShopName() {
        return carrierShopName;
    }

    public void setCarrierShopName(String carrieShopName) {
        this.carrierShopName = carrierShopName;
    }

    public LocalDateTime getShipDate() {
        return shipDate;
    }

    public void setShipDate(LocalDateTime shipDate) {
        this.shipDate = shipDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
