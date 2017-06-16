package net.myspring.future.modules.layout.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.OutBillTypeEnum;
import net.myspring.future.common.enums.ShopDepositTypeEnum;
import net.myspring.future.modules.layout.dto.ShopAllotDto;
import net.myspring.future.modules.layout.dto.ShopDepositDto;
import net.myspring.future.modules.layout.service.ShopDepositService;
import net.myspring.future.modules.layout.web.form.ShopDepositForm;
import net.myspring.future.modules.layout.web.query.ShopDepositQuery;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(value = "crm/shopDeposit")
public class ShopDepositController {

    @Autowired
    private ShopDepositService shopDepositService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ShopDepositDto> list(Pageable pageable, ShopDepositQuery shopDepositQuery){
        return shopDepositService.findPage(pageable, shopDepositQuery);
    }

    @RequestMapping(value = "getQuery")
    public ShopDepositQuery getQuery(ShopDepositQuery shopDepositQuery) {
        shopDepositQuery.getExtra().put("typeList", ShopDepositTypeEnum.getList());
        return shopDepositQuery;
    }

    @RequestMapping(value = "getDefaultDepartMent")
    public String getDefaultDepartMent(String shopId) {
//TODO 需要查金蝶获取结果
        return "ceshi";
    }

    @RequestMapping(value = "getForm")
    public ShopDepositForm getForm(ShopDepositForm shopDepositForm) {
        shopDepositForm.getExtra().put("outBillTypeList",OutBillTypeEnum.getList());
        return shopDepositForm;

    }

    @RequestMapping(value = "findDto")
    public ShopDepositDto findDto(String id) {

        if(StringUtils.isBlank(id)){
            return new ShopDepositDto();
        }
        return shopDepositService.findDto(id);

    }

    @RequestMapping(value = "save")
    public RestResponse save(ShopDepositForm shopDepositForm) {
        shopDepositService.save(shopDepositForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findLeftAmount")
    public BigDecimal findLeftAmount(String type, String depotId) {
        return shopDepositService.findLeftAmount(type, depotId);

    }

}
