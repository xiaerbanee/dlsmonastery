package net.myspring.cloud.modules.report.web.form;

import net.myspring.cloud.modules.report.dto.ConsignmentDto;

import java.util.List;

/**
 * Created by lihx on 2017/5/5.
 */
public class ConsignmentReportForm {
    private List<ConsignmentDto> consignmentDtoList;
    private String dateRange;

    public List<ConsignmentDto> getConsignmentDtoList() {
        return consignmentDtoList;
    }

    public void setConsignmentDtoList(List<ConsignmentDto> consignmentDtoList) {
        this.consignmentDtoList = consignmentDtoList;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }
}
