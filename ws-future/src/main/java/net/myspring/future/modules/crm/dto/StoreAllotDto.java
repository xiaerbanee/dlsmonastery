package net.myspring.future.modules.crm.dto;

import net.myspring.common.constant.CharConstant;
import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.StoreAllot;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class StoreAllotDto extends DataDto<StoreAllot> {


    @CacheInput(inputKey = "depots",inputInstance = "fromStoreId",outputInstance = "name")
    private String fromStoreName;
    private String fromStoreId;

    @CacheInput(inputKey = "depots",inputInstance = "toStoreId",outputInstance = "name")
    private String toStoreName;
    private String toStoreId;


    private String businessId;
    private String outCode;
    private String status;
    private Boolean enabled;
    private String expressOrderId;



    private String expressOrderCodes;
    private List<StoreAllotImeDto> storeAllotImeDtoList = new ArrayList<>();
    private List<StoreAllotDetailDto> storeAllotDetailDtoList = new ArrayList<>();

    public String getFormatId() {
        return StringUtils.getFormatId(businessId, CharConstant.FORMATTER_STORE_ALLOT);
    }

    public String getExpressOrderCodes() {
        return expressOrderCodes;
    }

    public void setExpressOrderCodes(String expressOrderCodes) {
        this.expressOrderCodes = expressOrderCodes;
    }

    public String getExpressOrderId() {
        return expressOrderId;
    }

    public void setExpressOrderId(String expressOrderId) {
        this.expressOrderId = expressOrderId;
    }


    public List<StoreAllotDetailDto> getStoreAllotDetailDtoList() {
        return storeAllotDetailDtoList;
    }

    public void setStoreAllotDetailDtoList(List<StoreAllotDetailDto> storeAllotDetailDtoList) {
        this.storeAllotDetailDtoList = storeAllotDetailDtoList;
    }

    public List<StoreAllotImeDto> getStoreAllotImeDtoList() {
        return storeAllotImeDtoList;
    }

    public void setStoreAllotImeDtoList(List<StoreAllotImeDto> storeAllotImeDtoList) {
        this.storeAllotImeDtoList = storeAllotImeDtoList;
    }



    public String getFromStoreName() {
        return fromStoreName;
    }

    public void setFromStoreName(String fromStoreName) {
        this.fromStoreName = fromStoreName;
    }

    public String getFromStoreId() {
        return fromStoreId;
    }

    public void setFromStoreId(String fromStoreId) {
        this.fromStoreId = fromStoreId;
    }

    public String getToStoreName() {
        return toStoreName;
    }

    public void setToStoreName(String toStoreName) {
        this.toStoreName = toStoreName;
    }

    public String getToStoreId() {
        return toStoreId;
    }

    public void setToStoreId(String toStoreId) {
        this.toStoreId = toStoreId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
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
