package net.myspring.cloud.modules.report.web.query;

import com.google.common.collect.Lists;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lihx on 2017/6/30.
 */
public class SupplierPayableDetailQuery {
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private List<String> supplierIdList = Lists.newArrayList();
    //针对专卖店
    private List<String> departmentIdList = Lists.newArrayList();

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
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
