package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.basic.mapper.PricesystemDetailMapper;
import net.myspring.future.modules.basic.mapper.PricesystemMapper;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.crm.domain.PricesystemChange;
import net.myspring.future.modules.crm.mapper.PricesystemChangeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class PricesystemChangeService {

    @Autowired
    private PricesystemChangeMapper pricesystemChangeMapper;
    @Autowired
    private PricesystemDetailMapper pricesystemDetailMapper;
    @Autowired
    private PricesystemMapper pricesystemMapper;
    @Autowired
    private ProductMapper productMapper;

    public PricesystemChange findOne(String id) {
        PricesystemChange pricesystemChange = pricesystemChangeMapper.findOne(id);
        return pricesystemChange;
    }

    public Page<PricesystemChange> findPage(Pageable pageable, Map<String, Object> map) {
        Page<PricesystemChange> page = pricesystemChangeMapper.findPage(pageable,map);
        return page;
    }

    @Transactional
    public void save(PricesystemChange pricesystemChange){
    }

    @Transactional
    public void audit(String[] ids,Boolean pass){
    }

    public List<List<Object>> getPricesystemDetail(List<String> productIdList){
        return null;
    }
}
