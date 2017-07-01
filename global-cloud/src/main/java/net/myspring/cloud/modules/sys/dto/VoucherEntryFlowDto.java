package net.myspring.cloud.modules.sys.dto;

import net.myspring.cloud.common.dto.DataDto;
import net.myspring.cloud.modules.sys.domain.VoucherEntryFlow;


/**
 * 含核算维度的信息
 * Created by lihx on 2017/4/5.
 */
public class VoucherEntryFlowDto extends DataDto<VoucherEntryFlow> {
    //Json 名称 例如：FDetailID__XXX（编码）
    private String name;
    //核算维度名称
    private String flexName;
    //科目名称
    private String value;
    //科目编码
    private String code;

    private String glVoucherEntryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlexNumber() {
        return getName().replaceFirst("FDetailID__","");
    }

    public String getFlexName() {
        return flexName;
    }

    public void setFlexName(String flexName) {
        this.flexName = flexName;
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
