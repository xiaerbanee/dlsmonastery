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
import net.myspring.basic.modules.sys.repository.DictMapRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DictMapService {

    @Autowired
    private DictMapRepository dictMapRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public DictMapDto findOne(DictMapDto dictMapDto) {
        if (!dictMapDto.isCreate()) {
            DictMap dictMap = dictMapRepository.findOne(dictMapDto.getId());
            dictMapDto = BeanUtil.map(dictMap, DictMapDto.class);
            cacheUtils.initCacheInput(dictMapDto);
        }
        return dictMapDto;
    }

    public List<String> findDistinctCategory() {
        return dictMapRepository.findDistinctCategory();
    }

    public Page<DictMapDto> findPage(Pageable pageable, DictMapQuery dictMapQuery) {
        Page<DictMapDto> dictMapDtoPage = dictMapRepository.findPage(pageable, dictMapQuery);
        cacheUtils.initCacheInput(dictMapDtoPage.getContent());
        return dictMapDtoPage;
    }

    public DictMap save(DictMapForm dictMapForm) {
        DictMap dictMap;
        if (StringUtils.isBlank(dictMapForm.getId())) {
            dictMap = BeanUtil.map(dictMapForm, DictMap.class);
            dictMapRepository.save(dictMap);
        } else {
            dictMap = dictMapRepository.findOne(dictMapForm.getId());
            ReflectionUtil.copyProperties(dictMapForm, dictMap);
            dictMapRepository.save(dictMap);
        }
        return dictMap;
    }

    public HashBiMap<String, String> getDictMapList(String category) {
        List<DictMap> dictMaps = dictMapRepository.findByCategory(category);
        HashBiMap<String, String> map = HashBiMap.create();
        for (DictMap dictMap : dictMaps) {
            map.put(dictMap.toString(), dictMap.getName());
        }
        return map;
    }

    public void logicDelete(String id) {
        dictMapRepository.logicDelete(id);
    }

    public List<DictMap> findByCategory(String category) {
        List<DictMap> dictMapList = dictMapRepository.findByCategory(category);
        return dictMapList;
    }

    public String findByName(String name) {
        DictMap dictMap = dictMapRepository.findByName(name);
        if(dictMap!=null){
            return dictMap.getRemarks();
        }else {
            return null;
        }

    }
}
