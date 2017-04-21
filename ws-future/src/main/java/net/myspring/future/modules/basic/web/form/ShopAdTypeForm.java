package net.myspring.future.modules.basic.web.form;

import com.google.common.collect.Lists;
import net.myspring.future.common.form.DataForm;
import net.myspring.future.modules.basic.domain.ShopAdType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lihx on 2017/4/19.
 */
public class ShopAdTypeForm extends DataForm<ShopAdType> {
    private String name;
    private String totalPriceType;
    private BigDecimal price;
    private List<String> totalPriceTypeList= Lists.newArrayList();


    public List<String> getTotalPriceTypeList() {
        return totalPriceTypeList;
    }

    public void setTotalPriceTypeList(List<String> totalPriceTypeList) {
        this.totalPriceTypeList = totalPriceTypeList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalPriceType() {
        return totalPriceType;
    }

    public void setTotalPriceType(String totalPriceType) {
        this.totalPriceType = totalPriceType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
