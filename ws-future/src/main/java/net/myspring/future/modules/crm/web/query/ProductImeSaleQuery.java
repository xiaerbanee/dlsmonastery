package net.myspring.future.modules.crm.web.query;

import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.List;


public class ProductImeSaleQuery extends BaseQuery{

    private String employeeId;
    private String createdDateRange;
    private Boolean isBack;
    private String shopName;
    private String ime;
    private String imeReverse;
    private LocalDate createdDateStart;
    private LocalDate createdDateEnd;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCreatedDateRange() {
        return createdDateRange;
    }

    public void setCreatedDateRange(String createdDateRange) {
        this.createdDateRange = createdDateRange;
    }

    public Boolean getIsBack() {
        return isBack;
    }

    public void setIsBack(Boolean isBack) {
        this.isBack = isBack;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getImeReverse(){
        return StringUtils.reverse(ime);
    }

    public void setImeReverse(String imeReverse) {
        this.imeReverse = imeReverse;
    }

    public LocalDate getCreatedDateStart() {
        if(StringUtils.isNotBlank(createdDateRange)) {
            return LocalDateUtils.parse(createdDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else if(createdDateStart!=null){
            return createdDateStart;
        }
        return null;
    }

    public void setCreatedDateStart(LocalDate createdDateStart) {
        this.createdDateStart = createdDateStart;
    }

    public LocalDate getCreatedDateEnd() {
        if(StringUtils.isNotBlank(createdDateRange)) {
            return LocalDateUtils.parse(createdDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else if(createdDateEnd!=null){
            return createdDateEnd.plusDays(1);
        }
        return null;
    }

    public void setCreatedDateEnd(LocalDate createdDateEnd) {
        this.createdDateEnd = createdDateEnd;
    }
}
