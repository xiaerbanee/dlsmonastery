package net.myspring.future.modules.layout.service;

import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.layout.domain.ShopImage;
import net.myspring.future.modules.layout.dto.ShopImageDto;
import net.myspring.future.modules.layout.repository.ShopImageRepository;
import net.myspring.future.modules.layout.web.form.ShopImageForm;
import net.myspring.future.modules.layout.web.query.ShopImageQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class ShopImageService {

    @Autowired
    private ShopImageRepository shopImageRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private RedisTemplate redisTemplate;

    public Page findPage(Pageable pageable, ShopImageQuery shopImageQuery){
        Page<ShopImageDto> page=shopImageRepository.findPage(pageable,shopImageQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public ShopImageDto findOne(String id){
        ShopImageDto shopImageDto = new ShopImageDto();
        if(StringUtils.isNotBlank(id)){
            ShopImage shopImage=shopImageRepository.findOne(id);
            shopImageDto = BeanUtil.map(shopImage,ShopImageDto.class);
            cacheUtils.initCacheInput(shopImageDto);
        }
        return shopImageDto;
    }

    public ShopImageForm getForm(ShopImageForm shopImageForm){
        List<String> imageTypeList= Arrays.asList(CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.SHOP_IMAGE_TYPE.name()).getValue().split(","));
        shopImageForm.setImageTypeList(imageTypeList);
        return shopImageForm;
    }

    public ShopImageQuery getQuery(ShopImageQuery shopImageQuery){
        return shopImageQuery;
    }

    public ShopImage save(ShopImageForm shopImageForm) {
        ShopImage shopImage;
        if(shopImageForm.isCreate()){
            shopImage =  BeanUtil.map(shopImageForm,ShopImage.class);
            shopImageRepository.save(shopImage);
        }else{
            shopImage = shopImageRepository.findOne(shopImageForm.getId());
            ReflectionUtil.copyProperties(shopImageForm,shopImage);
            shopImageRepository.save(shopImage);
        }
        return shopImage;
    }

    public void logicDelete(String id){
        shopImageRepository.logicDeleteOne(id);
    }

}
