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

    public ShopImageForm findOne(String id){
        ShopImage shopImage=shopImageMapper.findOne(id);
        ShopImageForm shopImageForm = BeanUtil.map(shopImage,ShopImageForm.class);
        String shopName = depotMapper.findOne(shopImageForm.getShopId()).getName();
        shopImageForm.setShopName(shopName);
        return shopImageForm;
    }

    public List<String> getProperty(){
        List<String> imageTypeProperties = new ArrayList<>();
        String[] shopImageTypes = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.SHOP_IMAGE_TYPE.getCode()).split(",");
        for(String shopImageType:shopImageTypes){
            imageTypeProperties.add(shopImageType);
        }
        return imageTypeProperties;
    }

    public ShopImage save(ShopImageForm shopImageForm) {
        ShopImage shopImage = new ShopImage();
        if(shopImageForm.isCreate()){
            shopImage =  BeanUtil.map(shopImageForm,ShopImage.class);
            shopImageMapper.save(shopImage);
        }else{
            shopImageMapper.updateForm(shopImageForm);
        }
        return shopImage;
    }

    public void logicDelete(String id){
         shopImageMapper.logicDeleteOne(id);
    }

}
