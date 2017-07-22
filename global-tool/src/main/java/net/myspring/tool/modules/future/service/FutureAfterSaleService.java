package net.myspring.tool.modules.future.service;

import net.myspring.tool.common.dataSource.annotation.FutureDataSource;
import net.myspring.tool.modules.future.repository.FutureAfterSaleRepository;
import net.myspring.tool.modules.future.repository.FutureStoreAllotRepository;
import net.myspring.tool.modules.oppo.domain.OppoCustomerAfterSaleImei;
import net.myspring.tool.modules.oppo.domain.OppoCustomerAllot;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@FutureDataSource
@Transactional(readOnly = true)
public class FutureAfterSaleService {
    @Autowired
    private FutureAfterSaleRepository futureAfterSaleRepository;


    public List<OppoCustomerAfterSaleImei> getFutureOppoCustomerAfterSaleImeis(String date){
        if(StringUtils.isEmpty(date)){
            date=LocalDateUtils.format(LocalDate.now());
        }
        String dateStart= date;
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<OppoCustomerAfterSaleImei> oppoCustomerAfterSaleImeis=futureAfterSaleRepository.findAll(dateStart,dateEnd);
        return oppoCustomerAfterSaleImeis;
    }


}
