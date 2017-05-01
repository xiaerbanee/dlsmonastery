package net.myspring.util.cahe;

import java.lang.reflect.Field;

/**
 * Created by liuj on 2017/3/31.
 */
public class CacheInputField {
    private String inputKey;
    private String inputInstance;
    private String outputInstance;

    private Field inputField;
    private Field outputField;
    private Boolean isCollection;

    public String getInputKey() {
        return inputKey;
    }

    public void setInputKey(String inputKey) {
        this.inputKey = inputKey;
    }

    public String getInputInstance() {
        return inputInstance;
    }

    public void setInputInstance(String inputInstance) {
        this.inputInstance = inputInstance;
    }

    public String getOutputInstance() {
        return outputInstance;
    }

    public void setOutputInstance(String outputInstance) {
        this.outputInstance = outputInstance;
    }

    public Field getInputField() {
        return inputField;
    }

    public void setInputField(Field inputField) {
        this.inputField = inputField;
    }

    public Field getOutputField() {
        return outputField;
    }

    public void setOutputField(Field outputField) {
        this.outputField = outputField;
    }

    public Boolean getCollection() {
        return isCollection;
    }

    public void setCollection(Boolean collection) {
        isCollection = collection;
    }
}
