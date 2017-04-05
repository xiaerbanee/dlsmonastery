package net.myspring.cloud.modules.sys.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="sys_company")
public class KingdeeBook extends DataEntity<KingdeeBook> {
    private String name;
    private String type;
    private String cloudUrl;
    private String cloudPostUrl;
    private String cloudUsername;
    private String cloudPassword;
    private String cloudDbid;
    private Integer version = 0;
    private List<Product> productList = Lists.newArrayList();
    private List<String> productIdList = Lists.newArrayList();
    private List<GlVoucher> glVoucherList = Lists.newArrayList();
    private List<String> glVoucherIdList = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCloudUrl() {
        return cloudUrl;
    }

    public void setCloudUrl(String cloudUrl) {
        this.cloudUrl = cloudUrl;
    }

    public String getCloudPostUrl() {
        return cloudPostUrl;
    }

    public void setCloudPostUrl(String cloudPostUrl) {
        this.cloudPostUrl = cloudPostUrl;
    }

    public String getCloudUsername() {
        return cloudUsername;
    }

    public void setCloudUsername(String cloudUsername) {
        this.cloudUsername = cloudUsername;
    }

    public String getCloudPassword() {
        return cloudPassword;
    }

    public void setCloudPassword(String cloudPassword) {
        this.cloudPassword = cloudPassword;
    }

    public String getCloudDbid() {
        return cloudDbid;
    }

    public void setCloudDbid(String cloudDbid) {
        this.cloudDbid = cloudDbid;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<String> getProductIdList() {
        return productIdList;
    }

    public void setProductIdList(List<String> productIdList) {
        this.productIdList = productIdList;
    }

    public List<GlVoucher> getGlVoucherList() {
        return glVoucherList;
    }

    public void setGlVoucherList(List<GlVoucher> glVoucherList) {
        this.glVoucherList = glVoucherList;
    }

    public List<String> getGlVoucherIdList() {
        return glVoucherIdList;
    }

    public void setGlVoucherIdList(List<String> glVoucherIdList) {
        this.glVoucherIdList = glVoucherIdList;
    }
}
