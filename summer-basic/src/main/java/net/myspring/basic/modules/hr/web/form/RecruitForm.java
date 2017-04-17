package net.myspring.basic.modules.hr.web.form;


import net.myspring.basic.modules.hr.domain.Recruit;
import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.hr.dto.PositionDto;
import net.myspring.basic.modules.sys.dto.DictEnumDto;

import java.util.List;

/**
 * Created by admin on 2017/4/6.
 */

public class RecruitForm extends DataForm<Recruit> {
    private List<PositionDto> positionDtoList;
    private List<DictEnumDto> educationsList;

    public List<PositionDto> getPositionDtoList() {
        return positionDtoList;
    }

    public void setPositionDtoList(List<PositionDto> positionDtoList) {
        this.positionDtoList = positionDtoList;
    }

    public List<DictEnumDto> getEducationsList() {
        return educationsList;
    }

    public void setEducationsList(List<DictEnumDto> educationsList) {
        this.educationsList = educationsList;
    }
}
