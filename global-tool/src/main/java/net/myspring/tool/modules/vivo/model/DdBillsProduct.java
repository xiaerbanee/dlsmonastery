package net.myspring.tool.modules.vivo.model;

/**
 * 数据库表 ：DD_Bills_Product
 */
public class DdBillsProduct {
    private String DD_BillID;
    private String PrevID;
    private String ProductID;
    private Integer ProductCount;
    private String ProductPrice;
    private String CustomStr1;
    private String CustomStr2;
    private String CustomStr3;
    private String CustomStr4;
    private String CustomStr5;
    private String CustomDec1;
    private String CustomDec2;
    private String CustomDec3;
    private String CustomDec4;
    private String CustomDec5;
    private String Remark;
    private String ApplyCount;
    private String C_FLAG;
    private DdBills ddBills;
    private String productName;

    public String getDD_BillID() {
        return DD_BillID;
    }

    public void setDD_BillID(String dD_BillID) {
        DD_BillID = dD_BillID;
    }

    public String getPrevID() {
        return PrevID;
    }

    public void setPrevID(String prevID) {
        PrevID = prevID;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public Integer getProductCount() {
        return ProductCount;
    }

    public void setProductCount(Integer productCount) {
        ProductCount = productCount;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getCustomStr1() {
        return CustomStr1;
    }

    public void setCustomStr1(String customStr1) {
        CustomStr1 = customStr1;
    }

    public String getCustomStr2() {
        return CustomStr2;
    }

    public void setCustomStr2(String customStr2) {
        CustomStr2 = customStr2;
    }

    public String getCustomStr3() {
        return CustomStr3;
    }

    public void setCustomStr3(String customStr3) {
        CustomStr3 = customStr3;
    }

    public String getCustomStr4() {
        return CustomStr4;
    }

    public void setCustomStr4(String customStr4) {
        CustomStr4 = customStr4;
    }

    public String getCustomStr5() {
        return CustomStr5;
    }

    public void setCustomStr5(String customStr5) {
        CustomStr5 = customStr5;
    }

    public String getCustomDec1() {
        return CustomDec1;
    }

    public void setCustomDec1(String customDec1) {
        CustomDec1 = customDec1;
    }

    public String getCustomDec2() {
        return CustomDec2;
    }

    public void setCustomDec2(String customDec2) {
        CustomDec2 = customDec2;
    }

    public String getCustomDec3() {
        return CustomDec3;
    }

    public void setCustomDec3(String customDec3) {
        CustomDec3 = customDec3;
    }

    public String getCustomDec4() {
        return CustomDec4;
    }

    public void setCustomDec4(String customDec4) {
        CustomDec4 = customDec4;
    }

    public String getCustomDec5() {
        return CustomDec5;
    }

    public void setCustomDec5(String customDec5) {
        CustomDec5 = customDec5;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getApplyCount() {
        return ApplyCount;
    }

    public void setApplyCount(String applyCount) {
        ApplyCount = applyCount;
    }

    public String getC_FLAG() {
        return C_FLAG;
    }

    public void setC_FLAG(String c_FLAG) {
        C_FLAG = c_FLAG;
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
