package net.myspring.cloud.modules.input.web.form;

import java.time.LocalDate;

/**
 * Created by lihx on 2017/5/17.
 */
public class StkMisDeliveryForm {
    private String departmentNumber;
    private LocalDate billDate;
    private String json;

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public void setDepartmentNumber(String departmentNumber) {
        this.departmentNumber = departmentNumber;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
