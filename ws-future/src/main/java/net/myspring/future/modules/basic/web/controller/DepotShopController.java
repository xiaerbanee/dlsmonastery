package net.myspring.future.modules.basic.web.controller;

import com.google.common.collect.Lists;
import net.myspring.future.modules.basic.domain.DepotShop;
import net.myspring.future.modules.basic.dto.BankDto;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.dto.DepotShopDto;
import net.myspring.future.modules.basic.dto.ProductDto;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.service.DepotShopService;
import net.myspring.future.modules.basic.web.query.BankQuery;
import net.myspring.future.modules.basic.web.query.DepotShopQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @Autowired
    private DepotManager depotManager;

    @RequestMapping(method = RequestMethod.GET)
    public Page<DepotShopDto> list(Pageable pageable, DepotShopQuery depotShopQuery){
        Page<DepotShopDto> page = depotShopService.findPage(pageable,depotShopQuery);
        return page;
    }



    @RequestMapping(value = "search")
    public List<DepotDto>  directAndSubShop(DepotShopQuery depotShopQuery) {
        return depotShopService.findDepotDtoList(depotShopQuery);
    }

    //直营门店查询
    @RequestMapping(value = "direct")
    public List<DepotDto>  direct(DepotShopQuery depotShopQuery) {
        depotShopQuery.setDirect(true);
        return depotShopService.findDepotDtoList(depotShopQuery);
    }

    //代理门店查询
    @RequestMapping(value = "delegate")
    public List<DepotDto>  delegate(DepotShopQuery depotShopQuery) {
        depotShopQuery.setDelegate(true);
        return depotShopService.findDepotDtoList(depotShopQuery);
    }

    //直营门店+子店
    @RequestMapping(value = "directAndSub")
    public List<DepotDto> directAndSub(DepotShopQuery depotShopQuery) {
        depotShopQuery.setDirectAndSub(true);
        return depotShopService.findDepotDtoList(depotShopQuery);
    }

    @RequestMapping(value = "findByIds")
    public List<DepotDto> findByListIds(List<String> ids) {
        List<DepotDto> depotDtoList =depotShopService.findByIds(ids);
        return depotDtoList;
    }

}
