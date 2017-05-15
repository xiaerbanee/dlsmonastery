package net.myspring.tool.modules.vivo.model;

public class VivoDepotStock {
    private Long depotId;
    private String colorId;
    private Integer qty;

    public VivoDepotStock(Long depotId, String colorId, Long qty) {
        this.depotId = depotId;
        this.colorId = colorId;
        this.qty = qty.intValue();
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }


}
