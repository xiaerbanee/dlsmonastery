package net.myspring.cloud.modules.sys.web.form;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.form.DataForm;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.domain.Voucher;
import net.myspring.cloud.modules.sys.dto.VoucherEntryDto;
import net.myspring.mybatis.form.BaseForm;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lihx on 2017/4/12.
 */
public class VoucherForm extends DataForm<Voucher> {
    //业务日期
    private LocalDate fDate;
    private String createdName;
    private Integer version;
    private String status;
    //返回的单号
    private String outCode;
    private KingdeeBook kingdeeBook;
    private String companyId ;
    private String kingdeeBookId;
    //明细
    private List<VoucherEntryDto> voucherEntryDtoList = Lists.newArrayList();
    private List<String> voucherEntryIdList = Lists.newArrayList();

    public LocalDate getfDate() {
        return fDate;
    }

    public void setfDate(LocalDate fDate) {
        this.fDate = fDate;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public KingdeeBook getKingdeeBook() {
        return kingdeeBook;
    }

    public void setKingdeeBook(KingdeeBook kingdeeBook) {
        this.kingdeeBook = kingdeeBook;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getKingdeeBookId() {
        return kingdeeBookId;
    }

    public void setKingdeeBookId(String kingdeeBookId) {
        this.kingdeeBookId = kingdeeBookId;
    }

    public List<VoucherEntryDto> getVoucherEntryDtoList() {
        return voucherEntryDtoList;
    }

    public void setVoucherEntryDtoList(List<VoucherEntryDto> voucherEntryDtoList) {
        this.voucherEntryDtoList = voucherEntryDtoList;
    }

    public List<String> getVoucherEntryIdList() {
        return voucherEntryIdList;
    }

    public void setVoucherEntryIdList(List<String> voucherEntryIdList) {
        this.voucherEntryIdList = voucherEntryIdList;
    }
}
