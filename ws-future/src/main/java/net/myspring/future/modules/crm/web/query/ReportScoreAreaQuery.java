package net.myspring.future.modules.crm.web.query;

import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;

public class ReportScoreAreaQuery extends BaseQuery{
    private String scoreDateRange;
    private String areaId;
    private LocalDate scoreDateStart;
    private LocalDate scoreDateEnd;
    private String sort = "month_rank,ASC";


    public String getScoreDateRange() {
        if (StringUtils.isBlank(scoreDateRange)) {
            scoreDateRange=LocalDate.now().minusDays(1)+CharConstant.DATE_RANGE_SPLITTER+LocalDate.now().minusDays(1);
        }
        return scoreDateRange;
    }

    public void setScoreDateRange(String scoreDateRange) {
        this.scoreDateRange = scoreDateRange;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @Override
    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public String getSort() {
        return sort;
    }

    public LocalDate getScoreDateStart() {
        if(StringUtils.isNotBlank(scoreDateRange)) {
            return LocalDateUtils.parse(scoreDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        }else if(scoreDateStart!=null){
            return scoreDateStart;
        }
        return null;
    }

    public void setScoreDateStart(LocalDate scoreDateStart) {
        this.scoreDateStart = scoreDateStart;
    }

    public LocalDate getScoreDateEnd() {
        if(StringUtils.isNotBlank(scoreDateRange)) {
            return LocalDateUtils.parse(scoreDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        }else if(scoreDateStart!=null){
            return scoreDateStart.plusDays(1);
        }
        return null;
    }

    public void setScoreDateEnd(LocalDate scoreDateEnd) {
        this.scoreDateEnd = scoreDateEnd;
    }
}
