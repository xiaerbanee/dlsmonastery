package net.myspring.future.modules.crm.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="crm_pricesystem")
public class Pricesystem extends DataEntity<Pricesystem> {
    private String name;
    private Integer sort;
    private Integer version = 0;
    private List<Depot> depotList = Lists.newArrayList();
    private List<String> depotIdList = Lists.newArrayList();
    private List<PricesystemChange> pricesystemChangeList = Lists.newArrayList();
    private List<String> pricesystemChangeIdList = Lists.newArrayList();
    private List<PricesystemDetail> pricesystemDetailList = Lists.newArrayList();
    private List<String> pricesystemDetailIdList = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<Depot> getDepotList() {
        return depotList;
    }

    public void setDepotList(List<Depot> depotList) {
        this.depotList = depotList;
    }

    public List<String> getDepotIdList() {
        return depotIdList;
    }

    public void setDepotIdList(List<String> depotIdList) {
        this.depotIdList = depotIdList;
    }

    public List<PricesystemChange> getPricesystemChangeList() {
        return pricesystemChangeList;
    }

    public void setPricesystemChangeList(List<PricesystemChange> pricesystemChangeList) {
        this.pricesystemChangeList = pricesystemChangeList;
    }

    public List<String> getPricesystemChangeIdList() {
        return pricesystemChangeIdList;
    }

    public void setPricesystemChangeIdList(List<String> pricesystemChangeIdList) {
        this.pricesystemChangeIdList = pricesystemChangeIdList;
    }

    public List<PricesystemDetail> getPricesystemDetailList() {
        return pricesystemDetailList;
    }

    public void setPricesystemDetailList(List<PricesystemDetail> pricesystemDetailList) {
        this.pricesystemDetailList = pricesystemDetailList;
    }

    public List<String> getPricesystemDetailIdList() {
        return pricesystemDetailIdList;
    }

    public void setPricesystemDetailIdList(List<String> pricesystemDetailIdList) {
        this.pricesystemDetailIdList = pricesystemDetailIdList;
    }
}
