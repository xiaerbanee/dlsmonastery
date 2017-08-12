package net.myspring.cloud.modules.report.web.query;


import java.time.LocalDate;

public class ConsignmentWZQuery {
    private LocalDate dateStart;
    private LocalDate dateEnd;

    public LocalDate getDateStart() {
        if (dateStart == null){
            dateStart = LocalDate.now().minusDays(7L);
        }
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        if (dateEnd == null){
            dateEnd = LocalDate.now();
        }
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }
}
