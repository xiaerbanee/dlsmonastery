package net.myspring.future.modules.crm.web.controller;


import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.crm.dto.ExpressOrderDto;
import net.myspring.future.modules.crm.service.ExpressOrderService;
import net.myspring.future.modules.crm.web.form.ExpressOrderForm;
import net.myspring.future.modules.crm.web.query.ExpressOrderQuery;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/expressOrder")
public class ExpressOrderController {

    @Autowired
    private ExpressOrderService expressOrderService;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:expressOrder:view')")
    public Page<ExpressOrderDto> list(Pageable pageable, ExpressOrderQuery expressOrderQuery){
        return expressOrderService.findPage(pageable, expressOrderQuery);
    }

    @RequestMapping(value = "getQuery")
    @PreAuthorize("hasPermission(null,'crm:expressOrder:view')")
    public ExpressOrderQuery getQuery(ExpressOrderQuery expressOrderQuery) {
        expressOrderQuery.getExtra().put("extendTypeList",ExpressOrderTypeEnum.getList());
        return expressOrderQuery;
    }

    @RequestMapping(value = "save")
    @PreAuthorize("hasPermission(null,'crm:expressOrder:edit')")
    public RestResponse save(ExpressOrderForm expressOrderForm){
        expressOrderService.save(expressOrderForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "resetPrintStatus")
    @PreAuthorize("hasPermission(null,'crm:expressOrder:edit')")
    public RestResponse resetPrintStatus(String id) {

        expressOrderService.resetPrintStatus(id);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());

    }

    @RequestMapping(value = "exportEMS", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:expressOrder:view')")
    public ModelAndView exportEMS(ExpressOrderQuery expressOrderQuery) throws IOException {
        return new ModelAndView(new ExcelView(), "simpleExcelBook", expressOrderService.exportEMS(expressOrderQuery));
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:expressOrder:view')")
    public ModelAndView export(ExpressOrderQuery expressOrderQuery) throws IOException {
        return new ModelAndView(new ExcelView(), "simpleExcelBook", expressOrderService.export(expressOrderQuery));
    }

    @RequestMapping(value = "findDto")
    @PreAuthorize("hasPermission(null,'crm:expressOrder:view')")
    public ExpressOrderDto findDto(String id) throws IOException {
        if(StringUtils.isBlank(id)){
            return new ExpressOrderDto();
        }

        return expressOrderService.findDto(id);

    }

    @RequestMapping(value = "findByGoodsOrderId")
    @PreAuthorize("hasPermission(null,'crm:expressOrder:view')")
    public ExpressOrderDto findByGoodsOrderId(String goodsOrderId) throws IOException {
        if(StringUtils.isBlank(goodsOrderId)){
            return new ExpressOrderDto();
        }
        ExpressOrderDto expressOrderDto = expressOrderService.findByGoodsOrderId(goodsOrderId);
        if(expressOrderDto == null){
            expressOrderDto = new ExpressOrderDto();
        }
        return expressOrderDto;
    }

    @RequestMapping(value = "getExpressConfig")
    public Map<String, Object> getExpressConfig() {
        Map<String, Object> result = new HashMap<>();
        result.put("expressProductId", CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.EXPRESS_PRODUCT_ID.name()).getValue());
        result.put("expressRuleList", ObjectMapperUtils.readValue(CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.EXPRESS_SHOULD_GET_RULE.name()).getValue(), List.class));
        return result;
    }


}
