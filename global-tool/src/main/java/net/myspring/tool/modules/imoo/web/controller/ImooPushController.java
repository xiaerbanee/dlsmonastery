package net.myspring.tool.modules.imoo.web.controller;

import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.modules.future.service.FutureCustomerService;
import net.myspring.tool.modules.imoo.dto.ImooPushDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "factory/imoo")
public class ImooPushController {
    @Autowired
    private FutureCustomerService futureCustomerService;

    @RequestMapping(value = "pushToLocal")
    public String pushToLocal(String companyName,String date){
        DbContextHolder.get().setCompanyName(companyName);
        ImooPushDto imooPushDto = new ImooPushDto();
        imooPushDto.setsCustomerDtoList(futureCustomerService.getImooCustomers());
        return "数据同步至本地成功";
    }


}
