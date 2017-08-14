package net.myspring.cloud.modules.report.web.query;


import java.time.LocalDate;

public class SalProxyReceiveQuery {
    private LocalDate dateStart;
    private LocalDate dateEnd;

    public LocalDate getDateStart() {
        if (dateStart == null){
            dateStart = LocalDate.now().minusMonths(2L);
        }
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        if (dateEnd == null){
            dateEnd = LocalDate.now().minusMonths(1L);
        }
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }
}
