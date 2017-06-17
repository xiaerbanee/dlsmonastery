package net.myspring.basic.common.gmap.bo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by liuj on 2017-03-02.
 */
public class GeocodeResult {
    @JsonProperty("results")
    private List<Address_component> address_components;
    private String status;

    public List<Address_component> getAddress_components() {
        return address_components;
    }

    public void setAddress_components(List<Address_component> address_components) {
        this.address_components = address_components;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
