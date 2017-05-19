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

import java.util.List;

@Service
public class AdPricesystemService {

    @Autowired
    private AdPricesystemMapper adPricesystemMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public AdPricesystem findOne(String id){
        AdPricesystem adPricesystem = adPricesystemMapper.findOne(id);
        return adPricesystem;
    }

    public Page<AdPricesystemDto> findPage(Pageable pageable, AdPricesystemQuery adPricesystemQuery) {
        Page<AdPricesystemDto> page = adPricesystemMapper.findPage(pageable, adPricesystemQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public AdPricesystemForm getForm(AdPricesystemForm adPricesystemForm){
        if(!adPricesystemForm.isCreate()){
            AdPricesystem adPricesystem=adPricesystemMapper.findOne(adPricesystemForm.getId());
            adPricesystemForm=BeanUtil.map(adPricesystem,AdPricesystemForm.class);
            cacheUtils.initCacheInput(adPricesystemForm);
        }
        return adPricesystemForm;
    }

    public List<AdPricesystemDto> findFilter(AdPricesystemQuery adPricesystemQuery){
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
        }
        List<Depot> depotList = depotMapper.findByIds(adPricesystemForm.getPageIds());
        for(Depot depot : depotList){
            if(!adPricesystemForm.getNewDepotIds().contains(depot.getId())){
                depot.setAdPricesystemId(null);
                depotMapper.update(depot);
            }else {
                if(!adPricesystemForm.getId().equals(depot.getAdPricesystemId())){
                    depot.setAdPricesystemId(adPricesystemForm.getId());
                    depotMapper.update(depot);
                }
            }
        }
        return adPricesystem;
    }

    public List<AdPricesystemDto> findAllEnabled(){
        List<AdPricesystem> adPricesystemList=adPricesystemMapper.findAllEnabled();
        List<AdPricesystemDto> adPricesystemDtoList= BeanUtil.map(adPricesystemList,AdPricesystemDto.class);
        return adPricesystemDtoList;
    }

    public void delete(AdPricesystemForm adPricesystemForm){
        adPricesystemForm.setEnabled(false);
        AdPricesystem adPricesystem = adPricesystemMapper.findOne(adPricesystemForm.getId());
        ReflectionUtil.copyProperties(adPricesystemForm,adPricesystem);
        adPricesystemMapper.update(adPricesystem);
    }
}
