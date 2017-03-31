package net.myspring.basic.modules.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import net.myspring.basic.modules.sys.mapper.ProcessTypeMapper;

@Service
public class ProcessTypeService {

    @Autowired
    private ProcessTypeMapper processTypeMapper;

}
