package net.myspring.basic.modules.hr.web.query;

import net.myspring.basic.common.query.BaseQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;

public class OfficeChangeQuery extends BaseQuery {
      private String  officeId;
      private String type;
      private String createdDate;
      private LocalDate createdDateStart;
      private LocalDate createdDateEnd;

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String  getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }


    public LocalDate getCreatedDateStart() {
        if(StringUtils.isNotBlank(createdDate)) {
            return LocalDateUtils.parse(createdDate.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else if(createdDateStart!=null){
            return createdDateStart;
        }
        return null;
    }

    public void setCreatedDateStart(LocalDate CreatedDateStart) {
        this.createdDateStart = CreatedDateStart;
    }

    public LocalDate getCreatedDateEnd() {
        if(StringUtils.isNotBlank(createdDate)) {
            return LocalDateUtils.parse(createdDate.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else if(createdDateEnd!=null){
            return createdDateEnd.plusDays(1);
        }
        return null;
    }

    public void setCreatedDateEnd(LocalDate CreatedDateEnd) {
        this.createdDateEnd = CreatedDateEnd;
    }
}
