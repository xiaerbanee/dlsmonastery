package net.myspring.future.modules.basic.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.AdPricesystem;
import net.myspring.future.modules.basic.dto.AdPricesystemDto;
import net.myspring.future.modules.basic.mapper.AdPricesystemMapper;
import net.myspring.future.modules.basic.web.form.AdPricesystemForm;
import net.myspring.future.modules.basic.web.query.AdPricesystemQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdPricesystemService {

    @Autowired
    private AdPricesystemMapper adPricesystemMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public AdPricesystemDto findOne(String id){
        AdPricesystemDto adPricesystemDto;
        if(StringUtils.isBlank(id)){
            adPricesystemDto = new AdPricesystemDto();
        } else {
            AdPricesystem adPricesystem = adPricesystemMapper.findOne(id);
            adPricesystemDto = BeanUtil.map(adPricesystem,AdPricesystemDto.class);
            adPricesystemDto.setOfficeIdList(adPricesystemMapper.findOfficeById(adPricesystemDto.getId()));
            cacheUtils.initCacheInput(adPricesystemDto);
        }
        return adPricesystemDto;
    }

    public Page<AdPricesystemDto> findPage(Pageable pageable, AdPricesystemQuery adPricesystemQuery) {
        Page<AdPricesystemDto> page = adPricesystemMapper.findPage(pageable, adPricesystemQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<AdPricesystemDto> findList(AdPricesystemQuery adPricesystemQuery){
        List<AdPricesystem> adPricesystemList = adPricesystemMapper.findList(adPricesystemQuery);
        List<AdPricesystemDto> adPricesystemDtoList = BeanUtil.map(adPricesystemList,AdPricesystemDto.class);
        cacheUtils.initCacheInput(adPricesystemDtoList);
        return adPricesystemDtoList;
    }

    public AdPricesystem save(AdPricesystemForm adPricesystemForm){
        AdPricesystem adPricesystem;
        if(adPricesystemForm.isCreate()){
            adPricesystem = BeanUtil.map(adPricesystemForm,AdPricesystem.class);
            adPricesystemMapper.save(adPricesystem);
        }else{
            adPricesystem = adPricesystemMapper.findOne(adPricesystemForm.getId());
            ReflectionUtil.copyProperties(adPricesystemForm,adPricesystem);
            adPricesystemMapper.update(adPricesystem);
            adPricesystemMapper.deleteOfficeIds(adPricesystem.getId());
        }
        //修改机构绑定
        if(CollectionUtil.isNotEmpty(adPricesystemForm.getOfficeIdList())){
            adPricesystemMapper.saveOfficeIds(adPricesystem.getId(),adPricesystemForm.getOfficeIdList());
        }
        return adPricesystem;
    }

    public List<AdPricesystemDto> findAllEnabled(){
        List<AdPricesystem> adPricesystemList=adPricesystemMapper.findAllEnabled();
        List<AdPricesystemDto> adPricesystemDtoList= BeanUtil.map(adPricesystemList,AdPricesystemDto.class);
        return adPricesystemDtoList;
    }

    public void logicDeleteOne(String id){
        adPricesystemMapper.logicDeleteOne(id);
    }
}
