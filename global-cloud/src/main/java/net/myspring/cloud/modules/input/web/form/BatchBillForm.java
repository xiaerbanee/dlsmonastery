package net.myspring.cloud.modules.input.web.form;

import java.time.LocalDate;

/**
 * Created by liuj on 2017/5/11.
 */
public class BatchBillForm {
    private String storeNumber;
    private LocalDate date;
    private String json;

    public String getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(String storeNumber) {
        this.storeNumber = storeNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
