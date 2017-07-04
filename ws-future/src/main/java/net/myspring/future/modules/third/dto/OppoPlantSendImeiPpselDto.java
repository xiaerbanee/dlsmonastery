package net.myspring.future.modules.third.dto;

import net.myspring.common.dto.IdDto;

import java.time.LocalDateTime;

/**
 * Created by guolm on 2017/5/23.
 */
public class OppoPlantSendImeiPpselDto extends IdDto {
    private String billId;
    private String imei;
    private String meid;
    private LocalDateTime createdTime;
    private String dlsProductId;
    private String imeiState;
    private String remark;
    private String imei2;
    private String lxProductId;
    private String productId;

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMeid() {
        return meid;
    }

    public void setMeid(String meid) {
        this.meid = meid;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getDlsProductId() {
        return dlsProductId;
    }

    public void setDlsProductId(String dlsProductId) {
        this.dlsProductId = dlsProductId;
    }

    public String getImeiState() {
        return imeiState;
    }

    public void setImeiState(String imeiState) {
        this.imeiState = imeiState;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImei2() {
        return imei2;
    }

    public void setImei2(String imei2) {
        this.imei2 = imei2;
    }

    public String getLxProductId() {
        return lxProductId;
    }

    public void setLxProductId(String lxProductId) {
        this.lxProductId = lxProductId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
