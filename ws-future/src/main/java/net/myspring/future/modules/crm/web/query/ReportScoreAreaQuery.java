package net.myspring.future.modules.crm.web.query;

import net.myspring.future.common.query.BaseQuery;

import java.time.LocalDate;

public class ReportScoreAreaQuery extends BaseQuery{
    private LocalDate scoreDate;
    private String areaId;

    public LocalDate getScoreDate() {
        return scoreDate;
    }

    public void setScoreDate(LocalDate scoreDate) {
        this.scoreDate = scoreDate;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
