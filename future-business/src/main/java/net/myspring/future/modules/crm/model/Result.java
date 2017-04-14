package net.myspring.future.modules.crm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by admin on 2016/12/21.
 */
public class Result {

    @JsonProperty("Id")
    private Long id;

    @JsonProperty("Number")
    private String number;

    @JsonProperty("ResponseStatus")
    private ResponseStatus responseStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }
}
