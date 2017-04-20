package net.myspring.future.modules.crm.model;

import com.google.common.collect.Lists;
import net.myspring.future.modules.crm.domain.ImeSaleReport;
import net.myspring.future.modules.crm.domain.ImeStockReport;

import java.util.List;

/**
 * Created by admin on 2017/1/5.
 */
public class InventoryMainModel {

    private ImeSaleReport baokaTotalImeSaleReport=new ImeSaleReport();
    private ImeSaleReport totalImeSaleReport=new ImeSaleReport();
    private ImeStockReport baokaTotalImeStockReport=new ImeStockReport();
    private ImeStockReport totalImeStockReport=new ImeStockReport();

    private List<InventoryDetailModel> inventoryDetailModelList = Lists.newArrayList();
    private Boolean hasNext=true;

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

    public List<InventoryDetailModel> getInventoryDetailModelList() {
        return inventoryDetailModelList;
    }

    public void setInventoryDetailModelList(List<InventoryDetailModel> inventoryDetailModelList) {
        this.inventoryDetailModelList = inventoryDetailModelList;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }
}
