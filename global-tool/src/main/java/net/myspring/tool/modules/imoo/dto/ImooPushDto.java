package net.myspring.tool.modules.imoo.dto;

import com.google.common.collect.Lists;
import net.myspring.tool.modules.vivo.dto.SCustomerDto;

import java.util.List;

public class ImooPushDto {
    private List<SCustomerDto> sCustomerDtoList = Lists.newArrayList();

    public List<SCustomerDto> getsCustomerDtoList() {
        return sCustomerDtoList;
    }

    public void setsCustomerDtoList(List<SCustomerDto> sCustomerDtoList) {
        this.sCustomerDtoList = sCustomerDtoList;
    }
}
