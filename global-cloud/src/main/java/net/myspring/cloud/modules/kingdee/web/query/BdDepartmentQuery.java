package net.myspring.cloud.modules.kingdee.web.query;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.query.BaseQuery;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by liuj on 2017/5/16.
 */
public class BdDepartmentQuery extends BaseQuery{
    private List<String> departmentIdList = Lists.newArrayList();
    //应付报表
    private LocalDate dateStart;
    private LocalDate dateEnd;

    public List<String> getDepartmentIdList() {
        return departmentIdList;
    }

    public void setDepartmentIdList(List<String> departmentIdList) {
        this.departmentIdList = departmentIdList;
    }

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
}
