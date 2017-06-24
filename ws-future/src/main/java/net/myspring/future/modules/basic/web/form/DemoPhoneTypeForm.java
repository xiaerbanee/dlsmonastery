package net.myspring.future.modules.basic.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.basic.domain.DemoPhoneType;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.dto.DemoPhoneTypeOfficeDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihx on 2017/4/18.
 */
public class DemoPhoneTypeForm extends BaseForm<DemoPhoneType> {
    private String name;
    private List<String> productTypeIdList=new ArrayList<>();
    private Integer limitQty;
    private LocalDate applyEndDate;
    private LocalDate renewEndDate;
    private List<DemoPhoneTypeOfficeDto> demoPhoneTypeOfficeDtos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getProductTypeIdList() {
        return productTypeIdList;
    }

    public void setProductTypeIdList(List<String> productTypeIdList) {
        this.productTypeIdList = productTypeIdList;
    }

    public Integer getLimitQty() {
        return limitQty;
    }

    public void setLimitQty(Integer limitQty) {
        this.limitQty = limitQty;
    }

    public LocalDate getApplyEndDate() {
        return applyEndDate;
    }

    public void setApplyEndDate(LocalDate applyEndDate) {
        this.applyEndDate = applyEndDate;
    }

    public LocalDate getRenewEndDate() {
        return renewEndDate;
    }

    public void setRenewEndDate(LocalDate renewEndDate) {
        this.renewEndDate = renewEndDate;
    }

    public List<DemoPhoneTypeOfficeDto> getDemoPhoneTypeOfficeDtos() {
        return demoPhoneTypeOfficeDtos;
    }

    public void setDemoPhoneTypeOfficeDtos(List<DemoPhoneTypeOfficeDto> demoPhoneTypeOfficeDtos) {
        this.demoPhoneTypeOfficeDtos = demoPhoneTypeOfficeDtos;
    }
}
