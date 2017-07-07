package net.myspring.future.modules.api.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.api.domain.CarrierOrder;
import net.myspring.future.modules.api.domain.CarrierShop;
import net.myspring.future.modules.api.dto.CarrierShopDto;
import net.myspring.future.modules.api.repository.CarrierShopRepository;
import net.myspring.future.modules.api.web.form.CarrierShopForm;
import net.myspring.future.modules.api.web.query.CarrierShopQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.time.LocalDateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


/**
 * Created by zhucc on 2017/7/5.
 */
@Service
@Transactional(readOnly = true)
public class CarrierShopService {

    @Autowired
    private CarrierShopRepository carrierShopRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public CarrierShopDto findOne(String id) {
        CarrierShopDto carrierShopDto;
        if(StringUtils.isBlank(id)){
            carrierShopDto = new CarrierShopDto();
        } else {
            CarrierShop carrierShop= carrierShopRepository.findOne(id);
            carrierShopDto = BeanUtil.map(carrierShop,CarrierShopDto.class);
            cacheUtils.initCacheInput(carrierShopDto);
        }
        return carrierShopDto;
    }

    @Transactional(readOnly = false)
    public CarrierShop save(CarrierShopForm carrierShopForm) {
        CarrierShop carrierShop;
        if(carrierShopForm.isCreate()) {
            carrierShop = BeanUtil.map(carrierShopForm, CarrierShop.class);
            carrierShopRepository.save(carrierShop);
        } else {
            carrierShop = carrierShopRepository.findOne(carrierShopForm.getId());
            ReflectionUtil.copyProperties(carrierShopForm,carrierShop);
            carrierShop =  carrierShopRepository.save(carrierShop);
        }
        return carrierShop;
    }

    @Transactional(readOnly = false)
    public void logicDelete(String id) {
        CarrierShop carrierShop=carrierShopRepository.findOne(id);
        carrierShop.setName(carrierShop.getName()+"废弃"+ LocalDateUtils.format(LocalDate.now()));
        carrierShop.setEnabled(false);
        carrierShopRepository.save(carrierShop);
    }

    public Page<CarrierShopDto> findPage(Pageable pageable, CarrierShopQuery carrierShopQuery) {
        Page<CarrierShopDto> carrierShopDtoPage= carrierShopRepository.findPage(pageable, carrierShopQuery);
        cacheUtils.initCacheInput(carrierShopDtoPage.getContent());
        return carrierShopDtoPage;
    }

    public CarrierShop findByName(String name){
        CarrierShop carrierShop=carrierShopRepository.findByName(name);
        return carrierShop;
    }

    public CarrierShopDto findByNameLike(String name){
        CarrierShop carrierShop=carrierShopRepository.findByNameLike("%"+name+"%");
        CarrierShopDto carrierShopDto= BeanUtil.map(carrierShop,CarrierShopDto.class);
        return carrierShopDto;
    }

}
