package net.myspring.future.modules.basic.web.query;

import com.google.common.collect.Lists;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.domain.ProductType;

import java.util.List;
import java.util.Map;

/**
 * Created by lihx on 2017/4/19.
 */
public class ProductQuery {
    private String name;
    private String code;
    private String type;
    private String hasIme;
    private String allowBill;
    private String productType;
    private String allowOrder;
    private String outGroupName;
    private String netType;
    private List<String> netTypeList= Lists.newArrayList();
    private List<Product> outGroupNameList=Lists.newArrayList();
    private Map<Boolean,String> boolMap;
    private List<ProductType> productTypeList=Lists.newArrayList();

    public List<ProductType> getProductTypeList() {
        return productTypeList;
    }

    public void setProductTypeList(List<ProductType> productTypeList) {
        this.productTypeList = productTypeList;
    }

    public List<String> getNetTypeList() {
        return netTypeList;
    }

    public void setNetTypeList(List<String> netTypeList) {
        this.netTypeList = netTypeList;
    }

    public List<Product> getOutGroupNameList() {
        return outGroupNameList;
    }

    public void setOutGroupNameList(List<Product> outGroupNameList) {
        this.outGroupNameList = outGroupNameList;
    }

    public Map<Boolean, String> getBoolMap() {
        return boolMap;
    }

    public void setBoolMap(Map<Boolean, String> boolMap) {
        this.boolMap = boolMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHasIme() {
        return hasIme;
    }

    public void setHasIme(String hasIme) {
        this.hasIme = hasIme;
    }

    public String getAllowBill() {
        return allowBill;
    }

    public void setAllowBill(String allowBill) {
        this.allowBill = allowBill;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getAllowOrder() {
        return allowOrder;
    }

    public void setAllowOrder(String allowOrder) {
        this.allowOrder = allowOrder;
    }

    public String getOutGroupName() {
        return outGroupName;
    }

    public void setOutGroupName(String outGroupName) {
        this.outGroupName = outGroupName;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }
}
