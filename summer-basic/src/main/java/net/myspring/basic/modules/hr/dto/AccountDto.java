package net.myspring.basic.modules.hr.dto;

import net.myspring.basic.common.dto.DataDto;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.util.cahe.annotation.CacheInput;

/**
 * Created by liuj on 2017/3/19.
 */
public class AccountDto extends DataDto<Account> {

    private String positionId;
    private String dataScope;

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }
}
