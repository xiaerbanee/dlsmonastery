package net.myspring.future.modules.basic.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.Client;

import java.time.LocalDateTime;

/**
 * Created by lihx on 2017/4/17.
 */
public class ClientDto extends DataDto<Client> {
    private String name;
    private String mobilePhone;
    private Boolean locked;
    private Boolean enabled;
    // 财务编号
    private String outId;
    //财务分组
    private String outGroupId;
    //财务分组名称
    private String outGroupName;
    //财务同步日期
    private LocalDateTime outDate;
    private String outCode;

    public String getOutGroupId() {
        return outGroupId;
    }

    public void setOutGroupId(String outGroupId) {
        this.outGroupId = outGroupId;
    }

    public String getOutGroupName() {
        return outGroupName;
    }

    public void setOutGroupName(String outGroupName) {
        this.outGroupName = outGroupName;
    }

    public LocalDateTime getOutDate() {
        return outDate;
    }

    public void setOutDate(LocalDateTime outDate) {
        this.outDate = outDate;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
