package net.myspring.future.modules.crm.domain;


import net.myspring.future.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;


@Entity
@Table(name="crm_ime_sale_report")
public class ImeSaleReport extends CompanyEntity<ImeSaleReport> {
    private LocalDate reportDate;
    private String reportType;
    private Integer saleQty;
    private Integer townQty;
    private Integer countyQty;
    private Integer cityQty;
    private Integer yidongQty;
    private Integer lianxinQty;
    private Integer quanwangtongQty;
    private Integer version = 0;
    private String officeId;

    private String productId;


    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public Integer getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(Integer saleQty) {
        this.saleQty = saleQty;
    }

    public Integer getTownQty() {
        return townQty;
    }

    public void setTownQty(Integer townQty) {
        this.townQty = townQty;
    }

    public Integer getCountyQty() {
        return countyQty;
    }

    public void setCountyQty(Integer countyQty) {
        this.countyQty = countyQty;
    }

    public Integer getCityQty() {
        return cityQty;
    }

    public void setCityQty(Integer cityQty) {
        this.cityQty = cityQty;
    }

    public Integer getYidongQty() {
        return yidongQty;
    }

    public void setYidongQty(Integer yidongQty) {
        this.yidongQty = yidongQty;
    }

    public Integer getLianxinQty() {
        return lianxinQty;
    }

    public void setLianxinQty(Integer lianxinQty) {
        this.lianxinQty = lianxinQty;
    }

    public Integer getQuanwangtongQty() {
        return quanwangtongQty;
    }

    public void setQuanwangtongQty(Integer quanwangtongQty) {
        this.quanwangtongQty = quanwangtongQty;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

}
