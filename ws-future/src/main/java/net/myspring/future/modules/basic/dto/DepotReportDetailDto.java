package net.myspring.future.modules.basic.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class DepotReportDetailDto {

    private List<DepotReportDto> depotReportList= Lists.newArrayList();
    private Map<String,Integer> productQtyMap= Maps.newHashMap();

    public List<DepotReportDto> getDepotReportList() {
        return depotReportList;
    }

    public void setDepotReportList(List<DepotReportDto> depotReportList) {
        this.depotReportList = depotReportList;
    }

    public Map<String, Integer> getProductQtyMap() {
        return productQtyMap;
    }

    public void setProductQtyMap(Map<String, Integer> productQtyMap) {
        this.productQtyMap = productQtyMap;
    }
}