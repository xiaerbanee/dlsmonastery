package net.myspring.future.modules.crm.dto;

import com.google.common.collect.Lists;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ExpressOrderPrintDto {
    private String id;
    private String fromDepotName;
    private LocalDate billDate;
    private String toDepotName;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "name")
    private String employeeName;
    private BigDecimal shouldGet;
    private String billRemarks;
    private String formatId;
    private String businessId;
    private String extendType;
    private String printId;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "mobilePhone")
    private String employeePhone;
    private String shipStatus;
    private String contator;
    private String address;
    private String mobilePhone;
    private String remarks;
    private String employeeId;
    private List<ExpressOrderPrintDetailDto> expressOrderPrintDetailDtoList = Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getContator() {
        return contator;
    }

    public void setContator(String contator) {
        this.contator = contator;
    }

    public String getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(String shipStatus) {
        this.shipStatus = shipStatus;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrintId() {
        return printId;
    }

    public void setPrintId(String printId) {
        this.printId = printId;
    }

    public String getExtendType() {
        return extendType;
    }

    public void setExtendType(String extendType) {
        this.extendType = extendType;
    }

    public String getFromDepotName() {
        return fromDepotName;
    }

    public void setFromDepotName(String fromDepotName) {
        this.fromDepotName = fromDepotName;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getToDepotName() {
        return toDepotName;
    }

    public void setToDepotName(String toDepotName) {
        this.toDepotName = toDepotName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public BigDecimal getShouldGet() {
        return shouldGet;
    }

    public void setShouldGet(BigDecimal shouldGet) {
        this.shouldGet = shouldGet;
    }

    public String getBillRemarks() {
        return billRemarks;
    }

    public void setBillRemarks(String billRemarks) {
        this.billRemarks = billRemarks;
    }

    public String getFormatId() {
        return formatId;
    }

    public void setFormatId(String formatId) {
        if(StringUtils.isNotBlank(formatId)) {
            if(StringUtils.isNotBlank(extendType) && formatId != null) {
                if(ExpressOrderTypeEnum.手机订单.name().equals(extendType)) {
                    formatId = StringUtils.getFormatId(formatId, "XK");
                } else if (ExpressOrderTypeEnum.大库调拨.name().equals(extendType)) {
                    formatId = StringUtils.getFormatId(formatId, "CA");
                } else if(ExpressOrderTypeEnum.物料订单.name().equals(extendType)) {
                    formatId = StringUtils.getFormatId(formatId, "AK");
                }
            }
        }
        this.formatId = formatId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public List<ExpressOrderPrintDetailDto> getExpressOrderPrintDetailDtoList() {
        return expressOrderPrintDetailDtoList;
    }

    public void setExpressOrderPrintDetailDtoList(List<ExpressOrderPrintDetailDto> expressOrderPrintDetailDtoList) {
        this.expressOrderPrintDetailDtoList = expressOrderPrintDetailDtoList;
    }
}
