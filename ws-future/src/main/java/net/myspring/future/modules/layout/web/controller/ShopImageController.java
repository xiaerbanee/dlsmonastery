package net.myspring.future.modules.layout.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.layout.dto.ShopImageDto;
import net.myspring.future.modules.layout.service.ShopImageService;
import net.myspring.future.modules.layout.web.form.ShopImageForm;
import net.myspring.future.modules.layout.web.query.ShopImageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "layout/shopImage")
public class ShopImageController {

    @Autowired
    private ShopImageService shopImageService;



    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:shopImage:view')")
    public Page<ShopImageDto> list(Pageable pageable, ShopImageQuery shopImageQuery) {
        return shopImageService.findPage(pageable,shopImageQuery);
    }

    @RequestMapping(value = "findOne")
    @PreAuthorize("hasPermission(null,'crm:shopImage:view')")
    public ShopImageDto detail(String id) {
        return shopImageService.findOne(id);
    }

    @RequestMapping(value = "getForm")
    @PreAuthorize("hasPermission(null,'crm:shopImage:view')")
    public ShopImageForm getForm(ShopImageForm shopImageForm){
        return shopImageService.getForm(shopImageForm);
    }

    @RequestMapping(value = "getQuery", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:shopImage:view')")
    public ShopImageQuery getQuery(ShopImageQuery shopImageQuery) {
        return shopImageService.getQuery(shopImageQuery);
    }

    @RequestMapping(value = "save")
    @PreAuthorize("hasPermission(null,'crm:shopImage:edit')")
    public RestResponse save(ShopImageForm shopImageForm) {
        shopImageService.save(shopImageForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete")
    @PreAuthorize("hasPermission(null,'crm:shopImage:delete')")
    public RestResponse delete(String id) {
        shopImageService.logicDelete(id);
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }


}
