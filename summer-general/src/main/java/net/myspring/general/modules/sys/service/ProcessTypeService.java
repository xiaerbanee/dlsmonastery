package net.myspring.general.modules.sys.service;

import net.myspring.general.modules.sys.mapper.ProcessTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProcessTypeService {

    @Autowired
    private ProcessTypeMapper processTypeMapper;

}
