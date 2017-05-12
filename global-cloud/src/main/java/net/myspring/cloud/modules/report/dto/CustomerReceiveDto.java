package net.myspring.cloud.modules.report.dto;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by liuj on 2017/5/11.
 */
public class CustomerReceiveDto {
    //客户分组
    private String customerGroup;
    //客户名称
    private String customerName;
    //期初金额
    private BigDecimal beginShouldGet;
    //期末金额
    private BigDecimal endShouldGet;
    //期间应收
    private BigDecimal shouldGet;
    //期间实收
    private BigDecimal realGet;

    private List<CustomerReceiveDetailDto> customerReceiveDetailDtoList = Lists.newArrayList();

    public String getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(String customerGroup) {
        this.customerGroup = customerGroup;
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
