package net.myspring.future.modules.basic.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.DemoPhoneType;
import net.myspring.future.modules.basic.domain.DemoPhoneTypeOffice;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;

/**
 * Created by lihx on 2017/4/17.
 */
public class DemoPhoneTypeOfficeDto extends DataDto<DemoPhoneTypeOffice> {
    private Integer qty;
    private Integer realQty;
    private String demoPhoneTypeId;
    @CacheInput(inputKey = "demoPhoneTypes",inputInstance = "demoPhoneTypeId",outputInstance = "name")
    private String demoPhoneTypeName;
    private String officeId;
    @CacheInput(inputKey = "offices", inputInstance = "officeId",outputInstance = "name")
    private String officeName;
    @CacheInput(inputKey = "offices",inputInstance = "officeId",outputInstance = "taskPoint")
    private BigDecimal officeTaskPoint;

    public Integer getRealQty() {
        return realQty;
    }

    public void setRealQty(Integer realQty) {
        this.realQty = realQty;
    }

    public String getDemoPhoneTypeName() {
        return demoPhoneTypeName;
    }

    public void setDemoPhoneTypeName(String demoPhoneTypeName) {
        this.demoPhoneTypeName = demoPhoneTypeName;
    }

    public String getOfficeTaskPointString() {
        return officeTaskPoint +"000%";
    }

    public BigDecimal getOfficeTaskPoint() {
        return officeTaskPoint;
    }

    public void setOfficeTaskPoint(BigDecimal officeTaskPoint) {
        this.officeTaskPoint = officeTaskPoint;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getDemoPhoneTypeId() {
        return demoPhoneTypeId;
    }

    public void setDemoPhoneTypeId(String demoPhoneTypeId) {
        this.demoPhoneTypeId = demoPhoneTypeId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }



}
