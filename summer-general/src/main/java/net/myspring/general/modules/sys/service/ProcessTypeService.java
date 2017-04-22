package net.myspring.general.modules.sys.service;

import com.google.common.collect.Lists;
import net.myspring.general.modules.sys.domain.ProcessFlow;
import net.myspring.general.modules.sys.domain.ProcessType;
import net.myspring.general.modules.sys.dto.ProcessFlowDto;
import net.myspring.general.modules.sys.dto.ProcessTypeDto;
import net.myspring.general.modules.sys.mapper.ProcessFlowMapper;
import net.myspring.general.modules.sys.mapper.ProcessTypeMapper;
import net.myspring.general.modules.sys.web.query.ProcessTypeQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ProcessTypeService {


    @Autowired
    private ProcessTypeMapper processTypeMapper;
    @Autowired
    private ProcessFlowMapper processFlowMapper;
    @Autowired
    private RepositoryService repositoryService;

    public ProcessType findByName(String name){
        ProcessType processFlowList=processTypeMapper.findByName(name);
        return processFlowList;
    }

    public List<ProcessType> findEnabledAuditFileType(){
        return processTypeMapper.findEnabledAuditFileType();
    }

    public ProcessType findOne(String id){
        ProcessType processType=processTypeMapper.findOne(id);
        return processType;
    }

    public void logicDeleteOne(ProcessType processType) {
        processType.setEnabled(false);
        processType.setName(processType.getName() +"removed("+System.currentTimeMillis()+")");
        processTypeMapper.update(processType);
    }

    @Transactional
    public void save(ProcessTypeDto processTypeDto){
        for (int i = processTypeDto.getProcessFlowDtoList().size() - 1; i >= 0; i--) {
            ProcessFlowDto processFlowDto= processTypeDto.getProcessFlowDtoList().get(i);
            if (StringUtils.isBlank(processFlowDto.getName())) {
                processTypeDto.getProcessFlowDtoList().remove(i);
            }
        }
        ProcessType processType = BeanUtil.map(processTypeDto,ProcessType.class);
        processTypeMapper.save(processType);
        for(ProcessFlowDto processFlowDto:processTypeDto.getProcessFlowDtoList()) {
            processFlowDto.setProcessTypeId(processType.getId());
        }
        processFlowMapper.batchSave(BeanUtil.map(processTypeDto.getProcessFlowDtoList(),ProcessFlow.class));
        // 部署流程
        String processId = "process_type_" + processType.getId();
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        process.setId(processId);
        process.setName(processType.getName());
        model.addProcess(process);

        // 起始节点
        StartEvent startEvent = new StartEvent();
        startEvent.setId("start");
        process.addFlowElement(startEvent);
        List<ProcessFlow> processFlows = processFlowMapper.findByProcessType(processType.getId());
        for (int i = 0; i < processFlows.size(); i++) {
            ProcessFlow processFlow = processFlows.get(i);
            List<String> candidataGroups = Lists.newArrayList();
            candidataGroups.add(processFlow.getPositionId());
            UserTask userTask = new UserTask();
            userTask.setName(processFlow.getName());
            userTask.setId("task_" + processFlow.getId());
            userTask.setCandidateGroups(candidataGroups);
            process.addFlowElement(userTask);
            if (i != processFlows.size() - 1) {
                ExclusiveGateway exclusivegateway = new ExclusiveGateway();
                exclusivegateway.setId("exclusivegateway_" + processFlow.getId());
                process.addFlowElement(exclusivegateway);
            }
        }
        // 结束节点
        EndEvent endEvent = new EndEvent();
        endEvent.setId("end");
        process.addFlowElement(endEvent);

        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef("start");
        flow.setTargetRef("task_" + processFlows.get(0).getId());
        process.addFlowElement(flow);

        for (int i = 0; i < processFlows.size() - 1; i++) {
            ProcessFlow processFlow = processFlows.get(i);
            flow = new SequenceFlow();
            flow.setSourceRef("task_" + processFlow.getId());
            flow.setTargetRef("exclusivegateway_" + processFlow.getId());
            process.addFlowElement(flow);

            flow = new SequenceFlow();
            flow.setSourceRef("exclusivegateway_" + processFlow.getId());
            flow.setTargetRef("task_" + processFlows.get(i + 1).getId());
            flow.setName("同意");
            flow.setConditionExpression("${task_" + processFlow.getId() + "_Pass}");
            process.addFlowElement(flow);

            flow = new SequenceFlow();
            flow.setSourceRef("exclusivegateway_" + processFlow.getId());
            flow.setTargetRef("end");
            flow.setName("不同意");
            flow.setConditionExpression("${!task_" + processFlow.getId() + "_Pass}");
            process.addFlowElement(flow);
        }
        flow = new SequenceFlow();
        flow.setSourceRef("task_" + processFlows.get(processFlows.size() - 1).getId());
        flow.setTargetRef("end");
        process.addFlowElement(flow);

        // 2. Generate graphical information
        new BpmnAutoLayout(model).execute();
        // 3. Deploy the process to the engine
        repositoryService.createDeployment().addBpmnModel(processId + ".bpmn", model).name(processType.getName()).deploy();
    }

    public Page<ProcessTypeDto> findPage(Pageable pageable, ProcessTypeQuery processTypeQuery) {
        Page<ProcessTypeDto> page = processTypeMapper.findPage(pageable, processTypeQuery);
        return page;
    }

}
