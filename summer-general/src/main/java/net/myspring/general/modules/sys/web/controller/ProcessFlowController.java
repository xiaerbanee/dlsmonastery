package net.myspring.general.modules.sys.web.controller;

import com.google.common.collect.Lists;
import net.myspring.general.modules.sys.dto.ProcessFlowDto;
import net.myspring.general.modules.sys.service.ProcessFlowService;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "sys/processFlow")
public class ProcessFlowController {

    @Autowired
    private ProcessFlowService processFlowService;

    @RequestMapping(value = "findByProcessTypeName")
    public List<ProcessFlowDto> findByProcessTypeName(String processTypeName){
        List<ProcessFlowDto> processFlowList= Lists.newArrayList();
        if(StringUtils.isNotBlank(processTypeName)){
            processFlowList=processFlowService.findByProcessTypeName(processTypeName);
            processFlowList.add(new ProcessFlowDto("已通过"));
            processFlowList.add(new ProcessFlowDto("未通过"));
        }
        return processFlowList;
    }



}
