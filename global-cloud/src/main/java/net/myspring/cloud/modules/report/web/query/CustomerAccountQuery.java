package net.myspring.cloud.modules.report.web.query;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by liuj on 2017/5/11.
 */
public class CustomerAccountQuery {
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private List<String> nameList;

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public List<String> getNameList() {
        return nameList;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }
}
