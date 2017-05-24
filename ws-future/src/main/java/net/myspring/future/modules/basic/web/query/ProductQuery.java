package net.myspring.future.modules.basic.web.query;

import com.google.common.collect.Lists;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.dto.ProductDto;

import java.util.List;
import java.util.Map;

/**
 * Created by lihx on 2017/4/19.
 */
public class ProductQuery extends BaseQuery {
    private String name;  //名称
    private String code;  //编码
    private String type;
    private String hasIme;  //包含串码
    private String allowBill;  //允许开单
    private String productTypeId; //产品型号
    private String allowOrder;  //允许开单
    private String outGroupName;  //产品类型
    private String netType;  //网络制式
    private List<String> netTypeList = Lists.newArrayList();
    private List<ProductDto> outGroupNameList = Lists.newArrayList();


    public List<String> getNetTypeList() {
        return netTypeList;
    }

    public void setNetTypeList(List<String> netTypeList) {
        this.netTypeList = netTypeList;
    }

    public List<ProductDto> getOutGroupNameList() {
        return outGroupNameList;
    }

    public void setOutGroupNameList(List<ProductDto> outGroupNameList) {
        this.outGroupNameList = outGroupNameList;
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

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
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
