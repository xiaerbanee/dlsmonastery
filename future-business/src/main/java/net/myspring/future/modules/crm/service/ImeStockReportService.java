package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.mapper.ImeStockReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImeStockReportService {

    @Autowired
    private ImeStockReportMapper imeStockReportMapper;

}
