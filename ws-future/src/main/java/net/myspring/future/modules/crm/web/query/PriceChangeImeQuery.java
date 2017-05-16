package net.myspring.future.modules.crm.web.query;

/**
 * Created by zhangyf on 2017/5/15.
 */
public class PriceChangeImeQuery {
    private String status;
    private String isCheck;
    private String image;
    private String shopId;
    private String ime;
    private String officeId;
    private String priceChangeName;
    private String productId;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getPriceChangeName() {
        return priceChangeName;
    }

    public void setPriceChangeName(String priceChangeName) {
        this.priceChangeName = priceChangeName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
