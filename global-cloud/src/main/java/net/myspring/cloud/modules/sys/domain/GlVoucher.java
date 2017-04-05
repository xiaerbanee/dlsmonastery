package net.myspring.cloud.modules.sys.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name="api_gl_voucher")
public class GlVoucher extends DataEntity<GlVoucher> {
    //业务日期
    private LocalDate fdate;
    private String createdName;
    private Integer version = 0;
    private String status;
    //返回的单号
    private String outCode;
    private KingdeeBook kingdeeBook;
    private String companyId ;
    private String kingdeeBookId;
    //明细
    private List<GlVoucherEntry> glVoucherEntryList = Lists.newArrayList();
    private List<String> glVoucherEntryIdList = Lists.newArrayList();

    public LocalDate getFdate() {
        return fdate;
    }

    public void setFdate(LocalDate fdate) {
        this.fdate = fdate;
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

    public List<GlVoucherEntry> getGlVoucherEntryList() {
        return glVoucherEntryList;
    }

    public void setGlVoucherEntryList(List<GlVoucherEntry> glVoucherEntryList) {
        this.glVoucherEntryList = glVoucherEntryList;
    }

    public List<String> getGlVoucherEntryIdList() {
        return glVoucherEntryIdList;
    }

    public void setGlVoucherEntryIdList(List<String> glVoucherEntryIdList) {
        this.glVoucherEntryIdList = glVoucherEntryIdList;
    }
}
