package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.mapper.PriceChangeCommissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceChangeCommissionService {

    @Autowired
    private PriceChangeCommissionMapper priceChangeCommissionMapper;

}
