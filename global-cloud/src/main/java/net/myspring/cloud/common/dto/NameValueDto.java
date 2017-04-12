package net.myspring.cloud.common.dto;

/**
 * Created by liuj on 2017/4/8.
 */
public class NameValueDto {
    private String name;
    private String value;
    private Boolean checked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
