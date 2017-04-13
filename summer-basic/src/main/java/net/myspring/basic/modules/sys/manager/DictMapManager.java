package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.DictMap;
import net.myspring.basic.modules.sys.mapper.DictMapMapper;
import net.myspring.basic.modules.sys.web.form.DictMapForm;
import net.myspring.util.mapper.BeanUtil;
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

    @Cacheable(value = "dictMaps",key="#p0.id")
    public DictMap save(DictMap dictMap){
        dictMapMapper.save(dictMap);
        return  dictMap;
    }

    @Cacheable(value = "dictMaps",key="#p0.id")
    public DictMap saveForm(DictMapForm dictMapForm){
        dictMapMapper.save(BeanUtil.map(dictMapForm,DictMap.class));
        return  dictMapMapper.findOne(dictMapForm.getId());
    }

    @CachePut(value = "dictMaps",key="#p0.id")
    public DictMap update(DictMap dictMap){
        dictMapMapper.update(dictMap);
        return  dictMapMapper.findOne(dictMap.getId());
    }

    @CachePut(value = "dictMaps",key="#p0.id")
    public DictMap updateForm(DictMapForm dictMapForm){
        dictMapMapper.updateForm(dictMapForm);
        return  dictMapMapper.findOne(dictMapForm.getId());
    }

    @CacheEvict(value = "dictMaps",key="#p0")
    public int deleteById(String id) {
        return dictMapMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }
}
