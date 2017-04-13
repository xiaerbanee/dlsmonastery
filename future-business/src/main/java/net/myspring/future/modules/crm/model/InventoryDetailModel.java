package net.myspring.future.modules.crm.model;

import net.myspring.future.modules.crm.domain.ImeSaleReport;
import net.myspring.future.modules.crm.domain.ImeStockReport;

/**
 * Created by admin on 2017/1/5.
 */
public class InventoryDetailModel {
    private String id;
    private String name;
    private ImeSaleReport baokaTotalImeSaleReport=new ImeSaleReport();
    private ImeSaleReport totalImeSaleReport=new ImeSaleReport();
    private ImeStockReport baokaTotalImeStockReport=new ImeStockReport();
    private ImeStockReport totalImeStockReport=new ImeStockReport();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImeSaleReport getBaokaTotalImeSaleReport() {
        return baokaTotalImeSaleReport;
    }

    public void setBaokaTotalImeSaleReport(ImeSaleReport baokaTotalImeSaleReport) {
        this.baokaTotalImeSaleReport = baokaTotalImeSaleReport;
    }

    public ImeSaleReport getTotalImeSaleReport() {
        return totalImeSaleReport;
    }

    public void setTotalImeSaleReport(ImeSaleReport totalImeSaleReport) {
        this.totalImeSaleReport = totalImeSaleReport;
    }

    public ImeStockReport getBaokaTotalImeStockReport() {
        return baokaTotalImeStockReport;
    }

    public void setBaokaTotalImeStockReport(ImeStockReport baokaTotalImeStockReport) {
        this.baokaTotalImeStockReport = baokaTotalImeStockReport;
    }

    public ImeStockReport getTotalImeStockReport() {
        return totalImeStockReport;
    }

    public void setTotalImeStockReport(ImeStockReport totalImeStockReport) {
        this.totalImeStockReport = totalImeStockReport;
    }
}
