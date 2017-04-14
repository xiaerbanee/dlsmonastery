package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.mapper.StockCheckDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockCheckDetailService {

    @Autowired
    private StockCheckDetailMapper stockCheckDetailMapper;

}
