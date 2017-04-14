package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.PricesystemDetail;
import net.myspring.future.modules.crm.mapper.PricesystemDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PricesystemDetailService {

    @Autowired
    private PricesystemDetailMapper pricesystemDetailMapper;

    public PricesystemDetail findOne(String id){
        return pricesystemDetailMapper.findOne(id);
    }
}
