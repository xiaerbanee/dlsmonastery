package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.dto.ProductDto;
import net.myspring.future.modules.crm.domain.PriceChange;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by zhangyf on 2017/5/12.
 */
public class PriceChangeDto extends DataDto<PriceChange> {

    private String name;
    private LocalDate priceChangeDate;
    private LocalDate uploadEndDate;
    private Integer checkPercent;
    private String status;

    private List<ProductDto> productDtos;
}
