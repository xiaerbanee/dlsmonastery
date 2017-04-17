package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.domain.DictEnum;
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


    public DictMapForm findForm(DictMapForm dictMapForm){
        if(!dictMapForm.isCreate()){
            DictMap dictMap= dictMapManager.findOne(dictMapForm.getId());
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
            dictMap=dictMapManager.save(dictMap);
        }else{
            dictMap=dictMapManager.updateForm(dictMapForm);
        }
        return  dictMap;
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
