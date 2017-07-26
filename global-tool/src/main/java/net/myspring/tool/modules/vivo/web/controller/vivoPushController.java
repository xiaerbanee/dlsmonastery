package net.myspring.tool.modules.vivo.web.controller;

import net.myspring.common.enums.CompanyNameEnum;
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
public class vivoPushController {

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


    @RequestMapping(value = "pushVivoData")
    public String pushVivoData(String companyName,String date){
        if(StringUtils.isBlank(RequestUtils.getCompanyName())) {
            DbContextHolder.get().setCompanyName(companyName);
        }
        if (CompanyNameEnum.IDVIVO.name().equals(DbContextHolder.get().getCompanyName())&&CompanyNameEnum.JXVIVO.name().equals(DbContextHolder.get().getCompanyName())){
            return "数据同步失败";
        }

        VivoPushDto vivoPushDto = new VivoPushDto();
        vivoPushDto.setDate(date);
        vivoPushDto.setProductColorMap(vivoPushService.getProductColorMap());
        if (CompanyNameEnum.JXVIVO.name().equals(DbContextHolder.get().getCompanyName())){
            vivoPushDto.setsCustomerDtoList(futureCustomerService.getVivoCustomersData(date));
            vivoPushDto.setsPlantCustomerStockDtoList(futureProductImeService.getVivoCustomerStockData(date));
            vivoPushDto.setsPlantCustomerStockDetailDtoList(futureProductImeService.getVivoCustomerStockDetailData(date));
            vivoPushDto.setsProductItemLendList(futureDemoPhoneService.getDemoPhonesData(date));
            vivoPushDto.setVivoCustomerSaleImeiDtoList(futureProductImeSaleService.getProductImeSaleData(date));
        }else {
            vivoPushDto.setsCustomerDtoList(futureCustomerService.getIDVivoCustomersData(date));
            vivoPushDto.setsPlantCustomerStockDtoList(futureProductImeService.getIDVivoCustomerStockData(date));
            vivoPushDto.setsPlantCustomerStockDetailDtoList(futureProductImeService.getIDVivoCustomerStockDetailData(date));
            vivoPushDto.setVivoCustomerSaleImeiDtoList(futureProductImeSaleService.getProductImeSaleData(date));
            vivoPushDto.setsStoresList(futureCustomerService.findIDvivoStore());
        }
        vivoPushService.pushToLocal(vivoPushDto);
        return "数据同步成功";
    }

}
