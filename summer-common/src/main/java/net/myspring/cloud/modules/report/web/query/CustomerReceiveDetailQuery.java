package net.myspring.cloud.modules.report.web.query;

import com.google.common.collect.Lists;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by liuj on 2017/5/11.
 */
public class CustomerReceiveDetailQuery {
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private List<String> customerIdList = Lists.newArrayList();

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

    public List<String> getCustomerIdList() {
        return customerIdList;
    }

    public void setCustomerIdList(List<String> customerIdList) {
        this.customerIdList = customerIdList;
    }

}
