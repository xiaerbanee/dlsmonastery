package net.myspring.common.domain;

import org.springframework.validation.FieldError;

/**
 * Created by liuj on 2016/12/10.
 */
public class RestError {
    private String field;
    private String code;
    private String defaultMessage;
    private String message;

    public RestError(FieldError fieldError) {
        this.field = fieldError.getField();
        this.code = fieldError.getCode();
        this.defaultMessage = fieldError.getDefaultMessage();
        this.message = fieldError.getDefaultMessage();
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
