package net.myspring.general.modules.sys.dto;

import com.google.common.collect.Maps;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.collection.CollectionUtil;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.persistence.entity.CommentEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import net.myspring.util.text.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/4/22.
 */
public class ActivitiDto {
    private List<HistoricVariableInstance> historicVariableInstances;

    private List<HistoricTaskInstance> historicTaskInstances;

    private ProcessInstance processInstance;

    private HistoricProcessInstance historicProcessInstance;

    private List<Comment> comments;

    private List<String> accountIdList;

    @CacheInput(inputKey = "accounts",inputInstance = "accountIdList",outputInstance = "loginName")
    private List<String> accountNameList;

    public List<HistoricVariableInstance> getHistoricVariableInstances() {
        return historicVariableInstances;
    }

    public void setHistoricVariableInstances(List<HistoricVariableInstance> historicVariableInstances) {
        this.historicVariableInstances = historicVariableInstances;
    }

    public List<HistoricTaskInstance> getHistoricTaskInstances() {
        return historicTaskInstances;
    }

    public void setHistoricTaskInstances(List<HistoricTaskInstance> historicTaskInstances) {
        this.historicTaskInstances = historicTaskInstances;
    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    public HistoricProcessInstance getHistoricProcessInstance() {
        return historicProcessInstance;
    }

    public void setHistoricProcessInstance(HistoricProcessInstance historicProcessInstance) {
        this.historicProcessInstance = historicProcessInstance;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<String> getAccountIdList() {
        return accountIdList;
    }

    public void setAccountIdList(List<String> accountIdList) {
        this.accountIdList = accountIdList;
    }

    public List<String> getAccountNameList() {
        return accountNameList;
    }

    public void setAccountNameList(List<String> accountNameList) {
        this.accountNameList = accountNameList;
    }

    public Map<String, String> getAccountMap() {
        Map<String,String> accountMap = Maps.newHashMap();
        for(int i=0;i<accountIdList.size();i++) {
            accountMap.put(accountIdList.get(i),accountNameList.get(i));
        }
        return accountMap;
    }

    public Map<String, Object> getVariableMap() {
        Map<String, Object> map = Maps.newHashMap();
        if (historicVariableInstances != null && historicVariableInstances.size() > 0) {
            for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
                map.put(historicVariableInstance.getVariableName(), historicVariableInstance.getValue());
            }
        }
        return map;
    }

    public Map<String, Object> getCommentMap() {
        Map<String, Object> map = Maps.newHashMap();
        if (comments != null && comments.size() > 0) {
            for (Comment comment : comments) {
                CommentEntity commentEntity = (CommentEntity)comment;
                map.put(comment.getTaskId(), commentEntity.getMessage());
            }
        }
        return map;
    }

    public boolean isEditable() {
        if(CollectionUtil.isNotEmpty(getHistoricTaskInstances())){
            return StringUtils.isBlank(getHistoricTaskInstances().get(getHistoricTaskInstances().size() - 1).getAssignee());
        }
        return false;
    }

}
