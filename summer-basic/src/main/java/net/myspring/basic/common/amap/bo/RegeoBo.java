package net.myspring.basic.common.amap.bo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by liuj on 2016/12/7.
 */
public class RegeoBo {
    private String status;
    private String info;
    private String infocode;
    @JsonProperty("regeocode")
    private RegeocodeBo regeocodeBo;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public RegeocodeBo getRegeocodeBo() {
        return regeocodeBo;
    }

    public void setRegeocodeBo(RegeocodeBo regeocodeBo) {
        this.regeocodeBo = regeocodeBo;
    }
}
