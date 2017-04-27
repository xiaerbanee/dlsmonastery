package net.myspring.basic.modules.sys.domain;

import net.myspring.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangzm on 2017/4/24.
 */
@Entity
@Table(name="sys_office_business")
public class OfficeBusiness extends CompanyEntity<OfficeBusiness> {
    private String officeId;
    private String businessOfficeId;

    public OfficeBusiness(){}

    public OfficeBusiness(String officeId,String businessOfficeId){
        this.businessOfficeId=businessOfficeId;
        this.officeId=officeId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getBusinessOfficeId() {
        return businessOfficeId;
    }

    public void setBusinessOfficeId(String businessOfficeId) {
        this.businessOfficeId = businessOfficeId;
    }
}
