package net.myspring.future.modules.basic.web.controller;

import com.google.common.collect.Lists;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.dto.ProductDto;
import net.myspring.future.modules.basic.service.DepotShopService;
import net.myspring.future.modules.basic.web.query.DepotShopQuery;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by liuj on 2017/5/12.
 */
@RestController
@RequestMapping(value = "basic/depotShop")
public class DepotShopController {

    @Autowired
    private DepotShopService depotShopService;

    @RequestMapping(value = "search")
    public List<DepotDto>  directAndSubShop(DepotShopQuery depotShopQuery) {
        return depotShopService.findAll(depotShopQuery);
    }

    @RequestMapping(value = "searchShop")
    public List<DepotDto> searchShop(String name,String category){
        List<DepotDto> depotShopList = Lists.newArrayList();
        if(StringUtils.isNotBlank(name)){
            depotShopList = depotShopService.findByLikeName(name,category);
        }
        return depotShopList;
    }
    //直营门店查询
    @RequestMapping(value = "direct")
    public List<DepotDto>  direct(DepotShopQuery depotShopQuery) {
        depotShopQuery.setDirect(true);
        return depotShopService.findAll(depotShopQuery);
    }

    //代理门店查询
    @RequestMapping(value = "delegate")
    public List<DepotDto>  delegate(DepotShopQuery depotShopQuery) {
        depotShopQuery.setDelegate(true);
        return depotShopService.findAll(depotShopQuery);
    }

    //直营门店+子店
    @RequestMapping(value = "directAndSub")
    public List<DepotDto> directAndSub(DepotShopQuery depotShopQuery) {
        depotShopQuery.setDirectAndSub(true);
        return depotShopService.findAll(depotShopQuery);
    }

    //广告门店
    @RequestMapping(value = "adShop")
    public List<DepotDto> adShop(DepotShopQuery depotShopQuery) {
        depotShopQuery.setAdShop(true);
        return depotShopService.findAll(depotShopQuery);
    }

    //直营广告门店
    @RequestMapping(value = "directAdShop")
    public List<DepotDto> directAdShop(DepotShopQuery depotShopQuery) {
        depotShopQuery.setDirect(true);
        depotShopQuery.setAdShop(true);
        return depotShopService.findAll(depotShopQuery);
    }

}
