package net.myspring.future.modules.crm.domain;


import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;


@Entity
@Table(name="crm_ime_stock_report")
public class ImeStockReport extends DataEntity<ImeStockReport> {
    private LocalDate reportDate;
    private String reportType;
    private Integer saleStock;
    private Integer townStock;
    private Integer countyStock;
    private Integer cityStock;
    private Integer yidongStock;
    private Integer lianxinStock;
    private Integer quanwangtongStock;
    private Integer version = 0;
    private String officeId;
    private Product product;
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

    public Integer getSaleStock() {
        return saleStock;
    }

    public void setSaleStock(Integer saleStock) {
        this.saleStock = saleStock;
    }

    public Integer getTownStock() {
        return townStock;
    }

    public void setTownStock(Integer townStock) {
        this.townStock = townStock;
    }

    public Integer getCountyStock() {
        return countyStock;
    }

    public void setCountyStock(Integer countyStock) {
        this.countyStock = countyStock;
    }

    public Integer getCityStock() {
        return cityStock;
    }

    public void setCityStock(Integer cityStock) {
        this.cityStock = cityStock;
    }

    public Integer getYidongStock() {
        return yidongStock;
    }

    public void setYidongStock(Integer yidongStock) {
        this.yidongStock = yidongStock;
    }

    public Integer getLianxinStock() {
        return lianxinStock;
    }

    public void setLianxinStock(Integer lianxinStock) {
        this.lianxinStock = lianxinStock;
    }

    public Integer getQuanwangtongStock() {
        return quanwangtongStock;
    }

    public void setQuanwangtongStock(Integer quanwangtongStock) {
        this.quanwangtongStock = quanwangtongStock;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
