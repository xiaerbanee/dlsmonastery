package net.myspring.general.modules.sys.web.controller;

import com.google.common.collect.Lists;
import net.myspring.general.modules.sys.dto.ActivitiCompleteDto;
import net.myspring.general.modules.sys.dto.ActivitiDetailDto;
import net.myspring.general.modules.sys.dto.ActivitiDto;
import net.myspring.general.modules.sys.dto.ActivitiStartDto;
import net.myspring.general.modules.sys.form.ActivitiCompleteForm;
import net.myspring.general.modules.sys.form.ActivitiStartForm;
import net.myspring.general.modules.sys.service.ActivitiService;
import net.myspring.util.text.StringUtils;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
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
    @Autowired
    private RepositoryService repositoryService;

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

    @RequestMapping(value = "/processList")
    public List<ActivitiDto> processList() {
        List<ActivitiDto> activitiList = Lists.newArrayList();
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            ActivitiDto ActivitiDto=new ActivitiDto();
            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            ActivitiDto.setDeploymentId(deploymentId);
            ActivitiDto.setDeploymentTime(deployment.getDeploymentTime());
            ActivitiDto.setId(processDefinition.getId());
            ActivitiDto.setKey(processDefinition.getKey());
            ActivitiDto.setName(processDefinition.getName());
            ActivitiDto.setResourceName(processDefinition.getResourceName());
            ActivitiDto.setSuspended(processDefinition.isSuspended());
            ActivitiDto.setVersion(processDefinition.getVersion());
            activitiList.add(ActivitiDto);
        }
        return activitiList;
    }
}
