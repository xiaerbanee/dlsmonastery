package net.myspring.future.modules.crm.web.controller;

import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.crm.domain.ShopAttribute;
import net.myspring.future.modules.crm.service.ShopAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "crm/shopAttribute")
public class ShopAttributeController {

    @Autowired
    private ShopAttributeService shopAttributeService;

    @ModelAttribute
    public ShopAttribute get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new ShopAttribute() : shopAttributeService.findOne(id);
    }
}
