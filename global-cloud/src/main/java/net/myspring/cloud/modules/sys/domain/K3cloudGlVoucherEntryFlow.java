package net.myspring.cloud.modules.sys.domain;


import net.myspring.common.domain.IdEntity;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="api_k3cloud_gl_voucher_entry_flow")
public class K3cloudGlVoucherEntryFlow extends IdEntity<K3cloudGlVoucherEntryFlow> {
    //Json 名称 例如：FDetailID__XXX
    private String name;
    //核算维度名称
    private String value;
    //核算维度值
    private String code;
    private K3cloudGlVoucherEntry k3cloudGlVoucherEntry;
    private String k3cloudGlVoucherEntryId;

    //核算维度名称
    @Transient
    private String flexName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public K3cloudGlVoucherEntry getK3cloudGlVoucherEntry() {
        return k3cloudGlVoucherEntry;
    }

    public void setK3cloudGlVoucherEntry(K3cloudGlVoucherEntry k3cloudGlVoucherEntry) {
        this.k3cloudGlVoucherEntry = k3cloudGlVoucherEntry;
    }

    public String getK3cloudGlVoucherEntryId() {
        if(StringUtils.isBlank(k3cloudGlVoucherEntryId) && k3cloudGlVoucherEntry!=null) {
            k3cloudGlVoucherEntryId = k3cloudGlVoucherEntry.getId();
        }
        return k3cloudGlVoucherEntryId;
    }

    public void setK3cloudGlVoucherEntryId(String k3cloudGlVoucherEntryId) {
        this.k3cloudGlVoucherEntryId = k3cloudGlVoucherEntryId;
    }

    public String getFlexName() {
        return flexName;
    }

    public void setFlexName(String flexName) {
        this.flexName = flexName;
    }

    public String getFlexNumber() {
        return name.replaceFirst("FDetailID__","");
    }
}
