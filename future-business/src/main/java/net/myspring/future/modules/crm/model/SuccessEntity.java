package net.myspring.future.modules.crm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by admin on 2016/12/21.
 */
public class SuccessEntity {
    @JsonProperty("Id")
    private Long id;

    @JsonProperty("Number")
    private String number;

    @JsonProperty("DIndex")
    private Long dIndex;

    public Long getId() {
        return id;
    }

    public void setId(Long Id) {
        this.id = Id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String Number) {
        this.number = Number;
    }

    public Long getdIndex() {
        return dIndex;
    }

    public void setdIndex(Long DIndex) {
        this.dIndex = DIndex;
    }
}
