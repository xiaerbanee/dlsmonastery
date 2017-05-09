package net.myspring.cloud.modules.input.web.form;

/**
 * Created by lihx on 2017/5/8.
 */
public class BatchDeliveryForm {
    private String data;
    private String departmentNumber;
    private String billDate;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public void setDepartmentNumber(String departmentNumber) {
        this.departmentNumber = departmentNumber;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }
}
