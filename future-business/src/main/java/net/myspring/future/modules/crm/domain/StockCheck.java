package net.myspring.future.modules.crm.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="crm_stock_check")
public class StockCheck extends DataEntity<StockCheck> {
    private String name;
    private String status;
    private Integer version = 0;
    private Integer stockQty;
    private Integer checkQty;
    private Depot depot;
    private String depotId;
    private List<StockCheckDetail> stockCheckDetailList = Lists.newArrayList();
    private List<String> stockCheckDetailIdList = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getStockQty() {
        return stockQty;
    }

    public void setStockQty(Integer stockQty) {
        this.stockQty = stockQty;
    }

    public Integer getCheckQty() {
        return checkQty;
    }

    public void setCheckQty(Integer checkQty) {
        this.checkQty = checkQty;
    }

    public Depot getDepot() {
        return depot;
    }

    public void setDepot(Depot depot) {
        this.depot = depot;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public List<StockCheckDetail> getStockCheckDetailList() {
        return stockCheckDetailList;
    }

    public void setStockCheckDetailList(List<StockCheckDetail> stockCheckDetailList) {
        this.stockCheckDetailList = stockCheckDetailList;
    }

    public List<String> getStockCheckDetailIdList() {
        return stockCheckDetailIdList;
    }

    public void setStockCheckDetailIdList(List<String> stockCheckDetailIdList) {
        this.stockCheckDetailIdList = stockCheckDetailIdList;
    }
}
