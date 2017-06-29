package net.myspring.future.modules.layout.web.controller;

import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.modules.crm.dto.StoreAllotDto;
import net.myspring.future.modules.layout.dto.ShopAllotDetailDto;
import net.myspring.future.modules.layout.dto.ShopAllotDetailSimpleDto;
import net.myspring.future.modules.layout.dto.ShopAllotDto;
import net.myspring.future.modules.layout.service.ShopAllotDetailService;
import net.myspring.future.modules.layout.service.ShopAllotService;
import net.myspring.future.modules.layout.web.form.ShopAllotAuditForm;
import net.myspring.future.modules.layout.web.form.ShopAllotForm;
import net.myspring.future.modules.layout.web.query.ShopAllotQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private ShopAllotDetailService shopAllotDetailService;

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:shopAllot:view')")
    public Page<ShopAllotDto> list(Pageable pageable, ShopAllotQuery shopAllotQuery){
        return shopAllotService.findPage(pageable, shopAllotQuery);
    }

    @RequestMapping(value = "logicDelete")
    @PreAuthorize("hasPermission(null,'crm:shopAllot:delete')")
    public RestResponse logicDelete(String id) {
        shopAllotService.logicDelete(id);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "save")
    @PreAuthorize("hasPermission(null,'crm:shopAllot:delete')")
    public RestResponse save(ShopAllotForm shopAllotForm) {
        if(CollectionUtil.isEmpty(shopAllotForm.getShopAllotDetailList())){
            throw new ServiceException("请录入门店调拨明细");
        }
        shopAllotService.save(shopAllotForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "audit")
    @PreAuthorize("hasPermission(null,'crm:shopAllot:audit')")
    public RestResponse audit(ShopAllotAuditForm shopAllotAuditForm) {
        shopAllotService.audit(shopAllotAuditForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findDetailListForEdit")
    @PreAuthorize("hasPermission(null,'crm:shopAllot:view')")
    public List<ShopAllotDetailSimpleDto> findDetailListForEdit(String shopAllotId) {
        return shopAllotDetailService.getShopAllotDetailListForEdit(shopAllotId);
    }

    @RequestMapping(value = "findDetailListForNew")
    @PreAuthorize("hasPermission(null,'crm:shopAllot:view')")
    public Map<String, Object> findDetailListForNew(String fromShopId, String toShopId) {
        Map<String, Object> result = new HashMap<>();

        if(fromShopId==null || toShopId==null){
            result.put("success", true);
            return result;
        }

        String errMsg = shopAllotService.checkShop(fromShopId, toShopId);
        if(!StringUtils.isBlank(errMsg)){
            result.put("success", false);
            result.put("errMsg", errMsg);
            return result;
        }

        result.put("success", true);
        result.put("shopAllotDetailList", shopAllotDetailService.getShopAllotDetailListForNew(fromShopId, toShopId));

        return result;

    }

    @RequestMapping(value="findTotalPrice")
    @PreAuthorize("hasPermission(null,'crm:shopAllot:view')")
    public Map<String, Object> findTotalPrice(String id) {
        return  shopAllotService.findTotalPrice(id);
    }

    @RequestMapping(value="findDetailListForViewOrAudit")
    @PreAuthorize("hasPermission(null,'crm:shopAllot:view')")
    public List<ShopAllotDetailDto> findDetailListForViewOrAudit(String id) {
        return  shopAllotService.findDetailListForViewOrAudit(id);
    }

    @RequestMapping(value="findDtoForViewOrAudit")
    @PreAuthorize("hasPermission(null,'crm:shopAllot:view')")
    public ShopAllotDto findDtoForViewOrAudit(String id) {
        if(StringUtils.isBlank(id)){
            return new ShopAllotDto();
        }
        return shopAllotService.findDtoForViewOrAudit(id);
    }

    @RequestMapping(value="findDto")
    @PreAuthorize("hasPermission(null,'crm:shopAllot:view')")
    public ShopAllotDto findDto(String id) {
        if(StringUtils.isBlank(id)){
            return new ShopAllotDto();
        }
        return shopAllotService.findDto(id);
    }

    @RequestMapping(value="getQuery")
    @PreAuthorize("hasPermission(null,'crm:shopAllot:view')")
    public ShopAllotQuery getQuery(ShopAllotQuery shopAllotQuery) {
        shopAllotQuery.getExtra().put("statusList",AuditStatusEnum.getList());
        return shopAllotQuery;
    }

    @RequestMapping(value="getForm")
    @PreAuthorize("hasPermission(null,'crm:shopAllot:view')")
    public ShopAllotForm getForm(ShopAllotForm shopAllotForm) {
        return shopAllotForm;
    }

}
