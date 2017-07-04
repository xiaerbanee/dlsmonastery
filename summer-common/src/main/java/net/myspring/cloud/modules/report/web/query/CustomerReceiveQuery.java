package net.myspring.cloud.modules.report.web.query;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/5/11.
 */
public class CustomerReceiveQuery{

    private  Integer page = 0;

    private Integer size = 50;

    private String sort = "id,DESC";

    private Map<String,Object> extra = Maps.newHashMap();

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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }
}
