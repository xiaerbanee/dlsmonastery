package net.myspring.general.common.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.general.common.activiti.ActivitiEntity;
import net.myspring.general.common.utils.SecurityUtils;
import net.myspring.util.cahe.CacheReadUtils;
import net.myspring.util.collection.CollectionUtil;
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
    private static Map<String, String> types = Maps.newHashMap();
    static {
        types.put("userTask", "用户任务");
        types.put("serviceTask", "系统任务");
        types.put("startEvent", "开始节点");
        types.put("endEvent", "结束节点");
        types.put("exclusiveGateway", "条件判断节点(系统自动根据条件处理)");
        types.put("inclusiveGateway", "并行处理任务");
        types.put("callActivity", "子流程");
    }

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

    /**
     * 转换流程节点类型为中文说明
     *
     * @param type 英文名称
     * @return 翻译后的中文名称
     */
    public String parseToZhType(String type) {
        return types.get(type) == null ? type : types.get(type);
    }

    public ActivitiEntity getActivitiEntity(String processInstanceId) {
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
        return activitiEntity;
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
