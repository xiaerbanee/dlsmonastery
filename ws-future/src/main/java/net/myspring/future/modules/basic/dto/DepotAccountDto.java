package net.myspring.future.modules.basic.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;

public class DepotAccountDto extends DataDto<Depot> {

    private String name;
    private String delegateDepotId;
    @CacheInput(inputKey = "depots",inputInstance = "delegateDepotId",outputInstance = "name")
    private String delegateDepotName;
    private String officeId;
    @CacheInput(inputKey = "offices",inputInstance = "officeId",outputInstance = "name")
    private String officeName;
    private String areaId;
    @CacheInput(inputKey = "offices",inputInstance = "areaId",outputInstance = "name")
    private String areaName;
    private String clientId;
    @CacheInput(inputKey = "clients",inputInstance = "clientId",outputInstance = "name")
    private String clientName;
    @CacheInput(inputKey = "clients",inputInstance = "clientId",outputInstance = "outId")
    private String clientOutId;
    private String depotShopAreaType;
    private BigDecimal credit;

    private BigDecimal qcys; //期初应收
    private BigDecimal qmys; //期末应收
    private BigDecimal scbzj; //市场保证金
    private BigDecimal xxbzj; //形象保证金
    private BigDecimal ysjyj; //演示机押金

    public String getDelegateDepotId() {
        return delegateDepotId;
    }

    public void setDelegateDepotId(String delegateDepotId) {
        this.delegateDepotId = delegateDepotId;
    }

    public String getDelegateDepotName() {
        return delegateDepotName;
    }

    public void setDelegateDepotName(String delegateDepotName) {
        this.delegateDepotName = delegateDepotName;
    }

    public String getDepotShopAreaType() {
        return depotShopAreaType;
    }

    public void setDepotShopAreaType(String depotShopAreaType) {
        this.depotShopAreaType = depotShopAreaType;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getClientOutId() {
        return clientOutId;
    }

    public void setClientOutId(String clientOutId) {
        this.clientOutId = clientOutId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public BigDecimal getQcys() {
        return qcys;
    }

    public void setQcys(BigDecimal qcys) {
        this.qcys = qcys;
    }

    public BigDecimal getQmys() {
        return qmys;
    }

    public void setQmys(BigDecimal qmys) {
        this.qmys = qmys;
    }

    public BigDecimal getScbzj() {
        return scbzj;
    }

    public void setScbzj(BigDecimal scbzj) {
        this.scbzj = scbzj;
    }

    public BigDecimal getXxbzj() {
        return xxbzj;
    }

    public void setXxbzj(BigDecimal xxbzj) {
        this.xxbzj = xxbzj;
    }

    public BigDecimal getYsjyj() {
        return ysjyj;
    }

    public void setYsjyj(BigDecimal ysjyj) {
        this.ysjyj = ysjyj;
    }
}
