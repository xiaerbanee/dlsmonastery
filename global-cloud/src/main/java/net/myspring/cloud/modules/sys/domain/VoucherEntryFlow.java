package net.myspring.cloud.modules.sys.domain;


import net.myspring.cloud.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 含核算维度的信息
 * Created by lihx on 2017/4/5.
 */
@Entity
@Table(name="sys_gl_voucher_entry_flow")
public class VoucherEntryFlow extends IdEntity<VoucherEntryFlow> {
    //Json 名称 例如：FDetailID__XXX（编码）
    private String name;
    //科目名称
    private String value;
    //科目编码
    private String code;

    private String glVoucherEntryId;

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

    public String getGlVoucherEntryId() {
        return glVoucherEntryId;
    }

    public void setGlVoucherEntryId(String glVoucherEntryId) {
        this.glVoucherEntryId = glVoucherEntryId;
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
