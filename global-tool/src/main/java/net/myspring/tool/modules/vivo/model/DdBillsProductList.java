package net.myspring.tool.modules.vivo.model;

/**
 * 数据库表 : DD_Bills_Product_List
 */
public class DdBillsProductList {
    private String DD_MainID;
    private String ProductID;
    private String ProductNo;
    private DdBills ddBills;
    private String productName;

    public String getDD_MainID() {
        return DD_MainID;
    }

    public void setDD_MainID(String dD_MainID) {
        DD_MainID = dD_MainID;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductNo() {
        return ProductNo;
    }

    public void setProductNo(String productNo) {
        ProductNo = productNo;
    }

    public DdBills getDdBills() {
        return ddBills;
    }

    public void setDdBills(DdBills ddBills) {
        this.ddBills = ddBills;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
