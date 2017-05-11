package net.myspring.cloud.modules.report.dto;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by liuj on 2017/5/11.
 */
public class CustomerAccountDto {
    private String customerName;
    private BigDecimal beginShouldGet;
    private BigDecimal endShouldGet;

    private List<CustomerAccountDetailDto> customerAccountDetailDtoList = Lists.newArrayList();

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

    public List<CustomerAccountDetailDto> getCustomerAccountDetailDtoList() {
        return customerAccountDetailDtoList;
    }

    public void setCustomerAccountDetailDtoList(List<CustomerAccountDetailDto> customerAccountDetailDtoList) {
        this.customerAccountDetailDtoList = customerAccountDetailDtoList;
    }
}
