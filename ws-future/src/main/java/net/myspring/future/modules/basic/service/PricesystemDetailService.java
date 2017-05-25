package net.myspring.future.modules.basic.service;

import net.myspring.future.modules.basic.repository.PricesystemDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PricesystemDetailService {

    @Autowired
    private PricesystemDetailRepository pricesystemDetailRepository;

}
