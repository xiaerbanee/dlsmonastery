package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.domain.DictMap;
import net.myspring.basic.modules.sys.dto.DictMapDto;
import net.myspring.basic.modules.sys.manager.DictMapManager;
import net.myspring.basic.modules.sys.web.form.DictMapForm;
import net.myspring.basic.modules.sys.web.query.DictMapQuery;
import net.myspring.util.mapper.BeanUtil;
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
    private DictMapManager dictMapManager;
    @Autowired
    private DictMapMapper dictMapMapper;
    @Autowired
    private CacheUtils cacheUtils;


    public DictMapDto findDto(String id){
        DictMap dictMap= dictMapManager.findOne(id);
        DictMapDto dictMapDto = BeanUtil.map(dictMap, DictMapDto.class);
        cacheUtils.initCacheInput(dictMapDto);
        return dictMapDto;
    }

    public List<String> findDistinctCategory(){
        return dictMapMapper.findDistinctCategory();
    }

    public Page<DictMapDto> findPage(Pageable pageable, DictMapQuery dictMapQuery) {
        Page<DictMapDto> dictMapDtoPage = dictMapMapper.findPage(pageable, dictMapQuery);
        cacheUtils.initCacheInput(dictMapDtoPage.getContent());
        return dictMapDtoPage;
    }

    public DictMapForm save(DictMapForm dictMapForm){
        if(StringUtils.isBlank(dictMapForm.getId())){
            dictMapManager.saveForm(dictMapForm);
        }else{
            dictMapManager.updateForm(dictMapForm);
        }
        return  dictMapForm;
    }

    public  List<DictMap> findByCategory(String category){
        List<DictMap> dictMapList= Lists.newArrayList();
        if(StringUtils.isNotBlank(category)){
            dictMapList=dictMapMapper.findByCategory(category);
        }
        return dictMapList;
    }

    public void logicDeleteOne(String id) {
        dictMapMapper.logicDeleteOne(id);
    }
}
