package net.myspring.tool.modules.vivo.dto;

import net.myspring.tool.modules.vivo.domain.*;

import java.util.List;

public class VivoPushDto {
    private List<SCustomers> sCustomersList;
    private List<SPlantEndProductSale> sPlantEndProductSaleList;
    private List<SPlantStockDealer> sPlantStockDealerList;
    private List<SPlantStockStores> sPlantStockStoresList;
    private List<SPlantStockSupply> sPlantStockSupplyList;
    private List<SProductItem000> sProductItem000List;
    private List<SProductItemLend> sProductItemLendList;
    private List<SProductItemStocks> sProductItemStocksList;
    private List<SStores> sStoresList;
    private List<SZones> sZonesList;

    public List<SCustomers> getsCustomersList() {
        return sCustomersList;
    }

    public void setsCustomersList(List<SCustomers> sCustomersList) {
        this.sCustomersList = sCustomersList;
    }

    public List<SPlantEndProductSale> getsPlantEndProductSaleList() {
        return sPlantEndProductSaleList;
    }

    public void setsPlantEndProductSaleList(List<SPlantEndProductSale> sPlantEndProductSaleList) {
        this.sPlantEndProductSaleList = sPlantEndProductSaleList;
    }

    public List<SPlantStockDealer> getsPlantStockDealerList() {
        return sPlantStockDealerList;
    }

    public void setsPlantStockDealerList(List<SPlantStockDealer> sPlantStockDealerList) {
        this.sPlantStockDealerList = sPlantStockDealerList;
    }

    public List<SPlantStockStores> getsPlantStockStoresList() {
        return sPlantStockStoresList;
    }

    public void setsPlantStockStoresList(List<SPlantStockStores> sPlantStockStoresList) {
        this.sPlantStockStoresList = sPlantStockStoresList;
    }

    public List<SPlantStockSupply> getsPlantStockSupplyList() {
        return sPlantStockSupplyList;
    }

    public void setsPlantStockSupplyList(List<SPlantStockSupply> sPlantStockSupplyList) {
        this.sPlantStockSupplyList = sPlantStockSupplyList;
    }

    public List<SProductItem000> getsProductItem000List() {
        return sProductItem000List;
    }

    public void setsProductItem000List(List<SProductItem000> sProductItem000List) {
        this.sProductItem000List = sProductItem000List;
    }

    public List<SProductItemLend> getsProductItemLendList() {
        return sProductItemLendList;
    }

    public void setsProductItemLendList(List<SProductItemLend> sProductItemLendList) {
        this.sProductItemLendList = sProductItemLendList;
    }

    public List<SProductItemStocks> getsProductItemStocksList() {
        return sProductItemStocksList;
    }

    public void setsProductItemStocksList(List<SProductItemStocks> sProductItemStocksList) {
        this.sProductItemStocksList = sProductItemStocksList;
    }

    public List<SStores> getsStoresList() {
        return sStoresList;
    }

    public void setsStoresList(List<SStores> sStoresList) {
        this.sStoresList = sStoresList;
    }

    public List<SZones> getsZonesList() {
        return sZonesList;
    }

    public void setsZonesList(List<SZones> sZonesList) {
        this.sZonesList = sZonesList;
    }
}
