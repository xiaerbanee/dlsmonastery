package net.myspring.tool.modules.future.service;


import net.myspring.tool.common.dataSource.annotation.FutureDataSource;
import net.myspring.tool.modules.future.repository.FutureImeAllotRepository;
import net.myspring.tool.modules.oppo.domain.OppoCustomerImeiStock;
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
public class FutureImeAllotService {
    @Autowired
    private FutureImeAllotRepository futureImeAllotRepository;

    public List<OppoCustomerImeiStock> getFutureOppoCustomerImeiStock(String date){
        if(StringUtils.isEmpty(date)){
            date= LocalDateUtils.format(LocalDate.now());
        }
        String dateStart= date;
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<OppoCustomerImeiStock>  oppoCustomerImeiStocks=futureImeAllotRepository.findAll(dateStart,dateEnd);
        return oppoCustomerImeiStocks;
    }

}
