package net.myspring.cloud.modules.sys.dto;

import net.myspring.cloud.common.dto.DataDto;
import net.myspring.cloud.modules.sys.domain.VoucherEntryFlow;


/**
 * Created by lihx on 2017/4/5.
 */
public class VoucherEntryFlowDto extends DataDto<VoucherEntryFlow> {
    private String name;
    //核算维度名称
    private String flexName;

    private VoucherEntryDto voucherEntryDto;

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

    public VoucherEntryDto getVoucherEntryDto() {
        return voucherEntryDto;
    }

    public void setVoucherEntryDto(VoucherEntryDto voucherEntryDto) {
        this.voucherEntryDto = voucherEntryDto;
    }
}
