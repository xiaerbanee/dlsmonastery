package net.myspring.future.modules.basic.dto;

import com.google.common.collect.Lists;
import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.DemoPhoneType;
import net.myspring.future.modules.basic.domain.DemoPhoneTypeOffice;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.crm.domain.DemoPhone;
import net.myspring.util.cahe.annotation.CacheInput;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lihx on 2017/4/17.
 */
public class DemoPhoneTypeDto  extends DataDto<DemoPhoneType>{
    private String name;
    private Integer limitQty;
    private String productTypeNames;
    private LocalDate applyEndDate;
    private LocalDate renewEndDate;
    private List<String> productTypeIdList;
    private Boolean locked;
    private Boolean enabled;

    public List<String> getProductTypeIdList() {
        return productTypeIdList;
    }

    public void setProductTypeIdList(List<String> productTypeIdList) {
        this.productTypeIdList = productTypeIdList;
    }

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

    public String getProductTypeNames() {
        return productTypeNames;
    }

    public void setProductTypeNames(String productTypeNames) {
        this.productTypeNames = productTypeNames;
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

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
