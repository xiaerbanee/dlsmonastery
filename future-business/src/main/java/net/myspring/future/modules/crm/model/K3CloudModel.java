package net.myspring.future.modules.crm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by admin on 2016/12/21.
 */
public class K3CloudModel {

    @JsonProperty("Result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
