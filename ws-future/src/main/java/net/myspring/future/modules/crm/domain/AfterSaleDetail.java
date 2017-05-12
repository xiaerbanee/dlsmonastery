package net.myspring.future.modules.crm.domain;

import net.myspring.future.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liuj on 2017/5/11.
 *
 */
@Entity
@Table(name="crm_after_sale_detail")
public class AfterSaleDetail extends CompanyEntity<AfterSaleDetail>{
    //售后节点：地区录入，总部录入，坏机返厂，好机返库
    private String status;
    //坏机来源
    private String fromDepotId;
    //坏机所在库
    private String toDepotId;
    //替换机串码
    private String replaceProductImeId;
    //替换机型号
    private String replaceProductId;
    //坏机录入金蝶单据
    private  String inputOutCode;
    //替换机录入对应金蝶单据
    private  String replaceOutCode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFromDepotId() {
        return fromDepotId;
    }

    public void setFromDepotId(String fromDepotId) {
        this.fromDepotId = fromDepotId;
    }

    public String getToDepotId() {
        return toDepotId;
    }

    public void setToDepotId(String toDepotId) {
        this.toDepotId = toDepotId;
    }

    public String getReplaceProductImeId() {
        return replaceProductImeId;
    }

    public void setReplaceProductImeId(String replaceProductImeId) {
        this.replaceProductImeId = replaceProductImeId;
    }

    public String getReplaceProductId() {
        return replaceProductId;
    }

    public void setReplaceProductId(String replaceProductId) {
        this.replaceProductId = replaceProductId;
    }

    public String getInputOutCode() {
        return inputOutCode;
    }

    public void setInputOutCode(String inputOutCode) {
        this.inputOutCode = inputOutCode;
    }

    public String getReplaceOutCode() {
        return replaceOutCode;
    }

    public void setReplaceOutCode(String replaceOutCode) {
        this.replaceOutCode = replaceOutCode;
    }
}
