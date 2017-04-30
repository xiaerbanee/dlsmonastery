package net.myspring.future.modules.crm.domain;


import com.google.common.collect.Lists;
import net.myspring.future.common.domain.CompanyEntity;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="crm_product_month_price")
public class ProductMonthPrice extends CompanyEntity<ProductMonthPrice> {
    private String month;
    private Integer version = 0;
    private List<ProductMonthPriceDetail> productMonthPriceDetailList = Lists.newArrayList();
    private List<String> productMonthPriceDetailIdList = Lists.newArrayList();

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<ProductMonthPriceDetail> getProductMonthPriceDetailList() {
        return productMonthPriceDetailList;
    }

    public void setProductMonthPriceDetailList(List<ProductMonthPriceDetail> productMonthPriceDetailList) {
        this.productMonthPriceDetailList = productMonthPriceDetailList;
    }

    public List<String> getProductMonthPriceDetailIdList() {
        return productMonthPriceDetailIdList;
    }

    public void setProductMonthPriceDetailIdList(List<String> productMonthPriceDetailIdList) {
        this.productMonthPriceDetailIdList = productMonthPriceDetailIdList;
    }
}
