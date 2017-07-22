package net.myspring.tool.modules.future.service;

import net.myspring.tool.common.dataSource.annotation.FutureDataSource;
import net.myspring.tool.modules.future.repository.FutureStoreAllotRepository;
import net.myspring.tool.modules.oppo.domain.OppoCustomerAllot;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@FutureDataSource
public class FutureStoreAllotService {
    @Autowired
    private FutureStoreAllotRepository futureStoreAllotRepository;

    public List<OppoCustomerAllot> getFutureOppoCustomerAllot(String date){
        if(StringUtils.isEmpty(date)){
            date= LocalDateUtils.format(LocalDate.now());
        }
        String dateStart= date;
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<OppoCustomerAllot> oppoCustomerAllots=futureStoreAllotRepository.findAll(dateStart,dateEnd);
        return oppoCustomerAllots;
    }
}
