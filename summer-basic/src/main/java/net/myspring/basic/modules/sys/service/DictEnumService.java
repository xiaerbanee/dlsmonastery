package net.myspring.basic.modules.sys.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.domain.DictEnum;
import net.myspring.basic.modules.sys.dto.DictEnumDto;
import net.myspring.basic.modules.sys.repository.DictEnumRepository;
import net.myspring.basic.modules.sys.web.form.DictEnumForm;
import net.myspring.basic.modules.sys.web.query.DictEnumQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DictEnumService {
    @Autowired
    private DictEnumRepository dictEnumRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public DictEnumDto findOne(String id) {
        DictEnumDto dictEnumDto;
        if(StringUtils.isBlank(id)){
            dictEnumDto = new DictEnumDto();
        } else {
            DictEnum dictEnum= dictEnumRepository.findOne(id);
            dictEnumDto = BeanUtil.map(dictEnum,DictEnumDto.class);
            cacheUtils.initCacheInput(dictEnumDto);
        }
        return dictEnumDto;
    }

    public List<String> findValueByCategory(String category){
        List<DictEnum> dictEnumList=dictEnumRepository.findByCategory(category);
        return CollectionUtil.extractToList(dictEnumList,"value");
    }

    public  List<DictEnumDto> findByCategory(String category){
        List<DictEnum> dictEnumList=dictEnumRepository.findByCategory(category);
        List<DictEnumDto> dictEnumDtoList= BeanUtil.map(dictEnumList,DictEnumDto.class);
        cacheUtils.initCacheInput(dictEnumDtoList);
        return dictEnumDtoList;
    }

    public  List<DictEnumDto> findByCategoryList(List<String> categoryList){
        List<DictEnum> dictEnumList=dictEnumRepository.findByCategoryIn(categoryList);
        List<DictEnumDto> dictEnumDtoList= BeanUtil.map(dictEnumList,DictEnumDto.class);
        cacheUtils.initCacheInput(dictEnumDtoList);
        return dictEnumDtoList;
    }


    public String findByValue(String value,String category){
        DictEnum dictEnum = dictEnumRepository.findByValue(value,category);
        if(dictEnum!=null){
            return dictEnum.getRemarks();
        }else{
            return null;
        }
    }

    @Transactional
    public DictEnum save(DictEnumForm dictEnumForm) {
        DictEnum dictEnum;
        if(dictEnumForm.isCreate()) {
            dictEnum = BeanUtil.map(dictEnumForm, DictEnum.class);
            dictEnumRepository.save(dictEnum);
        } else {
            dictEnum = dictEnumRepository.findOne(dictEnumForm.getId());
            ReflectionUtil.copyProperties(dictEnumForm,dictEnum);
            dictEnum =  dictEnumRepository.save(dictEnum);
        }
        return dictEnum;
    }

    @Transactional
    public void logicDelete(String id) {
        dictEnumRepository.logicDelete(id);
    }

    public Page<DictEnumDto> findPage(Pageable pageable, DictEnumQuery dictEnumQuery) {
        Page<DictEnumDto> dictEnumDtoPage= dictEnumRepository.findPage(pageable, dictEnumQuery);
        cacheUtils.initCacheInput(dictEnumDtoPage.getContent());
        return dictEnumDtoPage;
    }

    public List<String> findDistinctCategory(){
        return dictEnumRepository.findDistinctCategory();
    }

}
