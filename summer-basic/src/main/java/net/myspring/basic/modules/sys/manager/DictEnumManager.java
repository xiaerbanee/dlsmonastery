package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.hr.domain.Employee;
import net.myspring.basic.modules.sys.domain.DictEnum;
import net.myspring.basic.modules.sys.mapper.DictEnumMapper;
import net.myspring.basic.modules.sys.web.form.DictEnumForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
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
public class DictEnumManager {

    @Autowired
    private DictEnumMapper dictEnumMapper;

    @Cacheable(value = "dictEnums",key="#p0")
    public List<DictEnum> findByCategory(String category){
        return dictEnumMapper.findByCategory(category);
    }

    @Cacheable(value = "dictEnums",key="#p0")
    public DictEnum findOne(String id){
        return dictEnumMapper.findOne(id);
    }

    @Cacheable(value = "dictEnums",key="#p0.id")
    public DictEnum save(DictEnum dictEnum){
        dictEnumMapper.save(dictEnum);
        return  dictEnum;
    }

    @Cacheable(value = "dictEnums",key="#p0.id")
    public DictEnum saveForm(DictEnumForm dictEnumForm){
        dictEnumMapper.save(BeanUtil.map(dictEnumForm,DictEnum.class));
        return  dictEnumMapper.findOne(dictEnumForm.getId());
    }

    @CachePut(value = "dictEnums",key="#p0.id")
    public DictEnum update(DictEnum dictEnum){
        dictEnumMapper.update(dictEnum);
        return  dictEnumMapper.findOne(dictEnum.getId());
    }

    @CachePut(value = "dictEnums",key="#p0.id")
    public DictEnum updateForm(DictEnumForm dictEnumForm){
        dictEnumMapper.updateForm(dictEnumForm);
        return  dictEnumMapper.findOne(dictEnumForm.getId());
    }

    @CacheEvict(value = "dictEnums",key="#p0")
    public int deleteById(String id) {
        return dictEnumMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }

    public List<String> findValueByCategory(String category){
        List<DictEnum> dictEnumList=findByCategory(category);
        return CollectionUtil.extractToList(dictEnumList,"value");
    }

}
