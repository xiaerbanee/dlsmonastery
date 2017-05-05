package net.myspring.future.modules.layout.web.controller;

import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.modules.layout.dto.ShopAllotDto;
import net.myspring.future.modules.layout.service.ShopAllotService;
import net.myspring.future.modules.layout.web.query.ShopAllotQuery;
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

    @RequestMapping(method = RequestMethod.GET)
    public Page<ShopAllotDto> list(Pageable pageable, ShopAllotQuery shopAllotQuery){
        Page<ShopAllotDto> page = shopAllotService.findPage(pageable, shopAllotQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public String delete() {
        return null;
    }

    @RequestMapping(value = "save")
    public String save() {
        return null;
    }

    @RequestMapping(value = "audit")
    public String audit() {
        return null;
    }

    @RequestMapping(value = "findForm")
    public String findOne() {
        return null;
    }

    @RequestMapping(value="getProducts")
    public String getProducts() {
        return null;
    }

    @RequestMapping(value="getEditFormData")
    public String getEditFormData() {
        return null;
    }


    @RequestMapping(value="getQuery")
    public ShopAllotQuery getQuery(ShopAllotQuery shopAllotQuery) {
        shopAllotQuery.setStatusList(AuditStatusEnum.getList());
        return shopAllotQuery;
    }





}
