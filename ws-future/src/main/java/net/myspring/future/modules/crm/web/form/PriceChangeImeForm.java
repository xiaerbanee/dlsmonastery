package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.PriceChangeIme;
import net.myspring.future.modules.crm.dto.PriceChangeDto;
import net.myspring.util.cahe.annotation.CacheInput;

import java.util.List;

/**
 * Created by zhangyf on 2017/5/15.
 */
public class PriceChangeImeForm extends BaseForm<PriceChangeIme> {

    private String priceChangeId;
    private String priceChangeName;
    private String productId;
    private String productImeId;
    private String ime;
    private String shopId;

    private Boolean pass;
    private String auditRemarks;

    private String image;

    private List<PriceChangeDto> priceChangeDtos;

    public List<PriceChangeDto> getPriceChangeDtos() {
        return priceChangeDtos;
    }

    public void setPriceChangeDtos(List<PriceChangeDto> priceChangeDtos) {
        this.priceChangeDtos = priceChangeDtos;
    }

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

    public Boolean getPass() {
        return pass;
    }

    public void setPass(Boolean pass) {
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
