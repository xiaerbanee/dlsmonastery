package net.myspring.cloud.modules.sys.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.domain.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by liuj on 2017/4/5.
 */
public class ProductDto extends DataDto<Product> {
    private String name;
    private String code;
    private BigDecimal price1;
    private String outId;
    private String returnOutId;
    private LocalDateTime outDate;
    private Integer version;
    private KingdeeBook kingdeeBook;
    private String kingdeeBookId;
    private String companyId;

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

    public KingdeeBook getKingdeeBook() {
        return kingdeeBook;
    }

    public void setKingdeeBook(KingdeeBook kingdeeBook) {
        this.kingdeeBook = kingdeeBook;
    }

    public String getKingdeeBookId() {
        return kingdeeBookId;
    }

    public void setKingdeeBookId(String kingdeeBookId) {
        this.kingdeeBookId = kingdeeBookId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
