package net.myspring.common.response;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2016/11/21.
 */
public class RestResponse {
    private Boolean success;
    private String message;
    private String code;
    private List<RestErrorField> errors = Lists.newArrayList();

    private Map<String,Object> extra = Maps.newHashMap();


    public RestResponse(String message,String code) {
        this.success = true;
        this.message = message;
        this.code = code;
    }

    public RestResponse(String message,String code,Boolean success) {
        this.message = message;
        this.code = code;
        this.success = success;
    }


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<RestErrorField> getErrors() {
        return errors;
    }

    public void setErrors(List<RestErrorField> errors) {
        this.errors = errors;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }
}
