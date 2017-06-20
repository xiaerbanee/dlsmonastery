package net.myspring.cloud.modules.report.web.query;

import com.google.common.collect.Lists;
import net.myspring.common.query.BaseQuery;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by liuj on 2017/5/11.
 */
public class CustomerReceiveQuery extends BaseQuery{
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private List<String> customerIdList = Lists.newArrayList();
    private Boolean queryDetail = false;

    public LocalDate getDateStart() {
        if(dateStart != null) {
            return dateStart;
        } else {
            return LocalDate.now().minusMonths(1L);
        }
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        if(dateEnd != null) {
            return dateEnd;
        } else {
            return LocalDate.now();
        }
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Boolean getQueryDetail() {
        return queryDetail;
    }

    public void setQueryDetail(Boolean queryDetail) {
        this.queryDetail = queryDetail;
    }

    public List<String> getCustomerIdList() {
        return customerIdList;
    }

    public void setCustomerIdList(List<String> customerIdList) {
        this.customerIdList = customerIdList;
    }
}
