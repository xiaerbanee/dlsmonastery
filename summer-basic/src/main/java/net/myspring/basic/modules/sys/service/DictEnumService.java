package net.myspring.basic.modules.sys.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.domain.DictEnum;
import net.myspring.basic.modules.sys.dto.DictEnumDto;
import net.myspring.basic.modules.sys.manager.DictEnumManager;
import net.myspring.basic.modules.sys.mapper.DictEnumMapper;
import net.myspring.basic.modules.sys.web.form.DictEnumForm;
import net.myspring.basic.modules.sys.web.query.DictEnumQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<DictEnum> dictEnumList=dictEnumMapper.findByCategory(category);
        List<DictEnumDto> dictEnumDtoList= BeanUtil.map(dictEnumList,DictEnumDto.class);
        cacheUtils.initCacheInput(dictEnumDtoList);
        return dictEnumDtoList;
    }


    public DictEnumDto findDto(String id) {
        DictEnum dictEnum =dictEnumManager.findOne(id);
        DictEnumDto dictEnumDto= BeanUtil.map(dictEnum,DictEnumDto.class);
        cacheUtils.initCacheInput(dictEnumDto);
        return dictEnumDto;
    }

    public DictEnumForm save(DictEnumForm dictEnumForm) {
        if(dictEnumForm.isCreate()) {
            DictEnum dictEnum = BeanUtil.map(dictEnumForm, DictEnum.class);
            dictEnumManager.save(dictEnum);
        } else {
            dictEnumManager.updateForm(dictEnumForm);
        }
        return dictEnumForm;
    }


    public void logicDeleteOne(String id) {
        dictEnumMapper.logicDeleteOne(id);
    }

    public Page<DictEnumDto> findPage(Pageable pageable, DictEnumQuery dictEnumQuery) {
        Page<DictEnum> page = dictEnumMapper.findPage(pageable, dictEnumQuery);
        Page<DictEnumDto> dictEnumDtoPage= BeanUtil.map(page,DictEnumDto.class);
        cacheUtils.initCacheInput(dictEnumDtoPage.getContent());
        return dictEnumDtoPage;
    }

    public List<String> findDistinctCategory(){
        return dictEnumMapper.findDistinctCategory();
    }

}
