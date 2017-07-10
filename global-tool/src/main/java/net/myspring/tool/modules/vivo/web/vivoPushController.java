package net.myspring.tool.modules.vivo.web;

import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.modules.vivo.dto.FutureCustomerDto;
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
        //获取机构数据
        //vivoPushService.getVivoZones(date);
        //获取客户数据
        List<FutureCustomerDto> futureCustomerDtoList = vivoPushService.getFutureVivoCustomers(date);
        vivoPushService.saveVivoPushSCustomers(futureCustomerDtoList);
    }
}
