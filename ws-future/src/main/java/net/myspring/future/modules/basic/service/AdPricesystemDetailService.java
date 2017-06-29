package net.myspring.future.modules.basic.service;

import net.myspring.future.modules.basic.domain.AdPricesystemDetail;
import net.myspring.future.modules.basic.repository.AdPricesystemDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdPricesystemDetailService {

    @Autowired
    private AdPricesystemDetailRepository adPricesystemDetailRepository;

    public AdPricesystemDetail findOne(String id){
        AdPricesystemDetail adPricesystemDetail=adPricesystemDetailRepository.findOne(id);
        return adPricesystemDetail;
    }
}
