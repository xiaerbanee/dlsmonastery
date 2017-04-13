package net.myspring.future.modules.crm.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;


@Entity
@Table(name="crm_ad_pricesystem")
public class AdPricesystem extends DataEntity<AdPricesystem> {
    private String name;
    private Integer version = 0;
    private List<AdPricesystemChange> adPricesystemChangeList = Lists.newArrayList();
    private List<String> adPricesystemChangeIdList = Lists.newArrayList();
    private List<AdPricesystemDetail> adPricesystemDetailList = Lists.newArrayList();
    private List<String> adPricesystemDetailIdList = Lists.newArrayList();
    private List<String> districtIdList = Lists.newArrayList();
    private List<Depot> depotList = Lists.newArrayList();
    private List<String> depotIdList = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<AdPricesystemChange> getAdPricesystemChangeList() {
        return adPricesystemChangeList;
    }

    public void setAdPricesystemChangeList(List<AdPricesystemChange> adPricesystemChangeList) {
        this.adPricesystemChangeList = adPricesystemChangeList;
    }

    public List<String> getAdPricesystemChangeIdList() {
        return adPricesystemChangeIdList;
    }

    public void setAdPricesystemChangeIdList(List<String> adPricesystemChangeIdList) {
        this.adPricesystemChangeIdList = adPricesystemChangeIdList;
    }

    public List<AdPricesystemDetail> getAdPricesystemDetailList() {
        return adPricesystemDetailList;
    }

    public void setAdPricesystemDetailList(List<AdPricesystemDetail> adPricesystemDetailList) {
        this.adPricesystemDetailList = adPricesystemDetailList;
    }

    public List<String> getAdPricesystemDetailIdList() {
        return adPricesystemDetailIdList;
    }

    public void setAdPricesystemDetailIdList(List<String> adPricesystemDetailIdList) {
        this.adPricesystemDetailIdList = adPricesystemDetailIdList;
    }

    public List<String> getDistrictIdList() {
        return districtIdList;
    }

    public void setDistrictIdList(List<String> districtIdList) {
        this.districtIdList = districtIdList;
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
}
