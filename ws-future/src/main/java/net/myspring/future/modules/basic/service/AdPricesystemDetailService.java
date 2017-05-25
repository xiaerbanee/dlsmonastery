package net.myspring.future.modules.basic.service;

import net.myspring.future.modules.basic.domain.AdPricesystemDetail;
import net.myspring.future.modules.basic.repository.AdPricesystemDetailRepository;
import net.myspring.future.modules.basic.repository.AdPricesystemDetailRepository;
import net.myspring.future.modules.basic.repository.AdpricesystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdPricesystemDetailService {

    @Autowired
    private AdPricesystemDetailRepository adPricesystemDetailRepository;

    @Autowired
    private AdPricesystemDetailRepository adPricesystemDetailRepository;

    public AdPricesystemDetail findOne(String id){
        AdPricesystemDetail adPricesystemDetail=adPricesystemDetailRepository.findOne(id);
        return adPricesystemDetail;
    }
}
