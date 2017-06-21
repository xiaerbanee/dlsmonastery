package net.myspring.common.response;

import org.springframework.validation.FieldError;

/**
 * Created by liuj on 2016/12/10.
 */
public class RestErrorField {
    private String message;
    private String code;
    private String field;

    public RestErrorField(String message,String code,String field) {
        this.message = message;
        this.code = code;
        this.field = field;
    }

    public RestErrorField(FieldError fieldError,String code) {
        this.message = fieldError.getDefaultMessage();
        this.code = code;
        this.field = fieldError.getField();
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

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
