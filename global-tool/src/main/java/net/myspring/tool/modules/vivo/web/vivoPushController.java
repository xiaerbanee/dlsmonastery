package net.myspring.tool.modules.vivo.web;

import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.modules.vivo.domain.SProductItemLend;
import net.myspring.tool.modules.vivo.dto.SCustomerDto;
import net.myspring.tool.modules.vivo.dto.SPlantCustomerStockDetailDto;
import net.myspring.tool.modules.vivo.dto.SPlantCustomerStockDto;
import net.myspring.tool.modules.vivo.dto.VivoCustomerSaleImeiDto;
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

    @RequestMapping(value = "pushVivoData")
    public void pushVivoData(String companyName,String date){
        DbContextHolder.get().setCompanyName(companyName);
        //机构数据
        vivoPushService.pushVivoZonesData();
        //客户数据
        List<SCustomerDto> futureCustomerDtoList = vivoPushService.getVivoCustomersData(date);
        vivoPushService.pushVivoPushSCustomersData(futureCustomerDtoList,date);
        //库存汇总数据
        Map<String,String> productColorMap = vivoPushService.getProductColorMap();
        List<SPlantCustomerStockDto> sPlantCustomerStockDtoList = vivoPushService.getCustomerStockData(date);
        vivoPushService.pushCustomerStockData(sPlantCustomerStockDtoList,productColorMap,date);
        //库存串码明细
        List<SPlantCustomerStockDetailDto> sPlantCustomerStockDetailDtoList = vivoPushService.getCustomerStockDetailData(date);
        vivoPushService.pushCustomerStockDetailData(sPlantCustomerStockDetailDtoList,productColorMap,date);
        //演示机数据
        List<SProductItemLend> sProductItemLendList = vivoPushService.getDemoPhonesData(date);
        vivoPushService.pushDemoPhonesData(sProductItemLendList,productColorMap,date);
        //核销记录数据
        List<VivoCustomerSaleImeiDto> vivoCustomerSaleImeiDtoList = vivoPushService.getProductImeSaleData(date);
        vivoPushService.pushProductImeSaleData(vivoCustomerSaleImeiDtoList,productColorMap,date);
        //一代仓库上抛
        vivoPushService.pushSStoreData();
    }

}
