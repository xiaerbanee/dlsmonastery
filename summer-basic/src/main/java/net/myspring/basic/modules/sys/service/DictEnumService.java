package net.myspring.basic.modules.sys.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.domain.DictEnum;
import net.myspring.basic.modules.sys.dto.DictEnumDto;
import net.myspring.basic.modules.sys.manager.DictEnumManager;
import net.myspring.basic.modules.sys.mapper.DictEnumMapper;
import net.myspring.basic.modules.sys.web.form.DictEnumForm;
import net.myspring.util.mapper.BeanMapper;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DictEnumService {

    @Autowired
    private DictEnumManager dictEnumManager;
    @Autowired
    private DictEnumMapper dictEnumMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public List<String> findValueByCategory(String category){
        return  dictEnumManager.findValueByCategory(category);
    }

    public  List<DictEnumDto> findByCategory(String category){
        List<DictEnum> dictEnumList=dictEnumManager.findByCategory(category);
        List<DictEnumDto> dictEnumDtoList=BeanMapper.convertDtoList(dictEnumList,DictEnumDto.class);
        cacheUtils.initCacheInput(dictEnumDtoList);
        return dictEnumDtoList;
    }

    public DictEnum findOne(String id) {
        DictEnum dictEnum = dictEnumManager.findOne(id);
        return dictEnum;
    }

    public DictEnumDto findDto(String id) {
        DictEnum dictEnum =findOne(id);
        DictEnumDto dictEnumDto=BeanMapper.convertDto(dictEnum,DictEnumDto.class);
        cacheUtils.initCacheInput(dictEnumDto);
        return dictEnumDto;
    }

    public DictEnum save(DictEnumForm dictEnumForm) {
        boolean isCreate= StringUtils.isBlank(dictEnumForm.getId());
        if(isCreate) {
            dictEnumManager.save(dictEnumForm);
        } else {
            dictEnumManager.update(dictEnumForm);
        }
        return dictEnumForm;
    }


    public void logicDeleteOne(String id) {
        dictEnumMapper.logicDeleteOne(id);
    }

    public Page<DictEnumDto> findPage(Pageable pageable, Map<String, Object> map) {
        Page<DictEnum> page = dictEnumMapper.findPage(pageable, map);
        Page<DictEnumDto> dictEnumDtoPage= BeanMapper.convertPage(page,DictEnumDto.class);
        cacheUtils.initCacheInput(dictEnumDtoPage.getContent());
        return dictEnumDtoPage;
    }

    public List<String> findDistinctCategory(){
        return dictEnumMapper.findDistinctCategory();
    }

}
