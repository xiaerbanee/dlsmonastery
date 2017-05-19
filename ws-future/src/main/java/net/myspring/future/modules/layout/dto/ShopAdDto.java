package net.myspring.future.modules.layout.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.layout.domain.ShopAd;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.IdUtils;

import java.math.BigDecimal;

/**
 * Created by zhangyf on 2017/5/10.
 */
public class ShopAdDto extends DataDto<ShopAd>{

    private String shopId;
    @CacheInput(inputKey = "depots", inputInstance = "shopId", outputInstance = "name")
    private String shopName;
    private String officeId;
    @CacheInput(inputKey = "offices", inputInstance = "officeId", outputInstance = "name")
    private String officeName;
    private Boolean specialArea;
    private String shopAdTypeId;
    @CacheInput(inputKey = "shopAdTypes", inputInstance = "shopAdTypeId", outputInstance = "name")
    private String shopAdTypeName;
    private BigDecimal length;
    private BigDecimal width;
    private Integer qty;
    private BigDecimal price;
    private String content;
    private String processStatus;
    private String processPositionId;



    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public Boolean getSpecialArea() {
        return specialArea;
    }

    public void setSpecialArea(Boolean specialArea) {
        this.specialArea = specialArea;
    }

    public String getShopAdTypeId() {
        return shopAdTypeId;
    }

    public void setShopAdTypeId(String shopAdTypeId) {
        this.shopAdTypeId = shopAdTypeId;
    }

    public String getShopAdTypeName() {
        return shopAdTypeName;
    }

    public void setShopAdTypeName(String shopAdTypeName) {
        this.shopAdTypeName = shopAdTypeName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getProcessPositionId() {
        return processPositionId;
    }

    public void setProcessPositionId(String processPositionId) {
        this.processPositionId = processPositionId;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getLengthWidthQty() {
        if(this.length !=null&&this.width!=null&&this.qty!=null){
            return String.valueOf(length)+"*"+String.valueOf(width)+"*"+String.valueOf(qty);
        }else{
            return null;
        }
    }

    public BigDecimal getArea() {
        if(this.length !=null&&this.width!=null&&this.qty!=null){
            return length.multiply(width).multiply(BigDecimal.valueOf(qty));
        }else{
            return null;
        }
    }

    public String getFormatId(){
        if(getId()!=null){
            return IdUtils.getFormatId(getId(),"AD");
        }else {
            return null;
        }
    }

    public Boolean getIsAuditable(){
        if(RequestUtils.getRequestEntity().getPositionId().equals(getProcessPositionId())|| RequestUtils.getAccountId().equalsIgnoreCase("1")){
            return true;
        }else {
            return false;
        }
    }

    public Boolean getIsEditable(){
        if (RequestUtils.getAccountId().equals(getCreatedBy())|| RequestUtils.getAccountId().equalsIgnoreCase("1")){
            return true;
        }else {
            return false;
        }
    }
}
