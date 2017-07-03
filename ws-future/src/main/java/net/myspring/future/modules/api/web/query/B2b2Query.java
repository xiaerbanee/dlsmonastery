package net.myspring.future.modules.api.web.query;

import com.google.common.collect.Lists;

import java.time.LocalDate;
import java.util.List;

public class B2b2Query {

    private String storeId;
    private String status;
    private LocalDate shipDateStart;
    private LocalDate shipDateEnd;
    private List<String> proxyAreaIdList= Lists.newArrayList();

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getShipDateStart() {
        return shipDateStart;
    }

    public void setShipDateStart(LocalDate shipDateStart) {
        this.shipDateStart = shipDateStart;
    }

    public LocalDate getShipDateEnd() {
        return shipDateEnd;
    }

    public void setShipDateEnd(LocalDate shipDateEnd) {
        this.shipDateEnd = shipDateEnd;
    }

    public List<String> getProxyAreaIdList() {
        return proxyAreaIdList;
    }

    public void setProxyAreaIdList(List<String> proxyAreaIdList) {
        this.proxyAreaIdList = proxyAreaIdList;
    }
}
