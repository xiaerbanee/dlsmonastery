package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.ProcessFlow;
import net.myspring.basic.modules.sys.mapper.ProcessFlowMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
@Component
public class ProcessFlowManager {

    @Autowired
    private ProcessFlowMapper processFlowMapper;

    @Cacheable(value = "processFlows",key="#p0")
    public ProcessFlow findOne(String id) {
        return processFlowMapper.findOne(id);
    }

    @Cacheable(value = "processFlows",key="#p0.id")
    public ProcessFlow save(ProcessFlow processFlow){
        processFlowMapper.save(processFlow);
        return  processFlow;
    }

    @CachePut(value = "processFlows",key="#p0.id")
    public ProcessFlow update(ProcessFlow processFlow){
        processFlowMapper.update(processFlow);
        return  processFlowMapper.findOne(processFlow.getId());
    }

    @CacheEvict(value = "processFlows",key="#p0")
    public int deleteById(String id) {
        return processFlowMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }
}
