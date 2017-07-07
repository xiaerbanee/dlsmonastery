package net.myspring.cloud.modules.sys.domain;


import net.myspring.cloud.common.domain.CompanyEntity;
import net.myspring.cloud.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="sys_product")
public class Product extends CompanyEntity<Product> {
    //物料名称
    private String name;
    //物料编码
    private String code;
    //维护的价格
    private BigDecimal price1;
    //物料ID
    private String outId;
    //
    private String returnOutId;
    //ModifyDate
    private LocalDateTime outDate;
    private Integer version = 0;
    private String kingdeeBookId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPrice1() {
        return price1;
    }

    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getReturnOutId() {
        return returnOutId;
    }

    public void setReturnOutId(String returnOutId) {
        this.returnOutId = returnOutId;
    }

    public LocalDateTime getOutDate() {
        return outDate;
    }

    public void setOutDate(LocalDateTime outDate) {
        this.outDate = outDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getKingdeeBookId() {
        return kingdeeBookId;
    }

    public void setKingdeeBookId(String kingdeeBookId) {
        this.kingdeeBookId = kingdeeBookId;
    }

}
