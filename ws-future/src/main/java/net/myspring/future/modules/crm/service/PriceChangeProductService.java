package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.mapper.PriceChangeProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceChangeProductService {

    @Autowired
    private PriceChangeProductMapper priceChangeProductMapper;

}
