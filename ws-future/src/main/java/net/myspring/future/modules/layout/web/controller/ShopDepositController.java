package net.myspring.future.modules.layout.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.OutBillTypeEnum;
import net.myspring.future.common.enums.ShopDepositTypeEnum;
import net.myspring.future.modules.layout.dto.ShopDepositDto;
import net.myspring.future.modules.layout.service.ShopDepositService;
import net.myspring.future.modules.layout.web.form.ShopDepositForm;
import net.myspring.future.modules.layout.web.query.ShopDepositQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "crm/shopDeposit")
public class ShopDepositController {

    @Autowired
    private ShopDepositService shopDepositService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ShopDepositDto> list(Pageable pageable, ShopDepositQuery shopDepositQuery){
        Page<ShopDepositDto> page = shopDepositService.findPage(pageable, shopDepositQuery);
        return page;
    }


    @RequestMapping(value = "getQuery")
    public ShopDepositQuery getQuery(ShopDepositQuery shopDepositQuery) {
        shopDepositQuery.setTypeList( ShopDepositTypeEnum.getList());
        return shopDepositQuery;
    }

    @RequestMapping(value = "getDefaultDepartMent")
    public String getDefaultDepartMent(String shopId) {
//TODO 需要查金蝶获取结果
        return "ceshi";
    }


    @RequestMapping(value = "delete")
    public RestResponse delete(ShopDepositForm shopDepositForm) {
        shopDepositService.logicDelete(shopDepositForm);
        RestResponse restResponse=new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    /**
     * 押金列表只能够新增，不能修改和删除
     */
    @RequestMapping(value = "getForm")
    public ShopDepositForm getForm() {
        ShopDepositForm shopDepositForm = new ShopDepositForm();
        shopDepositForm.setOutBillTypeList(OutBillTypeEnum.getList());
        return shopDepositForm;

    }

    @RequestMapping(value = "save")
    public RestResponse save(ShopDepositForm shopDepositForm) {
        shopDepositService.save(shopDepositForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

}
