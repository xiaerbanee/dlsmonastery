package net.myspring.future.modules.layout.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.modules.basic.service.ProductService;
import net.myspring.future.modules.layout.dto.ShopAllotDetailDto;
import net.myspring.future.modules.layout.dto.ShopAllotDto;
import net.myspring.future.modules.layout.service.ShopAllotDetailService;
import net.myspring.future.modules.layout.service.ShopAllotService;
import net.myspring.future.modules.layout.web.form.ShopAllotDetailForm;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        shopAllotService.logicDelete(id);
        RestResponse restResponse=new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;

    }

    @RequestMapping(value = "save")
    public RestResponse save(ShopAllotForm shopAllotForm) {
        shopAllotService.save(shopAllotForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "audit")
    public RestResponse audit(ShopAllotViewOrAuditForm shopAllotViewOrAuditForm) {

        shopAllotService.audit(shopAllotViewOrAuditForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }



    @RequestMapping(value = "findDetailListForEdit")
    public List<ShopAllotDetailDto> findDetailListForEdit(String shopAllotId) {

        return shopAllotDetailService.getShopAllotDetailListForEdit(shopAllotId);

    }

    @RequestMapping(value = "findDetailListForNew")
    public Map<String, Object> findDetailListForNew(String fromShopId, String toShopId) {
        Map<String, Object> result = new HashMap<>();

        if(fromShopId==null || toShopId==null){
            result.put("success", true);
            return result;
        }

        String message = shopAllotService.checkShop(fromShopId, toShopId);
        if(!StringUtils.isBlank(message)){
            result.put("success", false);
            result.put("message", message);
            return result;
        }

        result.put("success", true);
        result.put("shopAllotDetailList", shopAllotDetailService.getShopAllotDetailListForNew(fromShopId, toShopId));

        return result;

    }

    @RequestMapping(value="findDetailListForViewOrAudit")
    public List<ShopAllotDetailDto> findDetailListForViewOrAudit(String id) {

        return  shopAllotService.findDetailListForViewOrAudit(id);
    }

    @RequestMapping(value="findDtoForViewOrAudit")
    public ShopAllotDto findDtoForViewOrAudit(String id) {
        if(StringUtils.isBlank(id)){
            return new ShopAllotDto();
        }

        return shopAllotService.findDtoForViewOrAudit(id);

    }

    @RequestMapping(value="findDto")
    public ShopAllotDto findDto(String id) {
        if(StringUtils.isBlank(id)){
            return new ShopAllotDto();
        }
        return shopAllotService.findDto(id);

    }




    @RequestMapping(value="getQuery")
    public ShopAllotQuery getQuery(ShopAllotQuery shopAllotQuery) {
        shopAllotQuery.setStatusList(AuditStatusEnum.getList());
        return shopAllotQuery;
    }





}
