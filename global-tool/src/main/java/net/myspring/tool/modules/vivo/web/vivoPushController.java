package net.myspring.tool.modules.vivo.web;

import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.modules.vivo.domain.SProductItemLendM13e00;
import net.myspring.tool.modules.vivo.dto.SCustomerDto;
import net.myspring.tool.modules.vivo.dto.SPlantCustomerStockDetailDto;
import net.myspring.tool.modules.vivo.dto.SPlantCustomerStockDto;
import net.myspring.tool.modules.vivo.dto.VivoCustomerSaleImeiDto;
import net.myspring.tool.modules.vivo.service.IDvivoPushService;
import net.myspring.tool.modules.vivo.service.VivoPushService;
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
    private IDvivoPushService idvivoPushService;

    @RequestMapping(value = "pushVivoData")
    public void pushVivoData(String date){
        DbContextHolder.get().setCompanyName("JXVIVO");
        //机构数据
        vivoPushService.pushVivoZonesData();
        //客户数据
        List<SCustomerDto> futureCustomerDtoList = vivoPushService.getVivoCustomersData(date);
        vivoPushService.saveVivoPushSCustomers(futureCustomerDtoList,date);
        //库存汇总数据
        Map<String,String> productColorMap = vivoPushService.getProductColorMap();
        List<SPlantCustomerStockDto> sPlantCustomerStockDtoList = vivoPushService.getCustomerStockData(date);
        vivoPushService.pushCustomerStockData(sPlantCustomerStockDtoList,productColorMap,date);
        //库存串码明细
        List<SPlantCustomerStockDetailDto> sPlantCustomerStockDetailDtoList = vivoPushService.getCustomerStockDetailData(date);
        vivoPushService.pushCustomerStockDetailData(sPlantCustomerStockDetailDtoList,productColorMap,date);
        //演示机数据
        List<SProductItemLendM13e00> sProductItemLendM13e00List = vivoPushService.findDemoPhonesDate(date);
        vivoPushService.pushDemoPhones(sProductItemLendM13e00List,productColorMap,date);
        //核销记录数据
        List<VivoCustomerSaleImeiDto> vivoCustomerSaleImeiDtoList = vivoPushService.findProductImeSaleDate(date);
        vivoPushService.pushProductImeSale(vivoCustomerSaleImeiDtoList,productColorMap,date);
    }


    @RequestMapping(value = "pushIDvivoData")
    public void pushIDvivoData(String date){
        //同步机构数据
        DbContextHolder.get().setCompanyName("IDVIVO");
//        idvivoPushService.pushIDvivoZonesData();
    }

}
