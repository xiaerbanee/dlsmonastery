package net.myspring.basic.modules.hr.web.form;


import com.google.common.collect.Lists;
import net.myspring.basic.modules.hr.domain.Recruit;
import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.hr.dto.PositionDto;
import net.myspring.basic.modules.sys.dto.DictEnumDto;

import java.util.List;

/**
 * Created by admin on 2017/4/6.
 */

public class RecruitForm extends DataForm<Recruit> {
    private List<PositionDto> positionList= Lists.newArrayList();
    private List<String> educationList= Lists.newArrayList();

    public List<PositionDto> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<PositionDto> positionList) {
        this.positionList = positionList;
    }

    public List<String> getEducationList() {
        return educationList;
    }

    public void setEducationList(List<String> educationList) {
        this.educationList = educationList;
    }
}
