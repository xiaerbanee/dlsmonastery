package net.myspring.basic.modules.sys.web.form;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.modules.hr.dto.PositionDto;
import net.myspring.basic.modules.sys.domain.ProcessType;
import net.myspring.basic.common.form.DataForm;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/6.
 */

public class ProcessTypeForm extends DataForm<ProcessType>{
    private List<PositionDto> positionList= Lists.newArrayList();
    private Map<Boolean,String>  bools= Maps.newHashMap();

    public List<PositionDto> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<PositionDto> positionList) {
        this.positionList = positionList;
    }

    public Map<Boolean, String> getBools() {
        return bools;
    }

    public void setBools(Map<Boolean, String> bools) {
        this.bools = bools;
    }
}
