package net.myspring.cloud.modules.sys.domain;

import net.myspring.cloud.common.domain.CompanyEntity;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;


@Entity
@Table(name="sys_gl_voucher")
public class Voucher extends CompanyEntity<Voucher> {
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
}
