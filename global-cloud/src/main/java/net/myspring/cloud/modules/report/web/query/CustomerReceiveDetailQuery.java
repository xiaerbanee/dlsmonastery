package net.myspring.cloud.modules.report.web.query;

import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by liuj on 2017/5/11.
 */
public class CustomerReceiveDetailQuery {
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private String customerId;
    private String dateRange;


    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
