package net.myspring.future.modules.crm.dto;

import com.google.common.collect.Lists;

import java.util.List;

public class ExpressOrderPrintDto {

    private List<ExpressOrderPrintDetailDto> expressOrderPrintDetailDtoList = Lists.newArrayList();

    public List<ExpressOrderPrintDetailDto> getExpressOrderPrintDetailDtoList() {
        return expressOrderPrintDetailDtoList;
    }

    public void setExpressOrderPrintDetailDtoList(List<ExpressOrderPrintDetailDto> expressOrderPrintDetailDtoList) {
        this.expressOrderPrintDetailDtoList = expressOrderPrintDetailDtoList;
    }
}
