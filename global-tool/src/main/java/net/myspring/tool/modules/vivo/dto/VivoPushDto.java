package net.myspring.tool.modules.vivo.dto;

import com.google.common.collect.Lists;
import net.myspring.tool.modules.vivo.domain.*;

import java.util.List;

public class VivoPushDto {
    private List<SCustomers> sCustomersList = Lists.newArrayList();
    private List<SPlantEndProductSale> sPlantEndProductSaleList = Lists.newArrayList();
    private List<SPlantStockDealer> sPlantStockDealerList = Lists.newArrayList();
    private List<SPlantStockStores> sPlantStockStoresList = Lists.newArrayList();
    private List<SPlantStockSupply> sPlantStockSupplyList = Lists.newArrayList();
    private List<SProductItem000> sProductItem000List = Lists.newArrayList();
    private List<SProductItemLend> sProductItemLendList = Lists.newArrayList();
    private List<SProductItemStocks> sProductItemStocksList = Lists.newArrayList();
    private List<SStores> sStoresList = Lists.newArrayList();
    private List<SZones> sZonesList = Lists.newArrayList();

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
