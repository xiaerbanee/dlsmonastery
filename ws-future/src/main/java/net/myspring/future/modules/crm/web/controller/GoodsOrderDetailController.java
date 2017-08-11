package net.myspring.future.modules.crm.web.controller;

import net.myspring.common.constant.CharConstant;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.dto.GoodsOrderDetailDto;
import net.myspring.future.modules.crm.service.GoodsOrderDetailService;
import net.myspring.future.modules.crm.web.form.GoodsOrderDetailForm;
import net.myspring.future.modules.crm.web.query.GoodsOrderDetailQuery;
import net.myspring.util.time.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(value = "crm/goodsOrderDetail")
public class GoodsOrderDetailController {

    @Autowired
    private GoodsOrderDetailService goodsOrderDetailService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<GoodsOrderDetailDto> list(Pageable pageable, GoodsOrderDetailQuery goodsOrderDetailQuery){
        Page<GoodsOrderDetailDto> page = goodsOrderDetailService.findPage(pageable,goodsOrderDetailQuery);
        return page;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/getQuery")
    public GoodsOrderDetailQuery getQuery(GoodsOrderDetailQuery goodsOrderDetailQuery){
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = LocalDateTimeUtils.getFirstDayOfMonth(today.atStartOfDay()).toLocalDate();
        goodsOrderDetailQuery.setCreatedDate(firstDayOfMonth.toString()+ CharConstant.DATE_RANGE_SPLITTER+today.toString());
        return goodsOrderDetailQuery;
    }
}
