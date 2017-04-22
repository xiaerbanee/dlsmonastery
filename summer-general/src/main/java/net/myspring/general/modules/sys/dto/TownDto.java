package net.myspring.general.modules.sys.dto;

import com.google.common.collect.Lists;
import net.myspring.general.common.dto.DataDto;
import net.myspring.general.common.dto.IdDto;
import net.myspring.general.modules.sys.domain.Town;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
public class TownDto extends IdDto<Town> {
    private String provinceId;
    private String provinceName;
    private String cityId;
    private String cityName;
    private String countyId;
    private String countyName;
    private String townName;
    private String townId;

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyId() {
        return countyId;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getTownId() {
        return townId;
    }

    public void setTownId(String townId) {
        this.townId = townId;
    }
}
