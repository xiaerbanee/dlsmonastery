package net.myspring.future.modules.layout.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.layout.dto.ShopDepositDto;
import net.myspring.future.modules.layout.service.ShopDepositService;
import net.myspring.future.modules.layout.web.form.ShopDepositForm;
import net.myspring.future.modules.layout.web.query.ShopDepositQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "crm/shopDeposit")
public class ShopDepositController {

    @RequestMapping(method = RequestMethod.GET)
    public Page<ShopDepositDto> list(Pageable pageable, ShopDepositQuery shopDepositQuery){
        Page<ShopDepositDto> page = shopDepositService.findPage(pageable, shopDepositQuery);
        return page;
    }


    @RequestMapping(value = "getDepartmentByShopId")
    public String getDepartmentByShopId() {
        return null;
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty() {
        return null;
    }

    @RequestMapping(value = "getQuery")
    public ShopDepositQuery getQuery(ShopDepositQuery shopDepositQuery) {
        shopDepositQuery.setTypeList(shopDepositService.findShopDepositTypeList());
        return shopDepositQuery;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(ShopDepositForm shopDepositForm) {
        shopDepositService.delete(shopDepositForm);
        RestResponse restResponse=new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    /**
     * 押金列表只能够新增，不能修改和删除
     */
    @RequestMapping(value = "findForm")
    public ShopDepositForm findForm() {
        ShopDepositForm shopDepositForm = new ShopDepositForm();
        return shopDepositForm;

    }

    @RequestMapping(value = "save")
    public RestResponse save(@Valid ShopDepositForm shopDepositForm, BindingResult result) {

        shopDepositService.save(shopDepositForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @Autowired
    private ShopDepositService shopDepositService;

}
