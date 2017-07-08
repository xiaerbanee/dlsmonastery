package net.myspring.future.modules.layout.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.layout.domain.ShopImage;
import net.myspring.util.cahe.annotation.CacheInput;

/**
 * Created by zhangyf on 2017/5/3.
 */
public class ShopImageDto extends DataDto<ShopImage>{

    private String shopId;
    private String shopName;
    private String imageSize;
    private String imageType;
    private String image;
    private String officeId;
    @CacheInput(inputKey = "offices", inputInstance = "officeId", outputInstance = "name")
    private String officeName;
    private String areaId;
    @CacheInput(inputKey = "offices", inputInstance = "areaId", outputInstance = "name")
    private String areaName;

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

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
