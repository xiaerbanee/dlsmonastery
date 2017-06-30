package net.myspring.general.modules.sys.service;

import com.google.common.collect.Lists;
import net.myspring.general.common.utils.CacheUtils;
import net.myspring.general.modules.sys.domain.ProcessTask;
import net.myspring.general.modules.sys.dto.ProcessTaskDto;
import net.myspring.general.modules.sys.repository.ProcessTaskRepository;
import net.myspring.general.modules.sys.web.query.ProcessTaskQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wangzm on 2017/6/5.
 */
@Service
@Transactional
public class ProcessTaskService {

    @Autowired
    private ProcessTaskRepository processTaskRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<ProcessTaskDto> findPage(Pageable pageable, ProcessTaskQuery processTaskQuery){
        Page<ProcessTaskDto> page=processTaskRepository.findPage(pageable,processTaskQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<ProcessTask> findByPositionAndOfficeIds(String positionId, List<String> officeIds){
        List<ProcessTask> processTasks= Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(officeIds)&& StringUtils.isNotBlank(positionId)){
            processTasks=processTaskRepository.findByPositionIdAndOfficeIdIn(positionId,officeIds);
        }
        return processTasks;
    }

}
