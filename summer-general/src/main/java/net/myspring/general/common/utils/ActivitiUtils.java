package net.myspring.general.common.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.general.common.activiti.ActivitiEntity;
import net.myspring.general.modules.sys.dto.ActivitiDto;
import net.myspring.util.cahe.CacheReadUtils;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Component
public class ActivitiUtils {
    private static Map<String, ProcessDefinitionEntity> processDefinitions = Maps.newHashMap();
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RedisTemplate redisTemplate;

    public ActivitiDto getActivitiDto(String processInstanceId) {
        ActivitiEntity activitiEntity = new ActivitiEntity();
        if (StringUtils.isNotBlank(processInstanceId)) {
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
            activitiEntity.setTasks(tasks);
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            if (historicProcessInstance != null) {
                activitiEntity.setHistoricProcessInstance(historicProcessInstance);
                activitiEntity.setProcessDefinition(repositoryService.createProcessDefinitionQuery().processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult());
            } else {
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
                if(processInstance!=null){
                    activitiEntity.setProcessInstance(processInstance);
                    activitiEntity.setProcessDefinition(repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult());
                }
            }
            activitiEntity.setHistoricTaskInstances(historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByHistoricTaskInstanceEndTime().asc().list());
            activitiEntity.setHistoricVariableInstances(historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list());
            activitiEntity.setComments(taskService.getProcessInstanceComments(processInstanceId));
            Map<String, String> accountMap = Maps.newHashMap();
            if (CollectionUtil.isNotEmpty(activitiEntity.getHistoricTaskInstances())) {
                List<String> accountKeyList = Lists.newArrayList();
                for (HistoricTaskInstance historicTaskInstance : activitiEntity.getHistoricTaskInstances()) {
                    if (StringUtils.isNotBlank(historicTaskInstance.getAssignee())) {
                        accountKeyList.add("accounts:" + historicTaskInstance.getAssignee());
                    }
                }
                Map<String,Object> accountCacheMap = Maps.newHashMap();
                if(CollectionUtil.isNotEmpty(accountKeyList)) {
                    accountCacheMap = CacheReadUtils.getCacheMap(redisTemplate,accountKeyList,"loginName");
                }
                for (HistoricTaskInstance historicTaskInstance : activitiEntity.getHistoricTaskInstances()) {
                    if (StringUtils.isNotBlank(historicTaskInstance.getAssignee())) {
                        accountMap.put(historicTaskInstance.getId(), StringUtils.toString(accountCacheMap.get("accounts:" + historicTaskInstance.getAssignee())));
                    }
                }
            }
            activitiEntity.setAccountMap(accountMap);
        }
        ActivitiDto activitiDto = BeanUtil.map(activitiEntity,ActivitiDto.class);
        return activitiDto;
    }

    public Boolean claim(Task task) {
        // 如果没有签收，签收
        if (task != null) {
            if (StringUtils.isEmpty(task.getAssignee())) {
                taskService.claim(task.getId(), SecurityUtils.getAccountId());
                return true;
            } else {
                if (task.getAssignee().equals(SecurityUtils.getAccountId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> getCandidateUsers(Task task) {
        List<String> ret = Lists.newLinkedList();
        ProcessDefinitionEntity processDefinition = getProcessDefinition(task.getProcessDefinitionId());
        Set<Expression> candidateUserIdExpressions = processDefinition.getTaskDefinitions().get(task.getTaskDefinitionKey()).getCandidateUserIdExpressions();
        for (Expression expression : candidateUserIdExpressions) {
            ret.add(expression.getExpressionText());
        }
        return ret;
    }

    private ProcessDefinitionEntity getProcessDefinition(String processDefinitionId) {
        ProcessDefinitionEntity processDefinition = processDefinitions.get(processDefinitionId);
        if (processDefinition == null) {
            ProcessDefinitionEntity loadedProcessDefinition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
            processDefinitions.put(processDefinitionId, loadedProcessDefinition);
            processDefinition = loadedProcessDefinition;
        }
        return processDefinition;
    }

    public List<String> getCandidateGroups(Task task) {
        List<String> ret = Lists.newLinkedList();
        ProcessDefinitionEntity processDefinition = getProcessDefinition(task.getProcessDefinitionId());
        Set<Expression> candidateUserIdExpressions = processDefinition.getTaskDefinitions().get(task.getTaskDefinitionKey()).getCandidateGroupIdExpressions();
        for (Expression expression : candidateUserIdExpressions) {
            ret.add(expression.getExpressionText());
        }
        return ret;
    }
}
