package net.myspring.general.modules.sys.service;

import net.myspring.general.modules.sys.mapper.ProcessFlowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessFlowService {

    @Autowired
    private ProcessFlowMapper processFlowMapper;

}
