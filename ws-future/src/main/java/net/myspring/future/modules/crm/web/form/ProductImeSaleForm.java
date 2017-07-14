package net.myspring.future.modules.crm.web.form;

import net.myspring.common.constant.CharConstant;
import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.ProductImeSale;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;


public class ProductImeSaleForm extends BaseForm<ProductImeSale> {
    private String shopId;
    private String imeStr;
    private String buyer;
    private Integer buyerAge;
    private String buyerSex;
    private String buyerPhone;
    private String productImeSaleDetailStr;
    private List<ProductImeSaleDetailForm> productImeSaleDetailList;

    public List<ProductImeSaleDetailForm> getProductImeSaleDetailList() {
        if(CollectionUtil.isEmpty(productImeSaleDetailList)&&StringUtils.isNotBlank(productImeSaleDetailStr)){
            this.productImeSaleDetailList= ObjectMapperUtils.readValueToBeanList(HtmlUtils.htmlUnescape(productImeSaleDetailStr),ProductImeSaleDetailForm.class);
        }
        return productImeSaleDetailList;
    }

    public String getProductImeSaleDetailStr() {
        return productImeSaleDetailStr;
    }

    public void setProductImeSaleDetailStr(String productImeSaleDetailStr) {
        this.productImeSaleDetailStr = productImeSaleDetailStr;
    }

    public void setProductImeSaleDetailList(List<ProductImeSaleDetailForm> productImeSaleDetailList) {
        this.productImeSaleDetailList = productImeSaleDetailList;
    }

    public List<String> getImeList(){
        if(imeStr == null){
            return new ArrayList<>();
        }
        return  StringUtils.getSplitList(imeStr, CharConstant.ENTER);
    }

    public String getImeStr() {
        return imeStr;
    }

    public void setImeStr(String imeStr) {
        this.imeStr = imeStr;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public Integer getBuyerAge() {
        return buyerAge;
    }

    public void setBuyerAge(Integer buyerAge) {
        this.buyerAge = buyerAge;
    }

    public String getBuyerSex() {
        return buyerSex;
    }

    public void setBuyerSex(String buyerSex) {
        this.buyerSex = buyerSex;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
