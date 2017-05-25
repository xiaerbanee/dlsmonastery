package net.myspring.future.modules.crm.domain;


import net.myspring.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="crm_stock_check_detail")
public class StockCheckDetail extends IdEntity<StockCheckDetail> {
    private String type;

    private String productImeId;

    private String stockCheckId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductImeId() {
        return productImeId;
    }

    public void setProductImeId(String productImeId) {
        this.productImeId = productImeId;
    }

    public String getStockCheckId() {
        return stockCheckId;
    }

    public void setStockCheckId(String stockCheckId) {
        this.stockCheckId = stockCheckId;
    }
}
