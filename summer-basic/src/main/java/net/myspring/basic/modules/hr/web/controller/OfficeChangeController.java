package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.modules.hr.dto.OfficeChangeDto;
import net.myspring.basic.modules.hr.dto.OfficeChangeFormDto;
import net.myspring.basic.modules.hr.service.OfficeChangeService;
import net.myspring.basic.modules.hr.web.form.OfficeChangeForm;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "hr/officeChange")
public class OfficeChangeController {

    @Autowired
    private OfficeChangeService officeChangeService;

    @RequestMapping(value = "getChangeByOfficeId")
    public List<OfficeChangeFormDto> getChangeByOfficeId(String officeId){
        List<OfficeChangeFormDto> officeChangeDtoList=officeChangeService.getChangeByOfficeId(officeId);
        return officeChangeDtoList;
    }

}
