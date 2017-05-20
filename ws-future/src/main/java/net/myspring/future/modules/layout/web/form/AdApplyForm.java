package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.common.enums.BillTypeEnum;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.dto.ProductDto;
import net.myspring.future.modules.layout.domain.AdApply;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wangzm on 2017/4/21.
 */
public class AdApplyForm extends DataForm<AdApply> {
    private String shopId;
    private String billType = BillTypeEnum.POP.name();

    private List<Integer> applyQty;
    private List<String> billTypes;
    private List<ProductDto> productDtos;


    public List<Integer> getApplyQty() {
        return applyQty;
    }

    public void setApplyQty(List<Integer> applyQty) {
        this.applyQty = applyQty;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public List<String> getBillTypes() {
        return billTypes;
    }

    public void setBillTypes(List<String> billTypes) {
        this.billTypes = billTypes;
    }

    public List<ProductDto> getProductDtos() {
        return productDtos;
    }

    public void setProductDtos(List<ProductDto> productDtos) {
        this.productDtos = productDtos;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

}
