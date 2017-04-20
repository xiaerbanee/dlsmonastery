package net.myspring.future.modules.basic.dto;

import com.google.common.collect.Lists;
import net.myspring.future.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.DemoPhoneType;
import net.myspring.future.modules.basic.domain.DemoPhoneTypeOffice;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.crm.domain.DemoPhone;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lihx on 2017/4/17.
 */
public class DemoPhoneTypeDto  extends DataDto<DemoPhoneType>{
    private String name;
    private Integer limitQty;
    private LocalDate applyEndDate;
    private LocalDate renewEndDate;
    private Integer version;
    private List<DemoPhone> demoPhoneList = Lists.newArrayList();
    private List<String> demoPhoneIdList = Lists.newArrayList();
    private List<DemoPhoneTypeOffice> demoPhoneTypeOfficeList = Lists.newArrayList();
    private List<String> demoPhoneTypeOfficeIdList = Lists.newArrayList();
    private List<ProductType> productTypeList = Lists.newArrayList();
    private List<String> productTypeIdList = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<DemoPhone> getDemoPhoneList() {
        return demoPhoneList;
    }

    public void setDemoPhoneList(List<DemoPhone> demoPhoneList) {
        this.demoPhoneList = demoPhoneList;
    }

    public List<String> getDemoPhoneIdList() {
        return demoPhoneIdList;
    }

    public void setDemoPhoneIdList(List<String> demoPhoneIdList) {
        this.demoPhoneIdList = demoPhoneIdList;
    }

    public List<DemoPhoneTypeOffice> getDemoPhoneTypeOfficeList() {
        return demoPhoneTypeOfficeList;
    }

    public void setDemoPhoneTypeOfficeList(List<DemoPhoneTypeOffice> demoPhoneTypeOfficeList) {
        this.demoPhoneTypeOfficeList = demoPhoneTypeOfficeList;
    }

    public List<String> getDemoPhoneTypeOfficeIdList() {
        return demoPhoneTypeOfficeIdList;
    }

    public void setDemoPhoneTypeOfficeIdList(List<String> demoPhoneTypeOfficeIdList) {
        this.demoPhoneTypeOfficeIdList = demoPhoneTypeOfficeIdList;
    }

    public List<ProductType> getProductTypeList() {
        return productTypeList;
    }

    public void setProductTypeList(List<ProductType> productTypeList) {
        this.productTypeList = productTypeList;
    }

    public List<String> getProductTypeIdList() {
        return productTypeIdList;
    }

    public void setProductTypeIdList(List<String> productTypeIdList) {
        this.productTypeIdList = productTypeIdList;
    }
}
