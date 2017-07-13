package net.myspring.future.modules.layout.web.query;

import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.text.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangyf on 2017/5/11.
 */
public class AdPricesystemChangeQuery extends BaseQuery{
    private String productName;
    private String productCode;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public List<String> getProductCodeLists(){
        if(StringUtils.isNotBlank(productCode)){
            return Arrays.asList(productCode.split(CharConstant.COMMA+CharConstant.VERTICAL_LINE+CharConstant.ENTER+CharConstant.VERTICAL_LINE+CharConstant.SPACE));
        }else{
            return null;
        }
    }
}
