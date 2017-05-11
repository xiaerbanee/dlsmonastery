package net.myspring.future.modules.layout.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.DataForm;
import net.myspring.future.modules.basic.dto.ShopAdTypeDto;
import net.myspring.future.modules.basic.web.form.ShopAdTypeForm;
import net.myspring.future.modules.layout.domain.ShopAd;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zhangyf on 2017/5/10.
 */
public class ShopAdForm extends DataForm<ShopAd>{
    private String shopId;
    private String shopAdTypeId;
    private BigDecimal length;
    private BigDecimal width;
    private Integer qty;
    private String content;
    private String specialArea;
    private String attachment;
    private BigDecimal price;
    private List<ShopAdTypeDto> shopAdTypeFormList = Lists.newArrayList();
    private String processInstanceId;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopAdTypeId() {
        return shopAdTypeId;
    }

    public void setShopAdTypeId(String shopAdTypeId) {
        this.shopAdTypeId = shopAdTypeId;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSpecialArea() {
        return specialArea;
    }

    public void setSpecialArea(String specialArea) {
        this.specialArea = specialArea;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getArea() {
        if(this.length !=null&&this.width!=null&&this.qty!=null){
            return length.multiply(width).multiply(BigDecimal.valueOf(qty));
        }else{
            return null;
        }
    }

    public List<ShopAdTypeDto> getShopAdTypeFormList() {
        return shopAdTypeFormList;
    }

    public void setShopAdTypeFormList(List<ShopAdTypeDto> shopAdTypeFormList) {
        this.shopAdTypeFormList = shopAdTypeFormList;
    }
}
