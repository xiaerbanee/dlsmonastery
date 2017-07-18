package net.myspring.basic.modules.hr.dto;

import net.myspring.basic.modules.hr.domain.AccountWeixin;
import net.myspring.common.dto.DataDto;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.util.cahe.annotation.CacheInput;

/**
 * Created by liuj on 2017/3/19.
 */
public class AccountWeixinDto extends DataDto<AccountWeixin> {
    private String accountId;
    private String openId;
    @CacheInput(inputKey = "accounts",inputInstance = "accountId",outputInstance = "loginName")
    private String accountName;
    private String officeId;
    @CacheInput(inputKey = "offices",inputInstance = "officeId",outputInstance = "name")
    private String officeName;
    private String areaId;
    @CacheInput(inputKey = "offices",inputInstance = "areaId",outputInstance = "name")
    private String areaName;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
