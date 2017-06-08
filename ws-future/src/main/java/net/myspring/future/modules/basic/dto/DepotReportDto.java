package net.myspring.future.modules.basic.dto;

public class DepotReportDto {
    private String depotId;
    private Integer qty;

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
