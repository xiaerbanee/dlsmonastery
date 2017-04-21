package net.myspring.future.modules.basic.dto;

import net.myspring.future.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.Bank;

/**
 * Created by lihx on 2017/4/17.
 */
public class BankDto extends DataDto<Bank> {
    private String name;
    private String code;
    private String accountList;
    private Boolean enabled;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccountList() {
        return accountList;
    }

    public void setAccountList(String accountList) {
        this.accountList = accountList;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
