package net.myspring.cloud.modules.report.web.query;

import com.google.common.collect.Lists;
import net.myspring.common.query.BaseQuery;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

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

    public CustomerReceiveQuery(){};

    public CustomerReceiveQuery(LocalDate dateStart,LocalDate dateEnd,String customerId){
        this.dateStart=dateStart;
        this.dateEnd=dateEnd;
        this.customerIdList=Lists.newArrayList(customerId);
    };

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

    public List<String> getCustomerIdList() {
        return customerIdList;
    }

    public void setCustomerIdList(List<String> customerIdList) {
        this.customerIdList = customerIdList;
    }
}
