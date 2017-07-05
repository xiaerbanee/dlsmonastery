package net.myspring.general.modules.sys.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.common.exception.ServiceException;
import net.myspring.general.common.activiti.ActivitiEntity;
import net.myspring.general.common.utils.ActivitiUtils;
import net.myspring.general.common.utils.CacheUtils;
import net.myspring.general.common.utils.RequestUtils;
import net.myspring.general.modules.sys.domain.ProcessFlow;
import net.myspring.general.modules.sys.domain.ProcessTask;
import net.myspring.general.modules.sys.domain.ProcessType;
import net.myspring.general.modules.sys.dto.ActivitiCompleteDto;
import net.myspring.general.modules.sys.dto.ActivitiDetailDto;
import net.myspring.general.modules.sys.dto.ActivitiStartDto;
import net.myspring.general.modules.sys.form.ActivitiCompleteForm;
import net.myspring.general.modules.sys.form.ActivitiStartForm;
import net.myspring.general.modules.sys.repository.ProcessFlowRepository;
import net.myspring.general.modules.sys.repository.ProcessTaskRepository;
import net.myspring.general.modules.sys.repository.ProcessTypeRepository;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

/**
 * Created by wangzm on 2017/4/25.
 */
@Service
@Transactional
public class ActivitiService {
    @Autowired
    protected ActivitiUtils activitiUtils;
    @Autowired
    protected TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ProcessFlowRepository processFlowRepository;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private ProcessTaskRepository processTaskRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ProcessTypeRepository processTypeRepository;

    public ActivitiStartDto start(ActivitiStartForm activitiStartForm){
        //启动流程
        ActivitiStartDto activitiStartDto=new ActivitiStartDto();
        ProcessType processType=processTypeRepository.findByName(activitiStartForm.getProcessTypeName());
        activitiStartDto.setProcessTypeId(processType.getId());
        identityService.setAuthenticatedUserId(RequestUtils.getAccountId());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process_type_" + processType.getId(), activitiStartForm.getBusinessKey());
        String processInstanceId = processInstance.getId();
        activitiStartDto.setProcessInstanceId(processInstanceId);
        String processStatus = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult().getName();
        activitiStartDto.setProcessStatus(processStatus);
        ProcessFlow processFlow = processFlowRepository.findByProcessTypeIdAndName(processType.getId(), processStatus);
        activitiStartDto.setProcessFlowId(processFlow==null?"":processFlow.getId());
        activitiStartDto.setPositionId(processFlow.getPositionId());
        //创建任务
        ProcessTask processTask = new ProcessTask();
        processTask.setName(activitiStartForm.getName());
        processTask.setProcessInstanceId(processInstanceId);
        processTask.setStatus(processStatus);
        processTask.setOfficeId(RequestUtils.getOfficeId());
        processTask.setPositionId(activitiStartDto.getPositionId());
        processTask.setMessage(activitiStartForm.getMessage());
        processTaskRepository.save(processTask);
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
            processFlow = processFlowRepository.findByProcessTypeIdAndName(activitiCompleteForm.getProcessTypeId(), task.getName());
            activitiCompleteDto.setProcessFlowId(processFlow.getId());
            activitiCompleteDto.setPositionId(processFlow.getPositionId());
        }
        activitiCompleteDto.setProcessStatus(getProcessStatus(processFlow, activitiCompleteForm.getPass()));
        ProcessTask processTask = processTaskRepository.findByProcessInstanceId(activitiCompleteForm.getProcessInstanceId());
        if(processTask!=null){
            if(activitiCompleteForm.getPass()){
                String processStatus = getProcessStatus(processFlow, activitiCompleteForm.getPass());
                if(AuditTypeEnum.PASS.getValue().equals(processStatus)){
                    processTask.setStatus(AuditTypeEnum.PASS.getValue());
                    processTask.setEnabled(false);
                }else{
                    processTask.setPositionId(processFlow.getPositionId());
                    processTask.setStatus(processStatus);
                }
            } else {
                processTask.setStatus(AuditTypeEnum.NOT_PASS.getValue());
                processTask.setEnabled(false);
            }
            processTaskRepository.save(processTask);
        }
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
                    activitiDetailDto.setAuditDate(LocalDateTime.ofInstant(historicTaskInstance.getEndTime().toInstant(), ZoneId.systemDefault()));
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
            return pass ? AuditTypeEnum.PASS.getValue() : AuditTypeEnum.NOT_PASS.getValue();
        }
    }
}
