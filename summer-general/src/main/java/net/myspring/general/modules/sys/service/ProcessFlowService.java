package net.myspring.general.modules.sys.service;

import net.myspring.basic.modules.sys.domain.ProcessFlow;
import net.myspring.general.modules.sys.mapper.ProcessFlowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessFlowService {

    @Autowired
    private ProcessFlowMapper processFlowMapper;

}
