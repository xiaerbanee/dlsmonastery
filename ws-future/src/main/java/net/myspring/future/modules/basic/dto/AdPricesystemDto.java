package net.myspring.future.modules.basic.dto;

import com.google.common.collect.Lists;
import net.myspring.future.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.AdPricesystem;
import net.myspring.future.modules.basic.domain.AdPricesystemDetail;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.layout.domain.AdPricesystemChange;

import java.util.List;

/**
 * Created by lihx on 2017/4/17.
 */
public class AdPricesystemDto extends DataDto<AdPricesystem>{
    private String name;
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
