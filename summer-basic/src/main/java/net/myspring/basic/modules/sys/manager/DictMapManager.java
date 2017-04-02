package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.DictMap;
import net.myspring.basic.modules.sys.mapper.DictMapMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/1.
 */
@Component
public class DictMapManager {

    @Autowired
    private DictMapMapper dictMapMapper;

    @Cacheable(value = "dictMaps",key="#p0")
    public DictMap findOne(String id) {
        return dictMapMapper.findOne(id);
    }

    public List<String> findDistinctCategory(){
        return dictMapMapper.findDistinctCategory();
    }

    public Page<DictMap> findPage(Pageable pageable, Map<String, Object> map) {
        Page<DictMap> page = dictMapMapper.findPage(pageable, map);
        return page;
    }

    @CachePut(value = "dictMaps",key="#p0.id")
    public DictMap save(DictMap dictMap){
        dictMapMapper.save(dictMap);
        return  dictMap;
    }

    @CachePut(value = "dictMaps",key="#p0.id")
    public DictMap update(DictMap dictMap){
        dictMapMapper.update(dictMap);
        return  dictMapMapper.findOne(dictMap.getId());
    }

    public void logicDeleteOne(String id) {
        dictMapMapper.logicDeleteOne(id);
    }
}
