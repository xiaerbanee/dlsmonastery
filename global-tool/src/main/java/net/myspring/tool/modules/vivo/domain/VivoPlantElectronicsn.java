package net.myspring.tool.modules.vivo.domain;


import net.myspring.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="vivo_plant_electronicsn")
public class VivoPlantElectronicsn extends IdEntity<VivoPlantElectronicsn> {
    private String snImei;
    private LocalDateTime retailDate;
    private LocalDateTime createTime;
    private String companyId;

    public String getSnImei() {
        return snImei;
    }

    public void setSnImei(String snImei) {
        this.snImei = snImei;
    }

    public LocalDateTime getRetailDate() {
        return retailDate;
    }

    public void setRetailDate(LocalDateTime retailDate) {
        this.retailDate = retailDate;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
