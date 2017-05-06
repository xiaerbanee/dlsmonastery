package net.myspring.cloud.modules.input.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import net.myspring.cloud.common.enums.CharEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by admin on 2016/10/21.
 */
public class BatchOtherRecAbleDto {
    // 客户
    private String customer;
    // 日期
    private LocalDate billDate;
    //部门
    private String department;
    // 组织（公司code）
    private String saleOrgId;
    // 总金额
    private BigDecimal amount = BigDecimal.ZERO;
    // 备注
    private String note;

    private List<BatchOtherRecAbleDetailDto> batchOtherRecAbleDetailDtoList = Lists.newArrayList();

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getBillDate() {
        return String.valueOf(billDate.getYear()) + CharEnum.MINUS.getValue() + billDate.getMonthValue() + CharEnum.MINUS.getValue() + billDate.getDayOfMonth();
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getSaleOrgId() {
        return saleOrgId;
    }

    public void setSaleOrgId(String saleOrgId) {
        this.saleOrgId = saleOrgId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @JsonIgnore
    public List<BatchOtherRecAbleDetailDto> getBatchOtherRecAbleDetailDtoList() {
        return batchOtherRecAbleDetailDtoList;
    }

    public void setBatchOtherRecAbleDetailDtoList(List<BatchOtherRecAbleDetailDto> batchOtherRecAbleDetailDtoList) {
        this.batchOtherRecAbleDetailDtoList = batchOtherRecAbleDetailDtoList;
    }


    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

}
