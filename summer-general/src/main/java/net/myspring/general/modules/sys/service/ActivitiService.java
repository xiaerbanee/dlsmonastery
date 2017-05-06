package net.myspring.general.modules.sys.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.common.exception.ServiceException;
import net.myspring.general.common.activiti.ActivitiEntity;
import net.myspring.general.common.utils.ActivitiUtils;
import net.myspring.general.common.utils.CacheUtils;
import net.myspring.general.common.utils.SecurityUtils;
import net.myspring.general.modules.sys.domain.ProcessFlow;
import net.myspring.general.modules.sys.domain.ProcessTask;
import net.myspring.general.modules.sys.dto.ActivitiCompleteDto;
import net.myspring.general.modules.sys.dto.ActivitiDetailDto;
import net.myspring.general.modules.sys.dto.ActivitiStartDto;
import net.myspring.general.modules.sys.form.ActivitiCompleteForm;
import net.myspring.general.modules.sys.form.ActivitiStartForm;
import net.myspring.general.modules.sys.mapper.ProcessFlowMapper;
import net.myspring.general.modules.sys.mapper.ProcessTaskMapper;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzm on 2017/4/25.
 */
@Service
public class ActivitiService {
    @Autowired
    protected ActivitiUtils activitiUtils;
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
    @Autowired
    private CacheUtils cacheUtils;

    public ActivitiStartDto start(ActivitiStartForm activitiStartForm){
        //启动流程
        ActivitiStartDto activitiStartDto=new ActivitiStartDto();
        identityService.setAuthenticatedUserId(SecurityUtils.getAccountId());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process_type_" + activitiStartForm.getProcessTypeId(), activitiStartForm.getBusinessKey());
        String processInstanceId = processInstance.getId();
        activitiStartDto.setProcessInstanceId(processInstanceId);
        String processStatus = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult().getName();
        activitiStartDto.setProcessStatus(processStatus);
        ProcessFlow processFlow = processFlowMapper.findByProcessTypeAndName(activitiStartForm.getProcessTypeId(), processStatus);
        activitiStartDto.setProcessFlowId(processFlow==null?"":processFlow.getId());
        activitiStartDto.setPositionId(processFlow.getPositionId());
        //创建任务
        ProcessTask processTask = new ProcessTask();
        processTask.setName(activitiStartForm.getName());
        processTask.setProcessInstanceId(processInstanceId);
        processTask.setStatus(processStatus);
        processTask.setOfficeId(SecurityUtils.getOfficeId());
        processTask.setPositionId(activitiStartDto.getPositionId());
        processTask.setMessage(activitiStartForm.getMessage());
        processTaskMapper.save(processTask);
        return activitiStartDto;
    }

    public ActivitiCompleteDto complete(ActivitiCompleteForm activitiCompleteForm) {
        ActivitiCompleteDto activitiCompleteDto=new ActivitiCompleteDto();
        Task task = taskService.createTaskQuery().processInstanceId(activitiCompleteForm.getProcessInstanceId()).singleResult();
        if (!activitiUtils.claim(task)) {
            throw new ServiceException("无法签收任务，您没有办理此任务的权限或者已经被其他人签收");
        }
        // 添加批注
        taskService.addComment(task.getId(), activitiCompleteForm.getProcessInstanceId(), activitiCompleteForm.getComment());
        Map<String, Object> map = Maps.newHashMap();
        map.put(task.getTaskDefinitionKey() + "_Pass", activitiCompleteForm.getPass());
        taskService.complete(task.getId(), map);
        task = taskService.createTaskQuery().processInstanceId(activitiCompleteForm.getProcessInstanceId()).singleResult();
        ProcessFlow processFlow = null;
        if (task != null) {
            processFlow = processFlowMapper.findByProcessTypeAndName(activitiCompleteForm.getProcessTypeId(), task.getName());
            activitiCompleteDto.setProcessFlowId(processFlow.getId());
            activitiCompleteDto.setPositionId(processFlow.getPositionId());
        }
        activitiCompleteDto.setProcessStatus(getProcessStatus(processFlow, activitiCompleteForm.getPass()));
        ProcessTask processTask = processTaskMapper.findByProcessInstanceId(activitiCompleteForm.getProcessInstanceId());
        if(activitiCompleteForm.getPass()){
            String processStatus = getProcessStatus(processFlow, activitiCompleteForm.getPass());
            if(AuditTypeEnum.PASSED.name().equals(processStatus)){
                processTask.setStatus(AuditTypeEnum.PASSED.name());
                processTask.setEnabled(false);
            }else{
                processTask.setPositionId(processFlow.getPositionId());
                processTask.setStatus(processStatus);
            }
        } else {
            processTask.setStatus(AuditTypeEnum.NOT_PASS.name());
            processTask.setEnabled(false);
        }
        processTaskMapper.update(processTask);
        return activitiCompleteDto;
    }

    public List<ActivitiDetailDto> getActivitiDetail(String processInstanceId){
        List<ActivitiDetailDto> activitiDetailDtoList= Lists.newArrayList();
        ActivitiEntity activitiEntity = activitiUtils.getActivitiEntity(processInstanceId);
        if (CollectionUtil.isNotEmpty(activitiEntity.getHistoricTaskInstances())) {
            for (HistoricTaskInstance historicTaskInstance : activitiEntity.getHistoricTaskInstances()) {
                if (StringUtils.isNotBlank(historicTaskInstance.getAssignee())) {
                    ActivitiDetailDto activitiDetailDto=new ActivitiDetailDto();
                    activitiDetailDto.setAuditBy(historicTaskInstance.getAssignee());
                    activitiDetailDto.setAuditDate(LocalDateTimeUtils.dateToLocalDateTime(historicTaskInstance.getEndTime()));
                    activitiDetailDto.setProcessStatus(historicTaskInstance.getName());
                    activitiDetailDto.setComment(activitiEntity.getCommonMap().get(historicTaskInstance.getId()));
                    activitiDetailDtoList.add(activitiDetailDto);
                }
            }
            cacheUtils.initCacheInput(activitiDetailDtoList);
        }
        return activitiDetailDtoList;
    }

    private  String getProcessStatus(ProcessFlow processFlow, Boolean pass) {
        if (processFlow != null) {
            return processFlow.getName();
        } else {
            return pass ? AuditTypeEnum.PASSED.name() : AuditTypeEnum.NOT_PASS.name();
        }
    }
}
