package net.myspring.future.modules.layout.web.controller;

import net.myspring.future.common.enums.BillTypeEnum;
import net.myspring.future.common.enums.OfficeRuleEnum;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDetailDto;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDetailSimpleDto;
import net.myspring.future.modules.layout.service.AdGoodsOrderDetailService;
import net.myspring.future.modules.layout.web.query.AdGoodsOrderDetailQuery;
import net.myspring.future.modules.layout.web.query.AdGoodsOrderQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "layout/adGoodsOrderDetail")
public class AdGoodsOrderDetailController {

    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private AdGoodsOrderDetailService adGoodsOrderDetailService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AdGoodsOrderDetailDto> list(Pageable pageable, AdGoodsOrderDetailQuery adGoodsOrderDetailQuery) {
        return adGoodsOrderDetailService.findPage(pageable, adGoodsOrderDetailQuery);
    }

    @RequestMapping(value = "getQuery")
    public AdGoodsOrderDetailQuery getQuery(AdGoodsOrderDetailQuery adGoodsOrderDetailQuery) {
        adGoodsOrderDetailQuery.getExtra().put("adGoodsOrderBillTypeList", BillTypeEnum.getList());
        adGoodsOrderDetailQuery.getExtra().put("adGoodsOrderShopAreaList", officeClient.findByOfficeRuleName(OfficeRuleEnum.办事处.name()));
        return adGoodsOrderDetailQuery;
    }


    @RequestMapping(value="export")
    public String export(AdGoodsOrderDetailQuery adGoodsOrderDetailQuery) {
        return adGoodsOrderDetailService.export(adGoodsOrderDetailQuery);
    }



}
