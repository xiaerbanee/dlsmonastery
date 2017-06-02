package net.myspring.future.modules.crm.web.query;

import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.cahe.annotation.CacheInput;

import java.time.LocalDate;

/**
 * Created by haos on 2017/6/1.
 */
public class ReportScoreOfficeQuery extends BaseQuery {

    private LocalDate scoreDate;
    private String officeId;
    private String areaId;

    public LocalDate getScoreDate() {
        return scoreDate;
    }

    public void setScoreDate(LocalDate scoreDate) {
        this.scoreDate = scoreDate;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
