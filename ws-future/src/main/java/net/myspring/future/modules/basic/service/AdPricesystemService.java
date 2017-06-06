package net.myspring.future.modules.basic.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.AdPricesystem;
import net.myspring.future.modules.basic.dto.AdPricesystemDto;
import net.myspring.future.modules.basic.repository.AdpricesystemRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdPricesystemService {

    @Autowired
    private AdpricesystemRepository adpricesystemRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public AdPricesystemDto findOne(String id){
        AdPricesystemDto adPricesystemDto = new AdPricesystemDto();
        if(StringUtils.isNotBlank(id)){
            AdPricesystem adPricesystem = adpricesystemRepository.findOne(id);
            adPricesystemDto = BeanUtil.map(adPricesystem,AdPricesystemDto.class);
            adPricesystemDto.setOfficeIdList(adpricesystemRepository.findOfficeById(id));
            cacheUtils.initCacheInput(adPricesystemDto);
        }
        return adPricesystemDto;
    }

    public Page<AdPricesystemDto> findPage(Pageable pageable, AdPricesystemQuery adPricesystemQuery) {
        Page<AdPricesystemDto> page = adpricesystemRepository.findPage(pageable, adPricesystemQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<AdPricesystemDto> findList(AdPricesystemQuery adPricesystemQuery){
        List<AdPricesystem> adPricesystemList = adpricesystemRepository.findList(adPricesystemQuery);
        List<AdPricesystemDto> adPricesystemDtoList = BeanUtil.map(adPricesystemList,AdPricesystemDto.class);
        cacheUtils.initCacheInput(adPricesystemDtoList);
        return adPricesystemDtoList;
    }

    public AdPricesystem save(AdPricesystemForm adPricesystemForm){
        AdPricesystem adPricesystem;
        if(adPricesystemForm.isCreate()){
            adPricesystem = BeanUtil.map(adPricesystemForm,AdPricesystem.class);
            adpricesystemRepository.save(adPricesystem);
        }else{
            adPricesystem = adpricesystemRepository.findOne(adPricesystemForm.getId());
            ReflectionUtil.copyProperties(adPricesystemForm,adPricesystem);
            adpricesystemRepository.save(adPricesystem);
            //清空adpricesystemOffice
            adpricesystemRepository.deleteOfficeId(adPricesystemForm.getId());

        }
        if(CollectionUtil.isNotEmpty(adPricesystemForm.getOfficeIdList())){
            adpricesystemRepository.saveAdpricesystemOffice(adPricesystem.getId(),adPricesystemForm.getOfficeIdList());
        }
        return adPricesystem;
    }

    public List<AdPricesystemDto> findAllEnabled(){
        List<AdPricesystem> adPricesystemList=adpricesystemRepository.findByEnabledIsTrue();
        List<AdPricesystemDto> adPricesystemDtoList= BeanUtil.map(adPricesystemList,AdPricesystemDto.class);
        return adPricesystemDtoList;
    }

    public void logicDelete(String id){
        adpricesystemRepository.logicDelete(id);
    }
}
