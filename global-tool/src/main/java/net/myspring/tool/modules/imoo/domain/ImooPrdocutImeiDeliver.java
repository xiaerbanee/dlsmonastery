package net.myspring.tool.modules.imoo.domain;


import net.myspring.tool.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="imoo_prdocut_imei_deliver")
public class ImooPrdocutImeiDeliver extends IdEntity<ImooPrdocutImeiDeliver> {
    private String mainagentid;
    private String agentid;
    private String msDes;
    private String msId;
    private String msiItem;
    private String msiItemDes;
    private String msibBarcode;
    private LocalDateTime creationDate;
    private String box;
    private String imei;
    private String companyId;

    public String getMainagentid() {
        return mainagentid;
    }

    public void setMainagentid(String mainagentid) {
        this.mainagentid = mainagentid;
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public String getMsDes() {
        return msDes;
    }

    public void setMsDes(String msDes) {
        this.msDes = msDes;
    }

    public String getMsId() {
        return msId;
    }

    public void setMsId(String msId) {
        this.msId = msId;
    }

    public String getMsiItem() {
        return msiItem;
    }

    public void setMsiItem(String msiItem) {
        this.msiItem = msiItem;
    }

    public String getMsiItemDes() {
        return msiItemDes;
    }

    public void setMsiItemDes(String msiItemDes) {
        this.msiItemDes = msiItemDes;
    }

    public String getMsibBarcode() {
        return msibBarcode;
    }

    public void setMsibBarcode(String msibBarcode) {
        this.msibBarcode = msibBarcode;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
