package net.myspring.cloud.modules.input.dto;

import com.google.common.collect.Lists;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lihx on 2017/4/25.
 */
public class BatchBillDto {
    // 客户
    private String customerFNumber;
    //仓库
    private String storeFNumber;
    // 日期
    private LocalDate date;
    // 组织（公司code）
    private String saleorgId;
    // 备注
    private String note;
    private String type;
    private String departmentFNumber;

    private List<BatchBillDetailDto> batchBillDetailDtoList = Lists.newArrayList();

    public List<BatchBillDetailDto> getBatchBillDetailDtoList() {
        return batchBillDetailDtoList;
    }

    public void setBatchBillDetailDtoList(List<BatchBillDetailDto> batchBillDetailDtoList) {
        this.batchBillDetailDtoList = batchBillDetailDtoList;
    }

    public String getCustomerFNumber() {
        return customerFNumber;
    }

    public void setCustomerFNumber(String customerFNumber) {
        this.customerFNumber = customerFNumber;
    }

    public String getStoreFNumber() {
        return storeFNumber;
    }

    public void setStoreFNumber(String storeFNumber) {
        this.storeFNumber = storeFNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getSaleorgId() {
        return saleorgId;
    }

    public void setSaleorgId(String saleorgId) {
        this.saleorgId = saleorgId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDepartmentFNumber() {
        return departmentFNumber;
    }

    public void setDepartmentFNumber(String departmentFNumber) {
        this.departmentFNumber = departmentFNumber;
    }
}
