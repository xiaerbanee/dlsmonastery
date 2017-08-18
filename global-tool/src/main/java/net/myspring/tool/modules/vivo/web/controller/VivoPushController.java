package net.myspring.tool.modules.vivo.web.controller;

import com.google.common.collect.Lists;
import net.myspring.common.enums.CompanyNameEnum;
import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.common.utils.RequestUtils;
import net.myspring.tool.modules.future.service.FutureCustomerService;
import net.myspring.tool.modules.future.service.FutureDemoPhoneService;
import net.myspring.tool.modules.future.service.FutureProductImeSaleService;
import net.myspring.tool.modules.future.service.FutureProductImeService;
import net.myspring.tool.modules.vivo.domain.SProductItem000;
import net.myspring.tool.modules.vivo.dto.*;
import net.myspring.tool.modules.vivo.service.VivoPushService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


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

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "pushToLocal")
    public String pushVivoData(String companyName,String date){
        logger.info("开始同步数据至中转库:"+ LocalDateTime.now());
        DbContextHolder.get().setCompanyName(companyName);
        PushToLocalDto pushToLocalDto = new PushToLocalDto();
        pushToLocalDto.setDate(date);
        pushToLocalDto.setProductColorMap(vivoPushService.getProductColorMap());

        logger.info("获取业务数据开始:"+LocalDateTime.now());
        if (CompanyNameEnum.JXVIVO.name().equals(DbContextHolder.get().getCompanyName())){
            pushToLocalDto.setsCustomerDtoList(futureCustomerService.getVivoCustomersData(date));
            pushToLocalDto.setsPlantCustomerStockDtoList(futureProductImeService.getVivoCustomerStockData(date));
            pushToLocalDto.setsPlantCustomerStockDetailDtoList(futureProductImeService.getVivoCustomerStockDetailData(date));
            pushToLocalDto.setsProductItemLendList(futureDemoPhoneService.getDemoPhonesData(date));
            pushToLocalDto.setVivoCustomerSaleImeiDtoList(futureProductImeSaleService.getProductImeSaleData(date));
        }else if(CompanyNameEnum.IDVIVO.name().equals(DbContextHolder.get().getCompanyName())){
            pushToLocalDto.setsCustomerDtoList(futureCustomerService.getIDVivoCustomersData(date));
            pushToLocalDto.setsPlantCustomerStockDtoList(futureProductImeService.getIDVivoCustomerStockData(date));
            pushToLocalDto.setsPlantCustomerStockDetailDtoList(futureProductImeService.getIDVivoCustomerStockDetailData(date));
            pushToLocalDto.setVivoCustomerSaleImeiDtoList(futureProductImeSaleService.getProductImeSaleData(date));
            pushToLocalDto.setsStoresList(futureCustomerService.findIDvivoStore());
        }else {
            logger.info("同步数据失败至中转库失败");
            return "数据同步失败";
        }
        logger.info("获取业务数据结束:"+LocalDateTime.now());

        vivoPushService.pushToLocal(pushToLocalDto,companyName);
        logger.info("同步数据至中转库结束:"+ LocalDateTime.now());
        return "数据同步成功";
    }

    @RequestMapping(value = "pushFactoryData")
    public String pushFactoryData(String companyName,String date){
        logger.info("上抛数据至工厂开始:"+LocalDateTime.now());
        DbContextHolder.get().setCompanyName(companyName);
        VivoPushDto vivoPushDto = vivoPushService.getPushFactoryDate(date);
        vivoPushService.pushFactoryData(vivoPushDto,date);
        logger.info("上抛数据至工厂完成:"+LocalDateTime.now());
        return "数据上抛成功";
    }

    //渠道库存明细数据上抛
    @RequestMapping(value = "pushStoreDataToFactory")
    public String pushStoreDataToFactory(String companyName){
        logger.info("开始上抛渠道库存明细数据");
        DbContextHolder.get().setCompanyName(companyName);
        try{
            int count = 0;
            while(true){
                List<SProductItem000> sProductItem000List = vivoPushService.getStoreData();
                if (sProductItem000List.size() == 0){
                    break;
                }
                vivoPushService.pushStoreDataToFactory(count,sProductItem000List);
                vivoPushService.updateStoreData(sProductItem000List);
                count += sProductItem000List.size();
            }
        }catch (Exception e){
            e.printStackTrace();
            return "数据上抛失败";
        }
        logger.info("成功上抛渠道库存明细数据");
        return "数据上抛成功";
    }


}
