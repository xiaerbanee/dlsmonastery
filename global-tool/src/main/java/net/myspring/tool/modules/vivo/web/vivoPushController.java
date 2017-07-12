package net.myspring.tool.modules.vivo.web;

import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.modules.vivo.domain.SCustomersM13e00;
import net.myspring.tool.modules.vivo.dto.SCustomerDto;
import net.myspring.tool.modules.vivo.service.VivoPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "vivo")
public class vivoPushController {
    @Autowired
    private VivoPushService vivoPushService;

    @RequestMapping(value = "pushVivoData")
    public void pushVivoData(String date){
        DbContextHolder.get().setCompanyName("JXVIVO");
        //机构数据
        vivoPushService.pushVivoZonesData();
        //客户数据
        DbContextHolder.get().setCompanyName("JXOPPO");
        List<SCustomerDto> futureCustomerDtoList = vivoPushService.getVivoCustomersData(date);
        DbContextHolder.get().setCompanyName("JXVIVO");
        vivoPushService.saveVivoPushSCustomers(futureCustomerDtoList,date);
    }
}
