package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.modules.crm.domain.ShopAttribute;
import net.myspring.future.modules.crm.mapper.ShopAttributeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopAttributeService {

    @Autowired
    private ShopAttributeMapper shopAttributeMapper;

    public ShopAttribute findOne(String id) {
        return shopAttributeMapper.findOne(id);
    }

    public  List<ShopAttribute>  findByShopId(String shopId) {
        List<ShopAttribute> list = Lists.newArrayList();
        return list;
    }

}
