package net.myspring.cloud.modules.report.web.query;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.query.BaseQuery;

import java.time.LocalDate;
import java.util.List;

public class SupplierPayableQuery{
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private List<String> supplierIdList = Lists.newArrayList();
    //针对专卖店
    private List<String> departmentIdList = Lists.newArrayList();
    private Boolean queryDetail = false;

    public LocalDate getDateStart() {
       if (dateStart == null){
           return LocalDate.now().minusMonths(1L);
       }else {
           return dateStart;
       }
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        if (dateEnd == null){
            return LocalDate.now();
        }else {
            return dateEnd;
        }
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd =  dateEnd;
    }

    public Boolean getQueryDetail() {
        return queryDetail;
    }

    public void setQueryDetail(Boolean queryDetail) {
        this.queryDetail = queryDetail;
    }

    public List<String> getSupplierIdList() {
        return supplierIdList;
    }

    public void setSupplierIdList(List<String> supplierIdList) {
        this.supplierIdList = supplierIdList;
    }

    public List<String> getDepartmentIdList() {
        return departmentIdList;
    }

    public void setDepartmentIdList(List<String> departmentIdList) {
        this.departmentIdList = departmentIdList;
    }
}
