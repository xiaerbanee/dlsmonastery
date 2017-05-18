package net.myspring.future.modules.layout.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.modules.basic.service.ProductService;
import net.myspring.future.modules.layout.dto.ShopAllotDto;
import net.myspring.future.modules.layout.service.ShopAllotDetailService;
import net.myspring.future.modules.layout.service.ShopAllotService;
import net.myspring.future.modules.layout.web.form.ShopAllotForm;
import net.myspring.future.modules.layout.web.form.ShopAllotViewOrAuditForm;
import net.myspring.future.modules.layout.web.query.ShopAllotQuery;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "crm/shopAllot")
public class ShopAllotController {

    @Autowired
    private ShopAllotService shopAllotService;

    @Autowired
    private ProductService productService;
    @Autowired
    private ShopAllotDetailService shopAllotDetailService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ShopAllotDto> list(Pageable pageable, ShopAllotQuery shopAllotQuery){
        Page<ShopAllotDto> page = shopAllotService.findPage(pageable, shopAllotQuery);
        return page;
    }

    @RequestMapping(value = "logicDelete")
    public RestResponse logicDelete(String id) {
        shopAllotService.logicDeleteOne(id);
        RestResponse restResponse=new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;

    }

    @RequestMapping(value = "save")
    public RestResponse save(ShopAllotForm shopAllotForm) {
        shopAllotService.saveOrUpdate(shopAllotForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "audit")
    public RestResponse audit(ShopAllotViewOrAuditForm shopAllotViewOrAuditForm) {

        shopAllotService.audit(shopAllotViewOrAuditForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getShopAllotDetailFormListForNew")
    public ShopAllotForm getShopAllotDetailFormListForNew(String fromShopId, String toShopId) {
        if(fromShopId==null || toShopId==null){
            ShopAllotForm resultForm = new ShopAllotForm();
            resultForm.setSuccess(Boolean.TRUE);
            return resultForm;
        }

        String message = shopAllotService.checkShop(fromShopId, toShopId);

        if(!StringUtils.isBlank(message)){
            ShopAllotForm resultForm = new ShopAllotForm();
            resultForm.setMessage(message);
            resultForm.setSuccess(Boolean.FALSE);
            return resultForm;
        }

        ShopAllotForm resultForm = new ShopAllotForm();
        resultForm.setShopAllotDetailFormList(shopAllotDetailService.getShopAllotDetailListForNewOrEdit(null, fromShopId, toShopId));
        resultForm.setSuccess(Boolean.TRUE);

        return resultForm;

    }

    @RequestMapping(value="findForm")
    public ShopAllotForm findForm(ShopAllotForm shopAllotForm ) {
        ShopAllotForm result = shopAllotService.findForm(shopAllotForm);

        return result;
    }

    @RequestMapping(value="getViewOrAuditForm")
    public ShopAllotViewOrAuditForm getViewOrAuditForm(String id, String action) {

        return  shopAllotService.getViewOrAuditForm(id, action);
    }

    @RequestMapping(value="getQuery")
    public ShopAllotQuery getQuery(ShopAllotQuery shopAllotQuery) {
        shopAllotQuery.setStatusList(AuditStatusEnum.getList());
        return shopAllotQuery;
    }





}
