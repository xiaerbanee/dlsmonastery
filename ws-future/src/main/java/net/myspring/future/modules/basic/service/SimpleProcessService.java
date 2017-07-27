package net.myspring.future.modules.basic.service;


import net.myspring.future.modules.basic.dto.SimpleProcessDetailDto;
import net.myspring.future.modules.basic.manager.SimpleProcessManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SimpleProcessService {

    @Autowired
    private SimpleProcessManager simpleProcessManager;

    public List<SimpleProcessDetailDto> findBySimpleProcessId(String simpleProcessId) {
        return simpleProcessManager.findDetailDtoList(simpleProcessId);
    }

}
