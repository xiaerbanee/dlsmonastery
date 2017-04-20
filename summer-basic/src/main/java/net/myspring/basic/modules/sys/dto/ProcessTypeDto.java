package net.myspring.basic.modules.sys.dto;

import com.google.common.collect.Lists;
import net.myspring.basic.common.dto.DataDto;
import net.myspring.basic.modules.hr.domain.AccountChange;
import net.myspring.basic.modules.hr.domain.AuditFile;
import net.myspring.basic.modules.hr.domain.EmployeeSalaryBasic;
import net.myspring.basic.modules.hr.domain.OfficeChange;
import net.myspring.basic.modules.sys.domain.ProcessFlow;
import net.myspring.basic.modules.sys.domain.ProcessType;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
public class ProcessTypeDto extends DataDto<ProcessType> {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
