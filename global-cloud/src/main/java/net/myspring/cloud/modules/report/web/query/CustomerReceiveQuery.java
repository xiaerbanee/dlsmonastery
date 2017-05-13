package net.myspring.cloud.modules.report.web.query;

import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by liuj on 2017/5/11.
 */
public class CustomerReceiveQuery {
    private String customerGroup;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private String dateRange;
    private List<String> customerIdList;
    private Boolean queryDetail = false;

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public String getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(String customerGroup) {
        this.customerGroup = customerGroup;
    }

    public LocalDate getDateStart() {
        if(StringUtils.isNotBlank(dateRange)){
            String[] tempParamValues = dateRange.split(CharConstant.DATE_RANGE_SPLITTER);
            dateStart = LocalDateUtils.parse(tempParamValues[0]);
        }else{
            dateStart = LocalDate.now().minusDays(3L);
        }
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        if(StringUtils.isNotBlank(dateRange)){
            String[] tempParamValues = dateRange.split(CharConstant.DATE_RANGE_SPLITTER);
            dateEnd = LocalDateUtils.parse(tempParamValues[1]);
        }else{
            dateEnd = LocalDate.now().minusDays(1L);
        }
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
