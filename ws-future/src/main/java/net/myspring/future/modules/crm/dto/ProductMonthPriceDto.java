package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.ProductMonthPrice;
/**
 * Created by sungm on 2017/6/1.
 */
public class ProductMonthPriceDto extends DataDto<ProductMonthPrice> {
    private String month;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
