package net.myspring.cloud.modules.kingdee.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BdMaterial {
    //id
    private String fMasterId;
    private String fNumber;
    //名称
    private String fName;
    //物料分组
    private Long fMaterialGroup;
    //分组名称
    private String fMaterialGroupName;
    private LocalDateTime fModifyDate;
    //存货类别
    private String fCateGoryId;

    //一级价
    private BigDecimal price1;

    //广告让利
    private BigDecimal rlPrice;

    public String getfMasterId() {
        return fMasterId;
    }

    public void setfMasterId(String fMasterId) {
        this.fMasterId = fMasterId;
    }

    public String getfNumber() {
        return fNumber;
    }

    public void setfNumber(String fNumber) {
        this.fNumber = fNumber;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public Long getfMaterialGroup() {
        return fMaterialGroup;
    }

    public void setfMaterialGroup(Long fMaterialGroup) {
        this.fMaterialGroup = fMaterialGroup;
    }

    public String getfMaterialGroupName() {
        return fMaterialGroupName;
    }

    public void setfMaterialGroupName(String fMaterialGroupName) {
        this.fMaterialGroupName = fMaterialGroupName;
    }

    public LocalDateTime getfModifyDate() {
        return fModifyDate;
    }

    public void setfModifyDate(LocalDateTime fModifyDate) {
        this.fModifyDate = fModifyDate;
    }

    public String getfCateGoryId() {
        return fCateGoryId;
    }

    public void setfCateGoryId(String fCateGoryId) {
        this.fCateGoryId = fCateGoryId;
    }

    public BigDecimal getPrice1() {
        return price1;
    }

    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

    public BigDecimal getRlPrice() {
        return rlPrice;
    }

    public void setRlPrice(BigDecimal rlPrice) {
        this.rlPrice = rlPrice;
    }
}
