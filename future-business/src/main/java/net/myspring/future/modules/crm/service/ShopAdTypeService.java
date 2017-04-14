package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.modules.crm.domain.ShopAdType;
import net.myspring.future.modules.crm.mapper.ShopAdTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ShopAdTypeService {

    @Autowired
    private ShopAdTypeMapper shopAdTypeMapper;

    public List<ShopAdType> findAllByEnabled() {
        List<ShopAdType> shopAdTypes = shopAdTypeMapper.findAllByEnabled();
        return shopAdTypes;
    }

    public ShopAdType findOne(String id) {
        ShopAdType shopAdType = shopAdTypeMapper.findOne(id);
        return shopAdType;
    }

    public Page<ShopAdType> findPage(Pageable pageable, Map<String, Object> map) {
        Page<ShopAdType> page = shopAdTypeMapper.findPage(pageable, map);
        return page;
    }

    public ShopAdType save(ShopAdType shopAdType) {
        return shopAdType;
    }



    public void delete(ShopAdType shopAdType) {
        shopAdType.setEnabled(false);
        shopAdTypeMapper.update(shopAdType);
    }
}
