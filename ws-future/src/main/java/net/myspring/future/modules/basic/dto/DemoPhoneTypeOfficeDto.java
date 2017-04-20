package net.myspring.future.modules.basic.dto;

import net.myspring.future.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.DemoPhoneType;
import net.myspring.future.modules.basic.domain.DemoPhoneTypeOffice;

/**
 * Created by lihx on 2017/4/17.
 */
public class DemoPhoneTypeOfficeDto extends DataDto<DemoPhoneTypeOffice> {
    private Integer qty;
    private DemoPhoneType demoPhoneType;
    private String demoPhoneTypeId;
    private String officeId;

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public DemoPhoneType getDemoPhoneType() {
        return demoPhoneType;
    }

    public void setDemoPhoneType(DemoPhoneType demoPhoneType) {
        this.demoPhoneType = demoPhoneType;
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
