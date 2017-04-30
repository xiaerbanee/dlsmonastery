package net.myspring.future.modules.layout.domain;

import net.myspring.future.common.domain.CompanyEntity;
import net.myspring.common.domain.DataEntity;
import net.myspring.future.modules.basic.domain.Depot;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="crm_shop_image")
public class ShopImage extends CompanyEntity<ShopImage> {
    private String shopId;
    private String imageSize;
    private String imageType;
    private String image;
    private Integer version = 0;

    private Depot shop;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Depot getShop() {
        return shop;
    }

    public void setShop(Depot shop) {
        this.shop = shop;
    }
}
