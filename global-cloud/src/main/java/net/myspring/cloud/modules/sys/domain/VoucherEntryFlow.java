package net.myspring.cloud.modules.sys.domain;


import net.myspring.cloud.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="sys_gl_voucher_entry_flow")
public class VoucherEntryFlow extends IdEntity<VoucherEntryFlow> {
    //Json 名称 例如：FDetailID__XXX
    private String name;
    //核算维度名称
    private String value;
    //核算维度值
    private String code;

    private String glVoucherEntryId;

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
}
