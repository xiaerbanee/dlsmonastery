package net.myspring.future.modules.crm.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.repository.StoreAllotImeRepository;
import net.myspring.future.modules.crm.dto.StoreAllotImeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StoreAllotImeService {

    @Autowired
    private StoreAllotImeRepository storeAllotImeRepository;

    @Autowired
    private CacheUtils cacheUtils;

    public List<StoreAllotImeDto> findByStoreAllotId(String id) {
        List<StoreAllotImeDto>  result  =storeAllotImeRepository.findByStoreAllotId(id);
        cacheUtils.initCacheInput(result);
       return  result;
    }
}
