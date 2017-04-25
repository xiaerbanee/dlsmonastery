package net.myspring.cloud.modules.input.web.form;

import net.myspring.cloud.common.enums.K3CloudBillTypeEnum;
import net.myspring.cloud.modules.input.domain.BdStock;

import java.util.List;
import java.util.Map;

/**
 * Created by lihx on 2017/4/25.
 */
public class BatchBIllForm {
    private List<String> customerNameList;
    private List<String> productNameList;
    private K3CloudBillTypeEnum[] typeList;
    private List<BdStock> storeList;
    private Map<String,String> materialMap;

    public List<String> getCustomerNameList() {
        return customerNameList;
    }

    public void setCustomerNameList(List<String> customerNameList) {
        this.customerNameList = customerNameList;
    }

    public List<String> getProductNameList() {
        return productNameList;
    }

    public void setProductNameList(List<String> productNameList) {
        this.productNameList = productNameList;
    }

    public K3CloudBillTypeEnum[] getTypeList() {
        return typeList;
    }

    public void setTypeList(K3CloudBillTypeEnum[] typeList) {
        this.typeList = typeList;
    }

    public List<BdStock> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<BdStock> storeList) {
        this.storeList = storeList;
    }

    public Map<String, String> getMaterialMap() {
        return materialMap;
    }

    public void setMaterialMap(Map<String, String> materialMap) {
        this.materialMap = materialMap;
    }
}
