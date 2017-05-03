package net.myspring.basic.modules.hr.domain;

import net.myspring.basic.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangzm on 2017/5/3.
 */
@Entity
@Table(name = "hr_office_leader")
public class OfficeLeader extends CompanyEntity<OfficeLeader> {
    private String officeId;
    private String leaderId;

    public OfficeLeader(){};

    public OfficeLeader(String officeId,String leaderId){
        this.officeId=officeId;
        this.leaderId=leaderId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }
}
