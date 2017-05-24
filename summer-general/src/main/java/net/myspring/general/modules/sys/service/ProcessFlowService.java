package net.myspring.general.modules.sys.service;

import net.myspring.general.modules.sys.domain.ProcessFlow;
import net.myspring.general.modules.sys.dto.ProcessFlowDto;
import net.myspring.general.modules.sys.repository.ProcessFlowRepository;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessFlowService {

    @Autowired
    private ProcessFlowRepository processFlowRepository;

    public List<ProcessFlowDto> findByProcessTypeName(String processTypeName){
        List<ProcessFlow> processFlowList=processFlowRepository.findByProcessTypeName(processTypeName);
        List<ProcessFlowDto> processFlowDtoList= BeanUtil.map(processFlowList,ProcessFlowDto.class);
        return processFlowDtoList;
    }

}
