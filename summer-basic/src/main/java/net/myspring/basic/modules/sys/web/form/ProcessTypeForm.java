package net.myspring.basic.modules.sys.web.form;

import net.myspring.basic.modules.hr.dto.PositionDto;
import net.myspring.basic.modules.sys.domain.ProcessType;
import net.myspring.basic.common.form.DataForm;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/6.
 */

public class ProcessTypeForm extends DataForm<ProcessType>{
    private List<PositionDto> positionDtoList;
    private Map<Boolean,String>  bools;

    public List<PositionDto> getPositionDtoList() {
        return positionDtoList;
    }

    public void setPositionDtoList(List<PositionDto> positionDtoList) {
        this.positionDtoList = positionDtoList;
    }

    public Map<Boolean, String> getBools() {
        return bools;
    }

    public void setBools(Map<Boolean, String> bools) {
        this.bools = bools;
    }
}
