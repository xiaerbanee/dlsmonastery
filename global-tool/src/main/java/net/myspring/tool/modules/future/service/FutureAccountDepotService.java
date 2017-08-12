package net.myspring.tool.modules.future.service;

import net.myspring.tool.common.utils.CacheUtils;
import net.myspring.tool.modules.future.dto.AccountDepotDto;
import net.myspring.tool.modules.future.repository.FutureAccountDepotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FutureAccountDepotService {
    @Autowired
    private FutureAccountDepotRepository futureAccountDepotRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public List<AccountDepotDto> findAll(){
        List<AccountDepotDto> accountDepotDtoList = futureAccountDepotRepository.findAll();
        cacheUtils.initCacheInput(accountDepotDtoList);
        return accountDepotDtoList;
    }
}
