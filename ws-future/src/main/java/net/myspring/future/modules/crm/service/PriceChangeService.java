package net.myspring.future.modules.crm.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.mapper.ProductTypeMapper;
import net.myspring.future.modules.crm.domain.PriceChange;
import net.myspring.future.modules.crm.dto.PriceChangeDto;
import net.myspring.future.modules.crm.mapper.PriceChangeImeMapper;
import net.myspring.future.modules.crm.mapper.PriceChangeMapper;
import net.myspring.future.modules.crm.mapper.PriceChangeProductMapper;
import net.myspring.future.modules.crm.web.query.PriceChangeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PriceChangeService {

    @Autowired
    private PriceChangeMapper priceChangeMapper;
    @Autowired
    private PriceChangeProductMapper priceChangeProductMapper;
    @Autowired
    private ProductTypeMapper productTypeMapper;
    @Autowired
    private PriceChangeImeMapper priceChangeImeMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public PriceChange findNearPriceChange(){
        return priceChangeMapper.findNearPriceChange();
    }


    public List<PriceChange> findAllByEnabledAndDate(LocalDateTime uploadEndDate) {
        return priceChangeMapper.finAllByEnabled(uploadEndDate);
    }

    public PriceChange findOne(String id){
        PriceChange priceChange = priceChangeMapper.findOne(id);
        return priceChange;
    }


    public Page<PriceChangeDto> findPage(Pageable pageable, PriceChangeQuery priceChangeQuery) {
        Page<PriceChangeDto> page = priceChangeMapper.findPage(pageable, priceChangeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    @Transactional
    public void save(PriceChange priceChange){
    }

    @Transactional
    public void delete(PriceChange priceChange){
        priceChange.setEnabled(false);
        priceChange.setName(priceChange.getName()+ LocalDate.now()+"废除");
        priceChangeMapper.update(priceChange);
    }

    @Transactional
    public void check(PriceChange priceChange){
    }


}
