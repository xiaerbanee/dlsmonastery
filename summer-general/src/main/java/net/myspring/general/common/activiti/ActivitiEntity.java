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
import net.myspring.util.text.StringUtils;

import java.util.List;
import java.util.Map;

public class ActivitiEntity {
	private List<Task> tasks;

	private List<HistoricVariableInstance> historicVariableInstances;

	private List<HistoricTaskInstance> historicTaskInstances;

	private ProcessInstance processInstance;

	private HistoricProcessInstance historicProcessInstance;

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

}
