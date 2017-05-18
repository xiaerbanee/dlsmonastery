package net.myspring.future.modules.basic.service;

import net.myspring.future.modules.basic.mapper.PricesystemDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PricesystemDetailService {

    @Autowired
    private PricesystemDetailMapper pricesystemDetailMapper;

}
