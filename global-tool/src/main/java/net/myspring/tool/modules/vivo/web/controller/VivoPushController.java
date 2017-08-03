package net.myspring.tool.modules.vivo.web.controller;

import net.myspring.common.enums.CompanyNameEnum;
import net.myspring.tool.common.client.OfficeClient;
import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.common.utils.RequestUtils;
import net.myspring.tool.modules.future.service.FutureCustomerService;
import net.myspring.tool.modules.future.service.FutureDemoPhoneService;
import net.myspring.tool.modules.future.service.FutureProductImeSaleService;
import net.myspring.tool.modules.future.service.FutureProductImeService;
import net.myspring.tool.modules.vivo.domain.SProductItemLend;
import net.myspring.tool.modules.vivo.dto.*;
import net.myspring.tool.modules.vivo.service.VivoPushService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "factory/vivo")
public class VivoPushController {

    @Autowired
    private VivoPushService vivoPushService;
    @Autowired
    private FutureCustomerService futureCustomerService;
    @Autowired
    private FutureProductImeService futureProductImeService;
    @Autowired
    private FutureDemoPhoneService futureDemoPhoneService;
    @Autowired
    private FutureProductImeSaleService futureProductImeSaleService;

    @RequestMapping(value = "pushToLocal")
    public String pushVivoData(String companyName,String date){
        if(StringUtils.isBlank(RequestUtils.getCompanyName())) {
            DbContextHolder.get().setCompanyName(companyName);
        }
        PushToLocalDto pushToLocalDto = new PushToLocalDto();
        pushToLocalDto.setDate(date);
        pushToLocalDto.setProductColorMap(vivoPushService.getProductColorMap());
        if (CompanyNameEnum.JXVIVO.name().equals(DbContextHolder.get().getCompanyName())){
            pushToLocalDto.setsCustomerDtoList(futureCustomerService.getVivoCustomersData(date));
            pushToLocalDto.setsPlantCustomerStockDtoList(futureProductImeService.getVivoCustomerStockData(date));
            pushToLocalDto.setsPlantCustomerStockDetailDtoList(futureProductImeService.getVivoCustomerStockDetailData(date));
            pushToLocalDto.setsProductItemLendList(futureDemoPhoneService.getDemoPhonesData(date));
            pushToLocalDto.setVivoCustomerSaleImeiDtoList(futureProductImeSaleService.getProductImeSaleData(date));
        }else {
            pushToLocalDto.setsCustomerDtoList(futureCustomerService.getIDVivoCustomersData(date));
            pushToLocalDto.setsPlantCustomerStockDtoList(futureProductImeService.getIDVivoCustomerStockData(date));
            pushToLocalDto.setsPlantCustomerStockDetailDtoList(futureProductImeService.getIDVivoCustomerStockDetailData(date));
            pushToLocalDto.setVivoCustomerSaleImeiDtoList(futureProductImeSaleService.getProductImeSaleData(date));
            pushToLocalDto.setsStoresList(futureCustomerService.findIDvivoStore());
        }
        vivoPushService.pushToLocal(pushToLocalDto);
        return "数据同步成功";
    }

    @RequestMapping(value = "pushFactoryData")
    public String pushFactoryData(String companyName,String date){
        if (StringUtils.isBlank(RequestUtils.getCompanyName())){
            DbContextHolder.get().setCompanyName(companyName);
        }
        VivoPushDto vivoPushDto = vivoPushService.getPushFactoryDate(date);
        vivoPushService.pushFactoryData(vivoPushDto,date);
        return "数据上抛成功";
    }


}
