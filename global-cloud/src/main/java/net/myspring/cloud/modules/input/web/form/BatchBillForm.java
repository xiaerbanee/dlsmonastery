package net.myspring.cloud.modules.input.web.form;

import java.util.List;

/**
 * Created by lihx on 2017/4/25.
 */
public class BatchBillForm {
    private String storeNumber;
    private String billDateBTW;
    private List<String> data;

    public String getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(String storeNumber) {
        this.storeNumber = storeNumber;
    }

    public String getBillDateBTW() {
        return billDateBTW;
    }

    public void setBillDateBTW(String billDateBTW) {
        this.billDateBTW = billDateBTW;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

}
