package net.myspring.basic.modules.sys.service;

import com.google.common.collect.HashBiMap;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.domain.DictMap;
import net.myspring.basic.modules.sys.dto.DictMapDto;
import net.myspring.basic.modules.sys.web.form.DictMapForm;
import net.myspring.basic.modules.sys.web.query.DictMapQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import net.myspring.basic.modules.sys.mapper.DictMapMapper;

import java.util.List;

@Service
public class DictMapService {
    
    @Autowired
    private DictMapMapper dictMapMapper;
    @Autowired
    private CacheUtils cacheUtils;


    public DictMapForm findForm(DictMapForm dictMapForm){
        if(!dictMapForm.isCreate()){
            DictMap dictMap= dictMapMapper.findOne(dictMapForm.getId());
            dictMapForm = BeanUtil.map(dictMap, DictMapForm.class);
            cacheUtils.initCacheInput(dictMapForm);
        }
        return dictMapForm;
    }

    public List<String> findDistinctCategory(){
        return dictMapMapper.findDistinctCategory();
    }

    public Page<DictMapDto> findPage(Pageable pageable, DictMapQuery dictMapQuery) {
        Page<DictMapDto> dictMapDtoPage = dictMapMapper.findPage(pageable, dictMapQuery);
        cacheUtils.initCacheInput(dictMapDtoPage.getContent());
        return dictMapDtoPage;
    }

    public DictMap save(DictMapForm dictMapForm){
        DictMap dictMap;
        if(StringUtils.isBlank(dictMapForm.getId())){
            dictMap=BeanUtil.map(dictMapForm,DictMap.class);
            dictMapMapper.save(dictMap);
        }else{
            dictMap = dictMapMapper.findOne(dictMapForm.getId());
            ReflectionUtil.copyProperties(dictMapForm,dictMap);
            dictMapMapper.update(dictMap);
        }
        return  dictMap;
    }

    public HashBiMap<String,String> getDictMapList(String category) {
        List<DictMap> dictMaps = dictMapMapper.findByCategory(category);
        HashBiMap<String,String> map = HashBiMap.create();
        for (DictMap dictMap : dictMaps) {
            map.put(dictMap.toString(),dictMap.getName());
        }
        return map;
    }

    public void logicDeleteOne(String id) {
        dictMapMapper.logicDeleteOne(id);
    }

    public List<DictMap> findByCategory(String category){
        List<DictMap> dictMapList=dictMapMapper.findByCategory(category);
        return dictMapList;
    }

    public DictMapDto findByName(String name){
        DictMapDto dictMapDto = dictMapMapper.findByName(name);
        return  dictMapDto;
    }
}
