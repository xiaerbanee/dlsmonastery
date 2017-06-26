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
    private String dateStart;
    private String dateEnd;
    private List<String> customerIdList = Lists.newArrayList();
    private Boolean queryDetail = false;

    public CustomerReceiveQuery(){};

    public CustomerReceiveQuery(String dateStart,String dateEnd,String customerId){
        this.dateStart=dateStart;
        this.dateEnd=dateEnd;
        this.customerIdList=Lists.newArrayList(customerId);
    };

    public String getDateStart() {
       if (StringUtils.isBlank(dateStart)){
           return LocalDateUtils.format(LocalDate.now().minusMonths(1L));
       }else {
           return dateStart;
       }
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        if (StringUtils.isBlank(dateEnd)){
            return LocalDateUtils.format(LocalDate.now());
        }else {
            return dateEnd;
        }
    }

    public void setDateEnd(String dateEnd) {
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
