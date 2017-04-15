package net.myspring.basic.modules.hr.dto;

import net.myspring.basic.common.dto.DataDto;
import net.myspring.basic.modules.hr.domain.AccountTask;

/**
 * Created by admin on 2017/4/5.
 */
public class AccountTaskDto extends DataDto<AccountTask> {
    private String name;
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
