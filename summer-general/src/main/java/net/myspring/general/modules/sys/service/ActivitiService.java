package net.myspring.general.modules.sys.service;

import com.google.common.collect.Maps;
import net.myspring.general.common.enums.AuditTypeEnum;
import net.myspring.general.common.utils.SecurityUtils;
import net.myspring.general.modules.sys.domain.ProcessFlow;
import net.myspring.general.modules.sys.domain.ProcessTask;
import net.myspring.general.modules.sys.dto.ActivitiAuditDto;
import net.myspring.general.modules.sys.dto.ActivitiAuthenticatedDto;
import net.myspring.general.modules.sys.form.ActivitiAuditForm;
import net.myspring.general.modules.sys.form.ActivitiAuthenticatedForm;
import net.myspring.general.modules.sys.mapper.ProcessFlowMapper;
import net.myspring.general.modules.sys.mapper.ProcessTaskMapper;
import net.myspring.util.text.StringUtils;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by wangzm on 2017/4/25.
 */
@Service
public class ActivitiService {

    @Autowired
    protected TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ProcessFlowMapper processFlowMapper;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private ProcessTaskMapper processTaskMapper;

    public ActivitiAuthenticatedDto authenticatedActiviti(ActivitiAuthenticatedForm activitiAuthenticatedForm){
        ActivitiAuthenticatedDto activitiDto=new ActivitiAuthenticatedDto();
        identityService.setAuthenticatedUserId(SecurityUtils.getAccountId());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process_type_" + activitiAuthenticatedForm.getProcessTypeId(), activitiAuthenticatedForm.getKey());
        String processInstanceId = processInstance.getId();
        activitiDto.setProcessInstanceId(processInstanceId);
        String processStatus = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult().getName();
        activitiDto.setProcessStatus(processStatus);
        ProcessFlow processFlow = processFlowMapper.findByProductTypeAndName(activitiAuthenticatedForm.getProcessTypeId(), processStatus);
        activitiDto.setProcessFlowId(processFlow==null?"":processFlow.getId());
        activitiDto.setProcessFlowId(processFlow.getPositionId());
        return activitiDto;
    }

    public ActivitiAuditDto audit(ActivitiAuditForm activitiAuditForm) {
        ActivitiAuditDto activitiAuditDto=new ActivitiAuditDto();
        Task task = taskService.createTaskQuery().processInstanceId(activitiAuditForm.getProcessInstanceId()).singleResult();
        // 添加批注
        taskService.addComment(task.getId(), activitiAuditForm.getProcessInstanceId(), activitiAuditForm.getComment());
        Map<String, Object> map = Maps.newHashMap();
        map.put(task.getTaskDefinitionKey() + "_Pass", activitiAuditForm.getPass());
        taskService.complete(task.getId(), map);
        task = taskService.createTaskQuery().processInstanceId(activitiAuditForm.getProcessInstanceId()).singleResult();
        ProcessFlow processFlow = null;
        if (task != null) {
            processFlow = processFlowMapper.findByProductTypeAndName(activitiAuditForm.getProcessTypeId(), task.getName());
        }
        activitiAuditDto.setProcessFlowId(processFlow.getId());
        activitiAuditDto.setPositionId(processFlow.getPositionId());
        activitiAuditDto.setProcessStatus(getProcessStatus(processFlow, activitiAuditForm.getPass()));
        return activitiAuditDto;
    }

    public void notify(String name,String extendId,String positionId,String processStatus) {
        ProcessTask processTask = processTaskMapper.findByNameAndExtendId(name,extendId);
        if(processTask==null){
            processTask =new ProcessTask();
            processTask.setName(name);
            processTask.setExtendId(extendId);
            processTask.setPositionId(positionId);
            processTask.setOfficeId(SecurityUtils.getOfficeId());
            processTaskMapper.save(processTask);
        }else {
            if(AuditTypeEnum.PASS.getValue().equals(processStatus) ||AuditTypeEnum.NOT_PASS.getValue().equals(processStatus)){
                processTask.setStatus("已审核");
                processTask.setEnabled(false);
            }else{
                processTask.setPositionId(positionId);
                processTask.setStatus(processStatus);
            }
            processTaskMapper.update(processTask);
        }
    }

    private  String getProcessStatus(ProcessFlow processFlow, Boolean pass) {
        if (processFlow != null) {
            return processFlow.getName();
        } else {
            return pass ? AuditTypeEnum.PASS.getValue() : AuditTypeEnum.NOT_PASS.getValue();
        }
    }
}
