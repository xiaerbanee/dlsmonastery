package net.myspring.future.modules.crm.web.form;

import net.myspring.common.constant.CharConstant;
import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.ProductImeSale;
import net.myspring.future.modules.crm.domain.ProductImeUpload;
import net.myspring.util.text.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzm on 2017/4/21.
 */
public class ProductImeSaleForm extends BaseForm<ProductImeSale> {

    private String imeStr;
    private String shopId;
    private String buyer;
    private Integer buyerAge;
    private String buyerSex;
    private String buyerPhone;
    private String buyerGrade;
    private String buyerSchool;

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

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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

    public String getBuyerGrade() {
        return buyerGrade;
    }

    public void setBuyerGrade(String buyerGrade) {
        this.buyerGrade = buyerGrade;
    }

    public String getBuyerSchool() {
        return buyerSchool;
    }

    public void setBuyerSchool(String buyerSchool) {
        this.buyerSchool = buyerSchool;
    }
}
