package net.myspring.future.modules.crm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by admin on 2016/12/21.
 */
public class ResponseStatus {

    @JsonProperty("IsSuccess")
    private Boolean isSuccess;

    @JsonProperty("ErrorCode")
    private Long errorCode;

    @JsonProperty("SuccessEntitys")
    private List<SuccessEntity> successEntitys = Lists.newArrayList();

    @JsonProperty("Errors")
    private List<Error> Errors = Lists.newArrayList();

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public List<SuccessEntity> getSuccessEntitys() {
        return successEntitys;
    }

    public void setSuccessEntitys(List<SuccessEntity> successEntitys) {
        this.successEntitys = successEntitys;
    }

    public Long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }

    public List<Error> getErrors() {
        return Errors;
    }

    public void setErrors(List<Error> errors) {
        Errors = errors;
    }
}
