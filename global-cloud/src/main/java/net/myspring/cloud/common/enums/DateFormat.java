package net.myspring.cloud.common.enums;

/**
 * Created by lihx on 2016/12/31.
 */
public enum DateFormat {
    DATE("yyyy-MM-dd"),
    DATE_SHORT("yyyyMMdd"),
    DATE_TIME("yyyy-MM-dd HH:mm:ss"),
    MONTH("yyyy-MM"),
    MONTH_SINGLE("yyyy.M"),
    TIME("HH:mm:ss"),
    YEAR("yyyy"),
    SINGLE_MONTH("M");
    private String value;

    DateFormat(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
