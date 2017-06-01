package net.myspring.future.modules.crm.web.query;

import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class ReportScoreQuery extends BaseQuery{

    private String scoreDateRange;

    public String getScoreDateRange() {
        return scoreDateRange;
    }

    public void setScoreDateRange(String scoreDateRange) {
        this.scoreDateRange = scoreDateRange;
    }

    public LocalDate getScoreDateStart() {
        if(StringUtils.isNotBlank(scoreDateRange)) {
            return LocalDateUtils.parse(scoreDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getScoreDateEnd() {
        if(StringUtils.isNotBlank(scoreDateRange)) {
            return LocalDateUtils.parse(scoreDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

}
