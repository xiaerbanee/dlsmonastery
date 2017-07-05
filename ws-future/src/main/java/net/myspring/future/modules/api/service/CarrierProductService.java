package net.myspring.future.modules.api.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.api.domain.CarrierProduct;
import net.myspring.future.modules.api.domain.CarrierShop;
import net.myspring.future.modules.api.dto.CarrierProductDto;
import net.myspring.future.modules.api.dto.CarrierShopDto;
import net.myspring.future.modules.api.repository.CarrierProductRepository;
import net.myspring.future.modules.api.repository.CarrierShopRepository;
import net.myspring.future.modules.api.web.form.CarrierProductForm;
import net.myspring.future.modules.api.web.form.CarrierShopForm;
import net.myspring.future.modules.api.web.query.CarrierProductQuery;
import net.myspring.future.modules.api.web.query.CarrierShopQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.time.LocalDateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Created by wangzm on 2017/7/5.
 */
@Service
@Transactional(readOnly = true)
public class CarrierProductService {

    @Autowired
    private CarrierProductRepository carrierProductRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public CarrierProductDto findOne(String id) {
        CarrierProductDto carrierProductDto;
        if(StringUtils.isBlank(id)){
            carrierProductDto = new CarrierProductDto();
        } else {
            CarrierProduct carrierProduct= carrierProductRepository.findOne(id);
            carrierProductDto = BeanUtil.map(carrierProduct,CarrierProductDto.class);
            cacheUtils.initCacheInput(carrierProductDto);
        }
        return carrierProductDto;
    }

    @Transactional(readOnly = false)
    public CarrierProduct save(CarrierProductForm carrierProductForm) {
        CarrierProduct carrierProduct;
        if(carrierProductForm.isCreate()) {
            carrierProduct = BeanUtil.map(carrierProductForm, CarrierProduct.class);
            carrierProductRepository.save(carrierProduct);
        } else {
            carrierProduct = carrierProductRepository.findOne(carrierProductForm.getId());
            ReflectionUtil.copyProperties(carrierProductForm,carrierProduct);
            carrierProduct =  carrierProductRepository.save(carrierProduct);
        }
        return carrierProduct;
    }

    @Transactional(readOnly = false)
    public void logicDelete(String id) {
        CarrierProduct carrierProduct=carrierProductRepository.findOne(id);
        carrierProduct.setName(carrierProduct.getName()+"废弃"+ LocalDateUtils.format(LocalDate.now()));
        carrierProduct.setEnabled(false);
        carrierProductRepository.save(carrierProduct);
    }

    public Page<CarrierProductDto> findPage(Pageable pageable, CarrierProductQuery carrierProductQuery) {
        Page<CarrierProductDto> carrierShopDtoPage= carrierProductRepository.findPage(pageable, carrierProductQuery);
        cacheUtils.initCacheInput(carrierShopDtoPage.getContent());
        return carrierShopDtoPage;
    }

    public CarrierProduct findByName(String name){
        CarrierProduct carrierProduct=carrierProductRepository.findByName(name);
        return carrierProduct;
    }
}
