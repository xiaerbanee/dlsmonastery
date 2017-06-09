package net.myspring.cloud.modules.input.web.form;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lihx on 2017/6/8.
 */
public class CnJournalForCashForm {
    private LocalDate billDate;
    private String json;
    //附带属性
    private List<String> accountNumberForList;
    private List<String> staffNameList;
    private List<String> departmentNameList;
    private List<String> otherTypeNameList;
    private List<String> expenseTypeNameList;
    //是否为对方关联客户
    private boolean customerForFlag;
    private List<String> customerNameForList;

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

    public List<String> getAccountNumberForList() {
        return accountNumberForList;
    }

    public void setAccountNumberForList(List<String> accountNumberForList) {
        this.accountNumberForList = accountNumberForList;
    }

    public List<String> getStaffNameList() {
        return staffNameList;
    }

    public void setStaffNameList(List<String> staffNameList) {
        this.staffNameList = staffNameList;
    }

    public List<String> getDepartmentNameList() {
        return departmentNameList;
    }

    public void setDepartmentNameList(List<String> departmentNameList) {
        this.departmentNameList = departmentNameList;
    }

    public List<String> getOtherTypeNameList() {
        return otherTypeNameList;
    }

    public void setOtherTypeNameList(List<String> otherTypeNameList) {
        this.otherTypeNameList = otherTypeNameList;
    }

    public List<String> getExpenseTypeNameList() {
        return expenseTypeNameList;
    }

    public void setExpenseTypeNameList(List<String> expenseTypeNameList) {
        this.expenseTypeNameList = expenseTypeNameList;
    }

    public boolean isCustomerForFlag() {
        return customerForFlag;
    }

    public void setCustomerForFlag(boolean customerForFlag) {
        this.customerForFlag = customerForFlag;
    }

    public List<String> getCustomerNameForList() {
        return customerNameForList;
    }

    public void setCustomerNameForList(List<String> customerNameForList) {
        this.customerNameForList = customerNameForList;
    }
}
