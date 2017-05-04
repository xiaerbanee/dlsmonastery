package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.layout.domain.ShopImage;

/**
 * Created by zhangyf on 2017/5/3.
 */
public class ShopImageForm extends DataForm<ShopImage>{

    private String shopId;
    private String shopName;
    private String imageType;
    private String imageSize;
    private String image;

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

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
