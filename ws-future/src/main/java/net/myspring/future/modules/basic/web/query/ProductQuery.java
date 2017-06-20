package net.myspring.future.modules.basic.web.query;

import com.google.common.collect.Lists;
import net.myspring.future.common.query.BaseQuery;

import java.util.List;

/**
 * Created by lihx on 2017/4/19.
 */
public class ProductQuery extends BaseQuery {
    private String id;
    private String name;  //名称
    private String code;  //编码
    private String type;
    private Boolean hasIme;  //包含串码
    private Boolean allowBill;  //允许开单
    private String productTypeId; //产品型号
    private Boolean allowOrder;  //允许开单
    private String outGroupName;  //产品类型
    private String netType;  //网络制式
    private List<String> ids= Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getHasIme() {
        return hasIme;
    }

    public void setHasIme(Boolean hasIme) {
        this.hasIme = hasIme;
    }

    public Boolean getAllowBill() {
        return allowBill;
    }

    public void setAllowBill(Boolean allowBill) {
        this.allowBill = allowBill;
    }

    public Boolean getAllowOrder() {
        return allowOrder;
    }

    public void setAllowOrder(Boolean allowOrder) {
        this.allowOrder = allowOrder;
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


    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
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

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
