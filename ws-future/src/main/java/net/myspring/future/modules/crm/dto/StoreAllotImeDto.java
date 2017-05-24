package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.common.constant.FormatterConstant;
import net.myspring.future.modules.crm.domain.StoreAllotIme;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.IdUtils;

import java.time.LocalDateTime;

public class StoreAllotImeDto extends DataDto<StoreAllotIme> {

    private String productId;
    @CacheInput(inputKey = "products",inputInstance = "productId",outputInstance = "name")
    private String productName;
    private String productImeIme;
    private String productImeMeid;

    private String storeAllotBusinessId;
    private String storeAllotOutCode;
    private String storeAllotFromStoreId;
    @CacheInput(inputKey = "depots",inputInstance = "storeAllotFromStoreId",outputInstance = "name")
    private String storeAllotFromStoreName;
    private String storeAllotToStoreId;
    @CacheInput(inputKey = "depots",inputInstance = "storeAllotToStoreId",outputInstance = "name")
    private String storeAllotToStoreName;
    private String storeAllotToStoreOfficeId;
    private String storeAllotToStoreAreaName;

    private LocalDateTime storeAllotBillDate;

    public String getStoreAllotToStoreName() {
        return storeAllotToStoreName;
    }

    public void setStoreAllotToStoreName(String storeAllotToStoreName) {
        this.storeAllotToStoreName = storeAllotToStoreName;
    }

    public String getStoreAllotFromStoreName() {
        return storeAllotFromStoreName;
    }

    public void setStoreAllotFromStoreName(String storeAllotFromStoreName) {
        this.storeAllotFromStoreName = storeAllotFromStoreName;
    }

    public String getStoreAllotToStoreAreaName() {
        return storeAllotToStoreAreaName;
    }

    public void setStoreAllotToStoreAreaName(String storeAllotToStoreAreaName) {
        this.storeAllotToStoreAreaName = storeAllotToStoreAreaName;
    }

    public String getStoreAllotFormatId() {
        return IdUtils.getFormatId(storeAllotBusinessId, FormatterConstant.STORE_ALLOT);
    }

    public String getStoreAllotBusinessId() {
        return storeAllotBusinessId;
    }

    public void setStoreAllotBusinessId(String storeAllotBusinessId) {
        this.storeAllotBusinessId = storeAllotBusinessId;
    }

    public String getStoreAllotOutCode() {
        return storeAllotOutCode;
    }

    public void setStoreAllotOutCode(String storeAllotOutCode) {
        this.storeAllotOutCode = storeAllotOutCode;
    }

    public String getStoreAllotFromStoreId() {
        return storeAllotFromStoreId;
    }

    public void setStoreAllotFromStoreId(String storeAllotFromStoreId) {
        this.storeAllotFromStoreId = storeAllotFromStoreId;
    }

    public String getStoreAllotToStoreId() {
        return storeAllotToStoreId;
    }

    public void setStoreAllotToStoreId(String storeAllotToStoreId) {
        this.storeAllotToStoreId = storeAllotToStoreId;
    }

    public String getStoreAllotToStoreOfficeId() {
        return storeAllotToStoreOfficeId;
    }

    public void setStoreAllotToStoreOfficeId(String storeAllotToStoreOfficeId) {
        this.storeAllotToStoreOfficeId = storeAllotToStoreOfficeId;
    }

    public LocalDateTime getStoreAllotBillDate() {
        return storeAllotBillDate;
    }

    public void setStoreAllotBillDate(LocalDateTime storeAllotBillDate) {
        this.storeAllotBillDate = storeAllotBillDate;
    }

    public String getProductImeIme() {
        return productImeIme;
    }

    public void setProductImeIme(String productImeIme) {
        this.productImeIme = productImeIme;
    }

    public String getProductImeMeid() {
        return productImeMeid;
    }

    public void setProductImeMeid(String productImeMeid) {
        this.productImeMeid = productImeMeid;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }





}
