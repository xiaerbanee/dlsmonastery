package net.myspring.future.modules.basic.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.AdPricesystem;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotShop;
import net.myspring.future.modules.basic.dto.AdPricesystemDto;
import net.myspring.future.modules.basic.mapper.AdPricesystemMapper;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.mapper.DepotShopMapper;
import net.myspring.future.modules.basic.web.query.AdPricesystemQuery;
import net.myspring.future.modules.basic.web.form.AdPricesystemForm;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.utils.mapper.BeanMapper;
import sun.misc.Cache;

import java.util.List;

@Service
public class AdPricesystemService {

    @Autowired
    private AdPricesystemMapper adPricesystemMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public AdPricesystemDto findOne(String id){
        AdPricesystem adPricesystem = adPricesystemMapper.findOne(id);
        AdPricesystemDto adPricesystemDto = BeanUtil.map(adPricesystem,AdPricesystemDto.class);
        cacheUtils.initCacheInput(adPricesystemDto);
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
        adPricesystemMapper.saveOfficeIds(adPricesystem.getId(),adPricesystemForm.getOfficeIdList());
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
