package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.ShopImage;
import net.myspring.future.modules.crm.mapper.DepotMapper;
import net.myspring.future.modules.crm.mapper.ShopImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class ShopImageService {

    @Autowired
    private ShopImageMapper shopImageMapper;
    @Autowired
    private DepotMapper depotMapper;

    public Page findPage(Pageable pageable, Map<String,Object> map){
        Page<ShopImage> page=shopImageMapper.findPage(pageable,map);
        return page;
    }

    public ShopImage findOne(String id){
        ShopImage shopImage=shopImageMapper.findOne(id);
        return shopImage;
    }

    public ShopImage save(ShopImage shopImage) {
        return shopImage;
    }

    public void logicDelete(String id){
         shopImageMapper.logicDeleteOne(id);
    }

}
