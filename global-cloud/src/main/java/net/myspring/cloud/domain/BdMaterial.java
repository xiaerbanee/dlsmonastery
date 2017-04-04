package net.myspring.cloud.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BdMaterial {
    //id
    private String fmasterId;
    private String fnumber;
    //名称
    private String fname;
    //物料分组
    private Long fmaterialGroup;
    //分组名称
    private String fmaterialGroupName;
    private LocalDateTime fmodifyDate;
    //存货类别
    private String fcateGoryId;

    //一级价
    private BigDecimal price1;

    //广告让利
    private BigDecimal rlPrice;

    public String getFmasterId() {
        return fmasterId;
    }

    public void setFmasterId(String fmasterId) {
        this.fmasterId = fmasterId;
    }

    public String getFnumber() {
        return fnumber;
    }

    public void setFnumber(String fnumber) {
        this.fnumber = fnumber;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public Long getFmaterialGroup() {
        return fmaterialGroup;
    }

    public void setFmaterialGroup(Long fmaterialGroup) {
        this.fmaterialGroup = fmaterialGroup;
    }

    public String getFmaterialGroupName() {
        return fmaterialGroupName;
    }

    public void setFmaterialGroupName(String fmaterialGroupName) {
        this.fmaterialGroupName = fmaterialGroupName;
    }

    public LocalDateTime getFmodifyDate() {
        return fmodifyDate;
    }

    public void setFmodifyDate(LocalDateTime fmodifyDate) {
        this.fmodifyDate = fmodifyDate;
    }

    public String getFcateGoryId() {
        return fcateGoryId;
    }

    public void setFcateGoryId(String fcateGoryId) {
        this.fcateGoryId = fcateGoryId;
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
