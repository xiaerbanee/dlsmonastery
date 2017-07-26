package net.myspring.tool.modules.vivo.dto;

import net.myspring.tool.modules.vivo.domain.SProductItemLend;
import net.myspring.tool.modules.vivo.domain.SStores;

import java.util.List;
import java.util.Map;

public class VivoPushDto {
    private String date;
    private List<SCustomerDto> sCustomerDtoList;
    private Map<String,String> productColorMap;
    private List<SPlantCustomerStockDto> sPlantCustomerStockDtoList;
    private List<SPlantCustomerStockDetailDto> sPlantCustomerStockDetailDtoList;
    private List<SProductItemLend> sProductItemLendList;
    private List<VivoCustomerSaleImeiDto> vivoCustomerSaleImeiDtoList;
    private List<SStores> sStoresList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<SCustomerDto> getsCustomerDtoList() {
        return sCustomerDtoList;
    }

    public void setsCustomerDtoList(List<SCustomerDto> sCustomerDtoList) {
        this.sCustomerDtoList = sCustomerDtoList;
    }

    public Map<String, String> getProductColorMap() {
        return productColorMap;
    }

    public void setProductColorMap(Map<String, String> productColorMap) {
        this.productColorMap = productColorMap;
    }

    public List<SPlantCustomerStockDto> getsPlantCustomerStockDtoList() {
        return sPlantCustomerStockDtoList;
    }

    public void setsPlantCustomerStockDtoList(List<SPlantCustomerStockDto> sPlantCustomerStockDtoList) {
        this.sPlantCustomerStockDtoList = sPlantCustomerStockDtoList;
    }

    public List<SPlantCustomerStockDetailDto> getsPlantCustomerStockDetailDtoList() {
        return sPlantCustomerStockDetailDtoList;
    }

    public void setsPlantCustomerStockDetailDtoList(List<SPlantCustomerStockDetailDto> sPlantCustomerStockDetailDtoList) {
        this.sPlantCustomerStockDetailDtoList = sPlantCustomerStockDetailDtoList;
    }

    public List<SProductItemLend> getsProductItemLendList() {
        return sProductItemLendList;
    }

    public void setsProductItemLendList(List<SProductItemLend> sProductItemLendList) {
        this.sProductItemLendList = sProductItemLendList;
    }

    public List<VivoCustomerSaleImeiDto> getVivoCustomerSaleImeiDtoList() {
        return vivoCustomerSaleImeiDtoList;
    }

    public void setVivoCustomerSaleImeiDtoList(List<VivoCustomerSaleImeiDto> vivoCustomerSaleImeiDtoList) {
        this.vivoCustomerSaleImeiDtoList = vivoCustomerSaleImeiDtoList;
    }

    public List<SStores> getsStoresList() {
        return sStoresList;
    }

    public void setsStoresList(List<SStores> sStoresList) {
        this.sStoresList = sStoresList;
    }
}
