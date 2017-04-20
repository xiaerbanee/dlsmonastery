package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.mapper.StockCheckMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockCheckService {

    @Autowired
    private StockCheckMapper stockCheckMapper;

}
