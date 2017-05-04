package net.myspring.basic.modules.sys.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.domain.DictEnum;
import net.myspring.basic.modules.sys.dto.DictEnumDto;
import net.myspring.basic.modules.sys.mapper.DictEnumMapper;
import net.myspring.basic.modules.sys.web.form.DictEnumForm;
import net.myspring.basic.modules.sys.web.query.DictEnumQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictEnumService {
    @Autowired
    private DictEnumMapper dictEnumMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public List<String> findValueByCategory(String category){
        List<DictEnum> dictEnumList=dictEnumMapper.findByCategory(category);
        return CollectionUtil.extractToList(dictEnumList,"value");
    }

    public  List<DictEnumDto> findByCategory(String category){
        List<DictEnum> dictEnumList=dictEnumMapper.findByCategory(category);
        List<DictEnumDto> dictEnumDtoList= BeanUtil.map(dictEnumList,DictEnumDto.class);
        cacheUtils.initCacheInput(dictEnumDtoList);
        return dictEnumDtoList;
    }


    public DictEnumForm findForm(DictEnumForm dictEnumForm) {
        if(!dictEnumForm.isCreate()) {
            DictEnum dictEnum =dictEnumMapper.findOne(dictEnumForm.getId());
            dictEnumForm= BeanUtil.map(dictEnum,DictEnumForm.class);
            cacheUtils.initCacheInput(dictEnumForm);
        }
        return dictEnumForm;
    }

    public DictEnum save(DictEnumForm dictEnumForm) {
        DictEnum dictEnum;
        if(dictEnumForm.isCreate()) {
            dictEnum = BeanUtil.map(dictEnumForm, DictEnum.class);
            dictEnumMapper.save(dictEnum);
        } else {
            dictEnum = dictEnumMapper.findOne(dictEnumForm.getId());
            ReflectionUtil.copyProperties(dictEnumForm,dictEnum);
            dictEnumMapper.update(dictEnum);
        }
        return dictEnum;
    }


    public void logicDeleteOne(String id) {
        dictEnumMapper.logicDeleteOne(id);
    }

    public Page<DictEnumDto> findPage(Pageable pageable, DictEnumQuery dictEnumQuery) {
        Page<DictEnumDto> dictEnumDtoPage= dictEnumMapper.findPage(pageable, dictEnumQuery);
        cacheUtils.initCacheInput(dictEnumDtoPage.getContent());
        return dictEnumDtoPage;
    }

    public List<String> findDistinctCategory(){
        return dictEnumMapper.findDistinctCategory();
    }

}
