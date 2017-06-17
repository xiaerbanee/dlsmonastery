package net.myspring.basic.common.amap.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by liuj on 2016/12/7.
 */
public class RegeocodeBo {
    private String formatted_address;
    @JsonProperty("pois")
    private List<RegeoPoiBo> regeoPoiBoList = Lists.newArrayList();

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public List<RegeoPoiBo> getRegeoPoiBoList() {
        return regeoPoiBoList;
    }

    public void setRegeoPoiBoList(List<RegeoPoiBo> regeoPoiBoList) {
        this.regeoPoiBoList = regeoPoiBoList;
    }
}
