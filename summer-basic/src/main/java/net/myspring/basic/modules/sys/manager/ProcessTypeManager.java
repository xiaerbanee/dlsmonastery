package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.ProcessType;
import net.myspring.basic.modules.sys.mapper.ProcessTypeMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/5.
 */
@Component
public class ProcessTypeManager {

    @Autowired
    private ProcessTypeMapper processTypeMapper;

    @Cacheable(value = "processTypes",key="#p0")
    public ProcessType findOne(String id) {
        return processTypeMapper.findOne(id);
    }

    @Cacheable(value = "processTypes",key="#p0.id")
    public ProcessType save(ProcessType processType){
        processTypeMapper.save(processType);
        return  processType;
    }

    @CachePut(value = "processTypes",key="#p0.id")
    public ProcessType update(ProcessType processType){
        processTypeMapper.update(processType);
        return  processTypeMapper.findOne(processType.getId());
    }

    @CacheEvict(value = "processTypes",key="#p0")
    public int deleteById(String id) {
        return processTypeMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }
}
