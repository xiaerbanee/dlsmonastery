package net.myspring.future.modules.layout.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.layout.domain.ShopImage;
import net.myspring.future.modules.layout.dto.ShopImageDto;
import net.myspring.future.modules.layout.mapper.ShopImageMapper;
import net.myspring.future.modules.layout.web.query.ShopImageQuery;
import net.myspring.util.mapper.BeanUtil;
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
    @Autowired
    private CacheUtils cacheUtils;

    public Page findPage(Pageable pageable, ShopImageQuery shopImageQuery){
        Page<ShopImageDto> page=shopImageMapper.findPage(pageable,shopImageQuery);
        cacheUtils.initCacheInput(page.getContent());
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
