package net.myspring.future.modules.basic.web.controller;

import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by liuj on 2017/5/15.
 */
@RestController
@RequestMapping(value = "basic/depot")
public class DepotController {

    @Autowired
    private DepotService depotService;

    //直营门店查询(POP申请开单类型为配件赠品用这个)
    @RequestMapping(value = "directShop")
    public List<DepotDto>  directShop(DepotQuery depotQuery) {
        depotQuery.setClientIsNull(false);
        List<DepotDto> result = depotService.findShopList(depotQuery);
        return result;
    }

    //代理门店查询
    @RequestMapping(value = "delegateShop")
    public List<DepotDto>  delegateShop(DepotQuery depotQuery) {
        depotQuery.setClientIsNull(true);
        return depotService.findShopList(depotQuery);
    }

    //门店查询(物料订单的代理门店也用这个)
    @RequestMapping(value = "shop")
    public List<DepotDto>  shop(DepotQuery depotQuery) {
        return depotService.findShopList(depotQuery);
    }

    //物料订单门店
    @RequestMapping(value = "adShop")
    public List<DepotDto>  adShop(DepotQuery depotQuery) {
        depotQuery.setAdShop(true);
        return depotService.findShopList(depotQuery);
    }

    //POP门店
    @RequestMapping(value = "popShop")
    public List<DepotDto>  popShop(DepotQuery depotQuery) {
        depotQuery.setPopShop(true);
        return depotService.findShopList(depotQuery);
    }

    //直营仓库查询
    @RequestMapping(value = "directStore")
    public List<DepotDto>  directStore(DepotQuery depotQuery) {
        depotQuery.setOutIdIsNull(false);
        return depotService.findStoreList(depotQuery);
    }

    //代理仓库查询
    @RequestMapping(value = "delegateStore")
    public List<DepotDto>  delegateStore(DepotQuery depotQuery) {
        depotQuery.setOutIdIsNull(true);
        return depotService.findStoreList(depotQuery);
    }

    //仓库查询
    @RequestMapping(value = "store")
    public List<DepotDto>  store(DepotQuery depotQuery) {
        return depotService.findStoreList(depotQuery);
    }

    @RequestMapping(value = "findByIds")
    public List<DepotDto> findByListIds(@RequestParam("idStr") List<String> ids) {
        List<DepotDto> depotDtoList =depotService.findByIds(ids);
        return depotDtoList;
    }

    @RequestMapping(value = "findById")
    public DepotDto findById(String id) {
        if(StringUtils.isBlank(id)){
            return new DepotDto();
        }
        return depotService.findById(id);
    }

    @RequestMapping(value = "findShopByGoodsOrderId")
    public DepotDto findShopByGoodsOrderId(String goodsOrderId) {
        if(StringUtils.isBlank(goodsOrderId)){
            return new DepotDto();
        }
        return depotService.findShopByGoodsOrderId(goodsOrderId);
    }

    @RequestMapping(value = "findStoreByGoodsOrderId")
    public DepotDto findStoreByGoodsOrderId(String goodsOrderId) {
        if(StringUtils.isBlank(goodsOrderId)){
            return new DepotDto();
        }
        return depotService.findStoreByGoodsOrderId(goodsOrderId);
    }

}
