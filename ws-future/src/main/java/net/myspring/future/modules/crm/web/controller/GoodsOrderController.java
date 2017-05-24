package net.myspring.future.modules.crm.web.controller;


import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.ExpressCompanyTypeEnum;
import net.myspring.future.common.enums.GoodsOrderStatusEnum;
import net.myspring.future.common.enums.NetTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.basic.service.ExpressCompanyService;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.dto.GoodsOrderDetailDto;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;
import net.myspring.future.modules.crm.dto.GoodsOrderImeDto;
import net.myspring.future.modules.crm.service.GoodsOrderImeService;
import net.myspring.future.modules.crm.service.GoodsOrderService;
import net.myspring.future.modules.crm.web.form.GoodsOrderBillForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderShipForm;
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "crm/goodsOrder")
public class GoodsOrderController {

    @Autowired
    private GoodsOrderService goodsOrderService;

    @Autowired
    private GoodsOrderImeService goodsOrderImeService;

    @Autowired
    private ExpressCompanyService expressCompanyService;
    @Autowired
    private DepotService depotService;

    @Autowired
    private RedisTemplate redisTemplate;


    @RequestMapping(method = RequestMethod.GET)
    public Page<GoodsOrderDto> list(Pageable pageable, GoodsOrderQuery goodsOrderQuery){
        Page<GoodsOrderDto> page = goodsOrderService.findPage(pageable, goodsOrderQuery);
        return page;
    }


    @RequestMapping(value = "getQuery")
    public GoodsOrderQuery getQuery(GoodsOrderQuery goodsOrderQuery) {
        goodsOrderQuery.setNetTypeList(NetTypeEnum.getList());
        goodsOrderQuery.setShipTypeList(ShipTypeEnum.getList());
        goodsOrderQuery.setStatusList(GoodsOrderStatusEnum.getList());
        return goodsOrderQuery;
    }


    @RequestMapping(value = "findOne")
    public GoodsOrderDto findOne(String id) {
        return goodsOrderService.findOne(id);
    }
}
