package net.myspring.cloud.modules.report.web.query;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by liuj on 2017/5/11.
 */
public class CustomerReceiveQuery {
    private String customerGroup;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private List<String> customerIdList;
    private Boolean queryDetail = false;


    public String getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(String customerGroup) {
        this.customerGroup = customerGroup;
    }

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

    public Boolean getQueryDetail() {
        return queryDetail;
    }

    public void setQueryDetail(Boolean queryDetail) {
        this.queryDetail = queryDetail;
    }
}
