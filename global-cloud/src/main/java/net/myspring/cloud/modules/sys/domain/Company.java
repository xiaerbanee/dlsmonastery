package net.myspring.cloud.modules.sys.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;
import net.myspring.util.collection.CollectionUtil;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="sys_company")
public class Company extends DataEntity<Company> {
    private String name;
    private String type;
    private String cloudUrl;
    private String cloudPostUrl;
    private String cloudUsername;
    private String cloudPassword;
    private String cloudDbid;
    private Integer version = 0;
    private List<CloudProduct> cloudProductList = Lists.newArrayList();
    private List<String> cloudProductIdList = Lists.newArrayList();
    private List<K3cloudGlVoucher> k3cloudGlVoucherList = Lists.newArrayList();
    private List<String> k3cloudGlVoucherIdList = Lists.newArrayList();

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

    public List<CloudProduct> getCloudProductList() {
        return cloudProductList;
    }

    public void setCloudProductList(List<CloudProduct> cloudProductList) {
        this.cloudProductList = cloudProductList;
    }

    public List<String> getCloudProductIdList() {
        if(CollectionUtil.isEmpty(cloudProductIdList) && CollectionUtil.isNotEmpty(cloudProductList)) {
            cloudProductIdList = CollectionUtil.extractToList(cloudProductList,"id");
        }
        return cloudProductIdList;
    }

    public void setCloudProductIdList(List<String> cloudProductIdList) {
        this.cloudProductIdList = cloudProductIdList;
    }

    public List<K3cloudGlVoucher> getK3cloudGlVoucherList() {
        return k3cloudGlVoucherList;
    }

    public void setK3cloudGlVoucherList(List<K3cloudGlVoucher> k3cloudGlVoucherList) {
        this.k3cloudGlVoucherList = k3cloudGlVoucherList;
    }

    public List<String> getK3cloudGlVoucherIdList() {
        if(CollectionUtil.isEmpty(k3cloudGlVoucherIdList) && CollectionUtil.isNotEmpty(k3cloudGlVoucherList)) {
            k3cloudGlVoucherIdList = CollectionUtil.extractToList(k3cloudGlVoucherList,"id");
        }
        return k3cloudGlVoucherIdList;
    }

    public void setK3cloudGlVoucherIdList(List<String> k3cloudGlVoucherIdList) {
        this.k3cloudGlVoucherIdList = k3cloudGlVoucherIdList;
    }
}
