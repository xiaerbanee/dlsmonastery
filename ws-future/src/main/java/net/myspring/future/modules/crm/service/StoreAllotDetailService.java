package net.myspring.future.modules.crm.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.crm.repository.StoreAllotDetailRepository;
import net.myspring.future.modules.crm.dto.StoreAllotDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StoreAllotDetailService {

    @Autowired
    private StoreAllotDetailRepository storeAllotDetaiRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public List<StoreAllotDetailDto> findByStoreAllotId(String storeAllotId) {
        List<String> storeAllotIds = new ArrayList<>();
        storeAllotIds.add(storeAllotId);
        return storeAllotDetaiRepository.findByStoreAllotIds(storeAllotIds);
    }

}
