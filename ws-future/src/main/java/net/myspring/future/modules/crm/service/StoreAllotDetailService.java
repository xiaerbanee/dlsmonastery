package net.myspring.future.modules.crm.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.crm.dto.StoreAllotDetailDto;
import net.myspring.future.modules.crm.repository.StoreAllotDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class StoreAllotDetailService {

    @Autowired
    private StoreAllotDetailRepository storeAllotDetailRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public List<StoreAllotDetailDto> findByStoreAllotId(String storeAllotId) {

        List<StoreAllotDetailDto> result = storeAllotDetailRepository.findByStoreAllotIds(Collections.singletonList(storeAllotId));
        cacheUtils.initCacheInput(result);
        return result;
    }

}
