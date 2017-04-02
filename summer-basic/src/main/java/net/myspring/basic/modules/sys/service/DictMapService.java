package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
import ma.glasnost.orika.metadata.Type;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.domain.DictMap;
import net.myspring.basic.modules.sys.dto.DictMapDto;
import net.myspring.basic.modules.sys.manager.DictMapManager;
import net.myspring.basic.modules.sys.web.form.DictMapForm;
import net.myspring.util.cahe.CacheReadUtils;
import net.myspring.util.mapper.BeanMapper;
import net.myspring.util.text.StringUtils;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import net.myspring.basic.modules.sys.mapper.DictMapMapper;

import java.util.List;
import java.util.Map;

@Service
public class DictMapService {

    @Autowired
    private DictMapManager dictMapManager;
    @Autowired
    private CacheUtils cacheUtils;

    public DictMap findOne(String id) {
        DictMap dictMap= dictMapManager.findOne(id);
        return dictMap;
    }

    public DictMapDto findDto(String id){
        DictMap dictMap=findOne(id);
        DictMapDto dictMapDto = BeanMapper.convertDto(dictMap, DictMapDto.class);
        cacheUtils.initCacheInput(dictMapDto);
        return dictMapDto;
    }

    public List<String> findDistinctCategory(){
        return dictMapManager.findDistinctCategory();
    }

    public Page<DictMapDto> findPage(Pageable pageable, Map<String, Object> map) {
        Page<DictMap> page = dictMapManager.findPage(pageable, map);
        Page<DictMapDto> dictMapDtoPage = BeanMapper.convertPage(page, DictMapDto.class);
        cacheUtils.initCacheInput(dictMapDtoPage.getContent());
        return dictMapDtoPage;
    }

    public DictMapForm save(DictMapForm dictMapForm){
        if(StringUtils.isBlank(dictMapForm.getId())){
            dictMapManager.save(dictMapForm);
        }else{
            dictMapManager.update(dictMapForm);
        }
        return  dictMapForm;
    }

    public void logicDeleteOne(String id) {
        dictMapManager.logicDeleteOne(id);
    }
}
