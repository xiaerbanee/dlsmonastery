package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.util.cahe.annotation.CacheInput;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ProductImeForSaleDto extends DataDto<ProductIme> {

    private String ime;
    private LocalDateTime retailDate;
    private String depotId;
    @CacheInput(inputKey = "depots",inputInstance = "depotId",outputInstance = "name")
    private String depotName;
    @CacheInput(inputKey = "products",inputInstance = "productId",outputInstance = "name")
    private String productName;
    private String productId;
    @CacheInput(inputKey = "depots",inputInstance = "productImeSaleShopId",outputInstance = "name")
    private String productImeSaleShopName;
    private String productImeSaleShopId;
    private String productImeSaleId;
    private String productImeSaleEmployeeId;
    @CacheInput(inputKey = "employees",inputInstance = "productImeSaleEmployeeId",outputInstance = "name")
    private String productImeSaleEmployeeName;
    private LocalDateTime productImeSaleCreatedDate;
    private LocalDateTime productImeUploadCreatedDate;
    private Boolean editable;
    private Boolean fromChain;
    private String depotChainId;
    private String depotDepotStoreId;
    private List<DepotDto> accessChainDepotList = new ArrayList<>();

    public String getDepotDepotStoreId() {
        return depotDepotStoreId;
    }

    public void setDepotDepotStoreId(String depotDepotStoreId) {
        this.depotDepotStoreId = depotDepotStoreId;
    }

    public String getDepotChainId() {
        return depotChainId;
    }

    public void setDepotChainId(String depotChainId) {
        this.depotChainId = depotChainId;
    }

    public String getProductImeSaleId() {
        return productImeSaleId;
    }

    public void setProductImeSaleId(String productImeSaleId) {
        this.productImeSaleId = productImeSaleId;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getFromChain() {
        return fromChain;
    }

    public void setFromChain(Boolean fromChain) {
        this.fromChain = fromChain;
    }

    public List<DepotDto> getAccessChainDepotList() {
        return accessChainDepotList;
    }

    public void setAccessChainDepotList(List<DepotDto> accessChainDepotList) {
        this.accessChainDepotList = accessChainDepotList;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public LocalDateTime getRetailDate() {
        return retailDate;
    }

    public void setRetailDate(LocalDateTime retailDate) {
        this.retailDate = retailDate;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImeSaleShopName() {
        return productImeSaleShopName;
    }

    public void setProductImeSaleShopName(String productImeSaleShopName) {
        this.productImeSaleShopName = productImeSaleShopName;
    }

    public String getProductImeSaleShopId() {
        return productImeSaleShopId;
    }

    public void setProductImeSaleShopId(String productImeSaleShopId) {
        this.productImeSaleShopId = productImeSaleShopId;
    }

    public String getProductImeSaleEmployeeId() {
        return productImeSaleEmployeeId;
    }

    public void setProductImeSaleEmployeeId(String productImeSaleEmployeeId) {
        this.productImeSaleEmployeeId = productImeSaleEmployeeId;
    }

    public String getProductImeSaleEmployeeName() {
        return productImeSaleEmployeeName;
    }

    public void setProductImeSaleEmployeeName(String productImeSaleEmployeeName) {
        this.productImeSaleEmployeeName = productImeSaleEmployeeName;
    }

    public LocalDateTime getProductImeSaleCreatedDate() {
        return productImeSaleCreatedDate;
    }

    public void setProductImeSaleCreatedDate(LocalDateTime productImeSaleCreatedDate) {
        this.productImeSaleCreatedDate = productImeSaleCreatedDate;
    }

    public LocalDateTime getProductImeUploadCreatedDate() {
        return productImeUploadCreatedDate;
    }

    public void setProductImeUploadCreatedDate(LocalDateTime productImeUploadCreatedDate) {
        this.productImeUploadCreatedDate = productImeUploadCreatedDate;
    }
}
