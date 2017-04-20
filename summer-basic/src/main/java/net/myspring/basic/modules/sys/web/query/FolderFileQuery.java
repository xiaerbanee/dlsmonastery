package net.myspring.basic.modules.sys.web.query;

import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by lihx on 2017/4/7.
 */
public class FolderFileQuery {
    private LocalDate createdDate;
    private String createdDateBTW;
    private LocalDateTime createdDateStart;
    private LocalDateTime createdDateEnd;


    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedDateBTW() {
        return createdDateBTW;
    }

    public void setCreatedDateBTW(String createdDateBTW) {
        this.createdDateBTW = createdDateBTW;
    }

    public LocalDateTime getCreatedDateStart() {
        if(createdDateStart==null&& StringUtils.isNotBlank(createdDateBTW)){
            String[] tempParamValues = createdDateBTW.split(" - ");
            this.createdDateStart= LocalDateTimeUtils.parse(tempParamValues[0]+ " 00:00:00");
        }
        return createdDateStart;
    }

    public void setCreatedDateStart(LocalDateTime createdDateStart) {
        this.createdDateStart = createdDateStart;
    }

    public LocalDateTime getCreatedDateEnd() {
        if(createdDateEnd==null&& StringUtils.isNotBlank(createdDateBTW)){
            String[] tempParamValues = createdDateBTW.split(" - ");
            this.createdDateEnd= LocalDateTimeUtils.parse(tempParamValues[1]+ " 23:59:59");
        }
        return createdDateEnd;
    }

    public void setCreatedDateEnd(LocalDateTime createdDateEnd) {
        this.createdDateEnd = createdDateEnd;
    }
}
