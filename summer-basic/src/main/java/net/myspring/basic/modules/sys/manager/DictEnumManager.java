package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.DictEnum;
import net.myspring.basic.modules.sys.mapper.DictEnumMapper;
import net.myspring.basic.modules.sys.web.form.DictEnumForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
@Component
@CacheConfig(cacheNames = "dictEnums")
public class DictEnumManager {

    @Autowired
    private DictEnumMapper dictEnumMapper;

    @Cacheable(key="#p0")
    public DictEnum findOne(String id){
        return dictEnumMapper.findOne(id);
    }

    @CachePut(key="#p0.id")
    public DictEnum save(DictEnum dictEnum){
        dictEnumMapper.save(dictEnum);
        return  dictEnum;
    }

    @CachePut(key="#p0.id")
    public DictEnum update(DictEnum dictEnum){
        dictEnumMapper.update(dictEnum);
        return  dictEnumMapper.findOne(dictEnum.getId());
    }

    @CachePut(key="#p0.id")
    public DictEnum updateForm(DictEnumForm dictEnumForm){
        dictEnumMapper.updateForm(dictEnumForm);
        return  dictEnumMapper.findOne(dictEnumForm.getId());
    }

    @CacheEvict(key="#p0")
    public int deleteById(String id) {
        return dictEnumMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }

}
