package net.myspring.general.modules.sys.dto;


import net.myspring.general.common.dto.DataDto;
import net.myspring.general.modules.sys.domain.ProcessType;

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
