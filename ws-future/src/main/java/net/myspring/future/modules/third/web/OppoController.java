package net.myspring.future.modules.third.web;


import net.myspring.future.modules.third.domain.OppoCustomerAllot;
import net.myspring.future.modules.third.service.OppoService;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.time.LocalDateTimeUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "third/factory/oppo")
public class OppoController {
    @Autowired
    private OppoService oppoService;

    @RequestMapping(value="synIme")
    public void synIme(String date){
        oppoService.synIme(date);
    }

    @RequestMapping(value="getOppoCustomerAllots")
    public String getOppoCustomerAllots(String dateStart,String dateEnd,String companyId){
        List<OppoCustomerAllot> oppoCustomerAllots=oppoService.findOppoCustomerAllots(LocalDateTimeUtils.parse(dateStart),LocalDateTimeUtils.parse(dateStart),companyId);
        return ObjectMapperUtils.writeValueAsString(oppoCustomerAllots);
    }
}
