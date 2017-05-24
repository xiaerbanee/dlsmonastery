package net.myspring.general.modules.sys.web.controller;

import net.myspring.general.modules.sys.dto.ActivitiCompleteDto;
import net.myspring.general.modules.sys.dto.ActivitiDetailDto;
import net.myspring.general.modules.sys.dto.ActivitiStartDto;
import net.myspring.general.modules.sys.form.ActivitiCompleteForm;
import net.myspring.general.modules.sys.form.ActivitiStartForm;
import net.myspring.general.modules.sys.service.ActivitiService;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wangzm on 2017/4/25.
 */
@RestController
@RequestMapping(value = "sys/activiti")
public class ActivitiController {

    @Autowired
    private ActivitiService  activitiService;

    @RequestMapping(value = "start")
    public ActivitiStartDto start(@RequestBody ActivitiStartForm activitiStartForm){
        ActivitiStartDto activitiStartDto=new ActivitiStartDto();
        if(StringUtils.isNotBlank(activitiStartForm.getProcessTypeName())){
            activitiStartDto=activitiService.start(activitiStartForm);
        }
        return activitiStartDto;
    }

    @RequestMapping(value = "complete")
    public ActivitiCompleteDto complete(@RequestBody ActivitiCompleteForm activitiCompleteForm){
        ActivitiCompleteDto activitiCompleteDto=new ActivitiCompleteDto();
        if(StringUtils.isNotBlank(activitiCompleteForm.getProcessTypeId())&&StringUtils.isNotBlank(activitiCompleteForm.getProcessInstanceId())){
            activitiCompleteDto= activitiService.complete(activitiCompleteForm);
        }
        return activitiCompleteDto;
    }

    @RequestMapping(value = "detail")
    public List<ActivitiDetailDto> getActivitiDetail(String processInstanceId){
        List<ActivitiDetailDto> activitiDetailDtoList= activitiService.getActivitiDetail(processInstanceId);
        return activitiDetailDtoList;
    }


}
