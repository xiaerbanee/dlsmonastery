package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.crm.domain.PriceChangeIme;
import net.myspring.util.cahe.annotation.CacheInput;

/**
 * Created by zhangyf on 2017/5/15.
 */
public class PriceChangeImeForm extends DataForm<PriceChangeIme> {

    private String priceChangeId;
    private String priceChangeName;
    private String productId;
    @CacheInput(inputKey = "products",inputInstance = "productId",outputInstance = "name")
    private String productName;
    private String productImeId;
    private String ime;
    private String shopId;
    @CacheInput(inputKey = "depots",inputInstance = "shopId",outputInstance = "name")
    private String shopName;

    private String pass;
    private String auditRemarks;

    private String image;

    public String getPriceChangeId() {
        return priceChangeId;
    }

    public void setPriceChangeId(String priceChangeId) {
        this.priceChangeId = priceChangeId;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImeId() {
        return productImeId;
    }

    public void setProductImeId(String productImeId) {
        this.productImeId = productImeId;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getAuditRemarks() {
        return auditRemarks;
    }

    public void setAuditRemarks(String auditRemarks) {
        this.auditRemarks = auditRemarks;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
