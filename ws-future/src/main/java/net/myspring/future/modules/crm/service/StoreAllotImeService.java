package net.myspring.future.modules.crm.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.crm.dto.StoreAllotImeDto;
import net.myspring.future.modules.crm.mapper.StoreAllotImeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreAllotImeService {

    @Autowired
    private StoreAllotImeMapper storeAllotImeMapper;

    @Autowired
    private CacheUtils cacheUtils;

    public List<StoreAllotImeDto> findByStoreAllotId(String id) {
        List<StoreAllotImeDto>  result  =storeAllotImeMapper.findByStoreAllotId(id);
        cacheUtils.initCacheInput(result);
       return  result;
    }
}
