package net.myspring.cloud.modules.sys.dto;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.dto.DataDto;
import net.myspring.cloud.modules.sys.domain.VoucherEntry;

import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
public class VoucherEntryDto extends DataDto<VoucherEntry> {
    private VoucherDto voucherDto;
    private List<VoucherEntryFlowDto> voucherEntryFlowDtoList = Lists.newArrayList();
    private List<String> voucherEntryFlowIdList = Lists.newArrayList();

    public VoucherDto getVoucherDto() {
        return voucherDto;
    }

    public void setVoucherDto(VoucherDto voucherDto) {
        this.voucherDto = voucherDto;
    }

    public List<VoucherEntryFlowDto> getVoucherEntryFlowDtoList() {
        return voucherEntryFlowDtoList;
    }

    public void setVoucherEntryFlowDtoList(List<VoucherEntryFlowDto> voucherEntryFlowDtoList) {
        this.voucherEntryFlowDtoList = voucherEntryFlowDtoList;
    }

    public List<String> getVoucherEntryFlowIdList() {
        return voucherEntryFlowIdList;
    }

    public void setVoucherEntryFlowIdList(List<String> voucherEntryFlowIdList) {
        this.voucherEntryFlowIdList = voucherEntryFlowIdList;
    }
}
