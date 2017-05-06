package net.myspring.future.modules.layout.service;

import net.myspring.future.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.client.CompanyConfigClient;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.layout.domain.ShopImage;
import net.myspring.future.modules.layout.dto.ShopImageDto;
import net.myspring.future.modules.layout.mapper.ShopImageMapper;
import net.myspring.future.modules.layout.web.form.ShopImageForm;
import net.myspring.future.modules.layout.web.query.ShopImageQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ShopImageService {

    @Autowired
    private ShopImageMapper shopImageMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private CompanyConfigClient companyConfigClient;

    public Page findPage(Pageable pageable, ShopImageQuery shopImageQuery){
        Page<ShopImageDto> page=shopImageMapper.findPage(pageable,shopImageQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public ShopImageForm findForm(ShopImageForm shopImageForm){
        if(!shopImageForm.isCreate()){
            ShopImage shopImage=shopImageMapper.findOne(shopImageForm.getId());
            shopImageForm = BeanUtil.map(shopImage,ShopImageForm.class);
            cacheUtils.initCacheInput(shopImageForm);
        }
        return shopImageForm;
    }

    public List<String> getQuery(){
        List<String> areaLists = new ArrayList<>();
        return areaLists;
    }

    public ShopImage save(ShopImageForm shopImageForm) {
        ShopImage shopImage;
        if(shopImageForm.isCreate()){
            shopImage =  BeanUtil.map(shopImageForm,ShopImage.class);
            shopImageMapper.save(shopImage);
        }else{
            shopImage = shopImageMapper.findOne(shopImageForm.getId());
            ReflectionUtil.copyProperties(shopImageForm,shopImage);
            shopImageMapper.update(shopImage);
        }
        return shopImage;
    }

    public void logicDelete(String id){
         shopImageMapper.logicDeleteOne(id);
    }

}
