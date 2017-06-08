package net.myspring.cloud.modules.report.dto;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

/**
 * 客户应收报表
 * Created by liuj on 2017/5/11.
 */
public class CustomerReceiveDto {
    private String customerId;
    private String customerGroupName;
    private String customerName;
    private BigDecimal beginShouldGet;
    private BigDecimal endShouldGet;
    private BigDecimal shouldGet;
    private BigDecimal realGet;

    private List<CustomerReceiveDetailDto> customerReceiveDetailDtoList = Lists.newArrayList();


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerGroupName() {
        return customerGroupName;
    }

    public void setCustomerGroupName(String customerGroupName) {
        this.customerGroupName = customerGroupName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getBeginShouldGet() {
        return beginShouldGet;
    }

    public void setBeginShouldGet(BigDecimal beginShouldGet) {
        this.beginShouldGet = beginShouldGet;
    }

    public BigDecimal getEndShouldGet() {
        return endShouldGet;
    }

    public void setEndShouldGet(BigDecimal endShouldGet) {
        this.endShouldGet = endShouldGet;
    }

    public BigDecimal getShouldGet() {
        return shouldGet;
    }

    public void setShouldGet(BigDecimal shouldGet) {
        this.shouldGet = shouldGet;
    }

    public BigDecimal getRealGet() {
        return realGet;
    }

    public void setRealGet(BigDecimal realGet) {
        this.realGet = realGet;
    }

    public List<CustomerReceiveDetailDto> getCustomerReceiveDetailDtoList() {
        return customerReceiveDetailDtoList;
    }

    public void setCustomerReceiveDetailDtoList(List<CustomerReceiveDetailDto> customerReceiveDetailDtoList) {
        this.customerReceiveDetailDtoList = customerReceiveDetailDtoList;
    }
}
