package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.AdPricesystemDetail;
import net.myspring.future.modules.crm.mapper.AdPricesystemDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdPricesystemDetailService {

    @Autowired
    private AdPricesystemDetailMapper adPricesystemDetailMapper;

    public AdPricesystemDetail findOne(String id){
        AdPricesystemDetail adPricesystemDetail=adPricesystemDetailMapper.findOne(id);
        return adPricesystemDetail;
    }
}
