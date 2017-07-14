package net.myspring.tool.modules.vivo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

//经销商库存表
@Entity
public class SPlantStockDealerM13e00 {
    @Id
    private String id;
    private String companyid;
    private String dealerid;
    private String productid;
    private String createdtime;
    private Integer sumstock;
    private Integer useablestock;
    private Integer bad;
    private String accountdate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "CompanyID")
    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    @Column(name = "DealerID")
    public String getDealerid() {
        return dealerid;
    }

    public void setDealerid(String dealerid) {
        this.dealerid = dealerid;
    }

    @Column(name = "ProductID")
    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    @Column(name = "CreatedTime")
    public String getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }

    @Column(name = "sumstock")
    public Integer getSumstock() {
        return sumstock;
    }

    public void setSumstock(Integer sumstock) {
        this.sumstock = sumstock;
    }

    @Column(name = "useablestock")
    public Integer getUseablestock() {
        return useablestock;
    }

    public void setUseablestock(Integer useablestock) {
        this.useablestock = useablestock;
    }

    @Column(name = "bad")
    public Integer getBad() {
        return bad;
    }

    public void setBad(Integer bad) {
        this.bad = bad;
    }

    @Column(name = "AccountDate")
    public String getAccountdate() {
        return accountdate;
    }

    public void setAccountdate(String accountdate) {
        this.accountdate = accountdate;
    }
}
