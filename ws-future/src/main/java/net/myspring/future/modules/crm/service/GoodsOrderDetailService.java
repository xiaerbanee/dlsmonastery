package net.myspring.future.modules.crm.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.crm.dto.GoodsOrderDetailDto;
import net.myspring.future.modules.crm.repository.GoodsOrderDetailRepository;
import net.myspring.future.modules.crm.web.query.GoodsOrderDetailQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GoodsOrderDetailService {

    @Autowired
    private GoodsOrderDetailRepository goodsOrderDetailRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<GoodsOrderDetailDto> findPage(Pageable pageable, GoodsOrderDetailQuery goodsOrderDetailQuery){
        Page<GoodsOrderDetailDto> page = goodsOrderDetailRepository.findPage(pageable,goodsOrderDetailQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }
}
