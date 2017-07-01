package net.myspring.cloud.modules.report.dto;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

/**
 * 供应商-应付
 * Created by lihx on 2017/6/30.
 */
public class SupplierPayableDto {
    private String supplierId;
    private String supplierName;
    private String departmentId;
    private String departmentName;
    private BigDecimal beginAmount;
    private BigDecimal payableAmount;
    private BigDecimal actualPayAmount;
    private BigDecimal endAmount;

    List<SupplierPayableDetailDto> supplierPayableDetailDtoList = Lists.newArrayList();

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public BigDecimal getBeginAmount() {
        return beginAmount;
    }

    public void setBeginAmount(BigDecimal beginAmount) {
        this.beginAmount = beginAmount;
    }

    public BigDecimal getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(BigDecimal payableAmount) {
        this.payableAmount = payableAmount;
    }

    public BigDecimal getActualPayAmount() {
        return actualPayAmount;
    }

    public void setActualPayAmount(BigDecimal actualPayAmount) {
        this.actualPayAmount = actualPayAmount;
    }

    public BigDecimal getEndAmount() {
        return endAmount;
    }

    public void setEndAmount(BigDecimal endAmount) {
        this.endAmount = endAmount;
    }

    public List<SupplierPayableDetailDto> getSupplierPayableDetailDtoList() {
        return supplierPayableDetailDtoList;
    }

    public void setSupplierPayableDetailDtoList(List<SupplierPayableDetailDto> supplierPayableDetailDtoList) {
        this.supplierPayableDetailDtoList = supplierPayableDetailDtoList;
    }
}
