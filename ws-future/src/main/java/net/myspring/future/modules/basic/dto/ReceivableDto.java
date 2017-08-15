package net.myspring.future.modules.basic.dto;

import net.myspring.cloud.modules.report.dto.CustomerReceiveDetailDto;
import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.Client;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;
import java.util.List;

public class ReceivableDto extends DataDto<Client> {

    private String name;
    private String outId;
    private String depotOfficeIds;
    private String depotOfficeNames;

    private String depotAreaIds;
    private String depotAreaNames;
    private BigDecimal qcys; //期初应收
    private BigDecimal qmys; //期末应收
    private List<CustomerReceiveDetailDto> customerReceiveDetailDtoList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getDepotOfficeIds() {
        return depotOfficeIds;
    }

    public void setDepotOfficeIds(String depotOfficeIds) {
        this.depotOfficeIds = depotOfficeIds;
    }

    public String getDepotOfficeNames() {
        return depotOfficeNames;
    }

    public void setDepotOfficeNames(String depotOfficeNames) {
        this.depotOfficeNames = depotOfficeNames;
    }

    public String getDepotAreaIds() {
        return depotAreaIds;
    }

    public void setDepotAreaIds(String depotAreaIds) {
        this.depotAreaIds = depotAreaIds;
    }

    public String getDepotAreaNames() {
        return depotAreaNames;
    }

    public void setDepotAreaNames(String depotAreaNames) {
        this.depotAreaNames = depotAreaNames;
    }

    public BigDecimal getQcys() {
        return qcys;
    }

    public void setQcys(BigDecimal qcys) {
        this.qcys = qcys;
    }

    public BigDecimal getQmys() {
        return qmys;
    }

    public void setQmys(BigDecimal qmys) {
        this.qmys = qmys;
    }

    public List<CustomerReceiveDetailDto> getCustomerReceiveDetailDtoList() {
        return customerReceiveDetailDtoList;
    }

    public void setCustomerReceiveDetailDtoList(List<CustomerReceiveDetailDto> customerReceiveDetailDtoList) {
        this.customerReceiveDetailDtoList = customerReceiveDetailDtoList;
    }
}
