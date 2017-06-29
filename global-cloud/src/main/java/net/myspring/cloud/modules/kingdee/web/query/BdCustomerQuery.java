package net.myspring.cloud.modules.kingdee.web.query;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.query.BaseQuery;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by liuj on 2017/5/16.
 */
public class BdCustomerQuery extends BaseQuery{
    private String customerGroup;
    private List<String> customerIdList = Lists.newArrayList();
    //应收报表
    private LocalDate dateStart;
    private LocalDate dateEnd;

    public List<String> getCustomerIdList() {
        return customerIdList;
    }

    public void setCustomerIdList(List<String> customerIdList) {
        this.customerIdList = customerIdList;
    }

    public String getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(String customerGroup) {
        this.customerGroup = customerGroup;
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
