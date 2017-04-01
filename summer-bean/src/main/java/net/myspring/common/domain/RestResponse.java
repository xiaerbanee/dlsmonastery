package net.myspring.common.domain;

import com.google.common.collect.Maps;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;

/**
 * Created by liuj on 2016/11/21.
 */
public class RestResponse {
    private Boolean success;
    private String message;
    private BindingResult bindingResult;

    private Map<String,Object> extendMap=Maps.newHashMap();

    public RestResponse(String message){
        this.success=true;
        this.message=message;
    }

    public RestResponse(boolean success, String message, String type){
        this.success=success;
        this.message=message;
    }

    public RestResponse(boolean success, BindingResult bindingResult, String type){
        this.success=success;
        this.bindingResult = bindingResult;
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

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public void setBindingResult(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public Map<String,RestError> getErrors() {
        Map<String,RestError> map = Maps.newHashMap();
        if(bindingResult != null && bindingResult.hasErrors()) {
            for(FieldError fieldError:bindingResult.getFieldErrors()) {
                map.put(fieldError.getField(),new RestError(fieldError));
            }
        }
        return map;
    }

    public Map<String, Object> getExtendMap() {
        return extendMap;
    }

    public void setExtendMap(Map<String, Object> extendMap) {
        this.extendMap = extendMap;
    }
}
