package net.myspring.future.modules.crm.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.dto.NameValueDto;
import net.myspring.future.modules.crm.domain.ProductIme;

import java.util.List;
import java.util.Map;

public class ProductImeSearchResultDto {

    private List<String> notExists = Lists.newArrayList();
    private List<ProductIme> productImeList = Lists.newArrayList();

    private Map<String,Integer> productQtyMap = Maps.newLinkedHashMap();
    private List<NameValueDto> productQty = Lists.newArrayList();

    public List<String> getNotExists() {
        return notExists;
    }

    public void setNotExists(List<String> notExists) {
        this.notExists = notExists;
    }

    public List<ProductIme> getProductImeList() {
        return productImeList;
    }

    public void setProductImeList(List<ProductIme> productImeList) {
        this.productImeList = productImeList;
    }

    public Map<String, Integer> getProductQtyMap() {
        return productQtyMap;
    }

    public void setProductQtyMap(Map<String, Integer> productQtyMap) {
        this.productQtyMap = productQtyMap;
    }

    public List<NameValueDto> getProductQty() {
        return productQty;
    }

    public void setProductQty(List<NameValueDto> productQty) {
        this.productQty = productQty;
    }
}
