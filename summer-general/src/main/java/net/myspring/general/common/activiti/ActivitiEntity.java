package net.myspring.general.common.activiti;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import net.myspring.util.collection.CollectionUtil;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.persistence.entity.CommentEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class ActivitiEntity {

	@JsonIgnore
	private List<Task> tasks;

	private List<HistoricVariableInstance> historicVariableInstances;

	private List<HistoricTaskInstance> historicTaskInstances;

	private ProcessInstance processInstance;

	private HistoricProcessInstance historicProcessInstance;

	@JsonIgnore
	private ProcessDefinition processDefinition;

	private List<Comment> comments;

	private Map<String,String> accountMap = Maps.newHashMap();

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}


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

	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Map<String, String> getAccountMap() {
		return accountMap;
	}

	public void setAccountMap(Map<String, String> accountMap) {
		this.accountMap = accountMap;
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
