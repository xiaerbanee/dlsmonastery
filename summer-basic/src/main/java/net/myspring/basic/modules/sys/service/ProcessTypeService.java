package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.manager.PositionManager;
import net.myspring.basic.modules.sys.domain.ProcessFlow;
import net.myspring.basic.modules.sys.domain.ProcessType;
import net.myspring.basic.modules.sys.dto.ProcessTypeDto;
import net.myspring.basic.modules.sys.manager.PermissionManager;
import net.myspring.basic.modules.sys.manager.ProcessFlowManager;
import net.myspring.basic.modules.sys.manager.ProcessTypeManager;
import net.myspring.basic.modules.sys.mapper.ProcessFlowMapper;
import net.myspring.basic.modules.sys.mapper.ProcessTypeMapper;
import net.myspring.util.mapper.BeanMapper;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
    private ProcessTypeManager processTypeManager;
    @Autowired
    private ProcessTypeMapper processTypeMapper;
    @Autowired
    private ProcessFlowManager processFlowManager;
    @Autowired
    private ProcessFlowMapper processFlowMapper;
    @Autowired
    private PositionManager positionManager;
    @Autowired
    private PermissionManager permissionManager;
    @Autowired
    private CacheUtils cacheUtils;

    public ProcessType findByName(String name){
        ProcessType processFlowList=processTypeMapper.findByName(name);
        return processFlowList;
    }

    public List<ProcessType> findEnabledAuditFileType(){
        return processTypeMapper.findEnabledAuditFileType();
    }

    public ProcessType findOne(String id){
        ProcessType processType=processTypeManager.findOne(id);
        return processType;
    }

    public void logicDeleteOne(ProcessType processType) {
        processType.setEnabled(false);
        processType.setName(processType.getName() +"废弃("+System.currentTimeMillis()+")");
        processTypeManager.update(processType);
    }

    public Page<ProcessTypeDto> findPage(Pageable pageable, Map<String, Object> map) {
        Page<ProcessType> page = processTypeMapper.findPage(pageable, map);
        Page<ProcessTypeDto> processTypeDtoPage= BeanMapper.convertPage(page,ProcessTypeDto.class);
        cacheUtils.initCacheInput(processTypeDtoPage.getContent());
        return processTypeDtoPage;
    }

}
