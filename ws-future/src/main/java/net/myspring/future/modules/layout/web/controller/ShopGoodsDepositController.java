package net.myspring.future.modules.layout.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.OutBillTypeEnum;
import net.myspring.future.common.enums.ShopGoodsDepositStatusEnum;
import net.myspring.future.modules.layout.dto.ShopGoodsDepositDto;
import net.myspring.future.modules.layout.service.ShopGoodsDepositService;
import net.myspring.future.modules.layout.web.form.ShopGoodsDepositForm;
import net.myspring.future.modules.layout.web.query.ShopGoodsDepositQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "crm/shopGoodsDeposit")
public class ShopGoodsDepositController {

    @Autowired
    private ShopGoodsDepositService shopGoodsDepositService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<ShopGoodsDepositDto> list(Pageable pageable, ShopGoodsDepositQuery shopGoodsDepositQuery){
        Page<ShopGoodsDepositDto> page = shopGoodsDepositService.findPage(pageable, shopGoodsDepositQuery);
        return page;
    }


    @RequestMapping(value="getQuery")
    public ShopGoodsDepositQuery getQuery(ShopGoodsDepositQuery shopGoodsDepositQuery) {
        shopGoodsDepositQuery.setOutBillTypeList(OutBillTypeEnum.getList());
       shopGoodsDepositQuery.setStatusList(ShopGoodsDepositStatusEnum.getList());

        return shopGoodsDepositQuery;
    }

    @RequestMapping(value = "searchDepartment")
    public String searchDepartment() {
        return null;
    }


    @RequestMapping(value = "save")
    public RestResponse save(ShopGoodsDepositForm shopGoodsDepositForm) {
        shopGoodsDepositService.save(shopGoodsDepositForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }





    @RequestMapping(value = "batchAudit")
    public RestResponse batchAudit(String[] ids) {
        shopGoodsDepositService.batchAudit(ids);
//        for(Long id:ids){
//            k3cloudSynService.syn(id, K3CloudSynEntity.ExtendType.定金收款.name());
//        }
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());

    }

    @RequestMapping(value = "delete")
    public RestResponse delete(ShopGoodsDepositForm shopGoodsDepositForm) {
        shopGoodsDepositService.delete(shopGoodsDepositForm);
        RestResponse restResponse=new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;

    }

    @RequestMapping(value = "getForm")
    public ShopGoodsDepositForm getForm(ShopGoodsDepositForm shopGoodsDepositForm) {

        ShopGoodsDepositForm result = shopGoodsDepositService.getForm(shopGoodsDepositForm);
        return result;
    }

}
