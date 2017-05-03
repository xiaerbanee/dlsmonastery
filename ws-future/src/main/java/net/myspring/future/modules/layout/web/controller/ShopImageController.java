package net.myspring.future.modules.layout.web.controller;


import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.layout.domain.ShopImage;
import net.myspring.future.modules.layout.dto.ShopImageDto;
import net.myspring.future.modules.layout.service.ShopImageService;
import net.myspring.future.modules.layout.web.query.ShopImageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "layout/shopImage")
public class ShopImageController {

    @Autowired
    private ShopImageService shopImageService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<ShopImageDto> list(Pageable pageable, ShopImageQuery shopImageQuery) {
        return shopImageService.findPage(pageable,shopImageQuery);
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail() {
        return null;
    }

    @RequestMapping(value = "getFormProperty", method = RequestMethod.GET)
    public String getFormProperty() {
        return null;
    }

    @RequestMapping(value = "getQuery", method = RequestMethod.GET)
    public String getQuery() {
        return null;
    }

    @RequestMapping(value = "save")
    @ResponseBody
    public String save() {
        return null;
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public String logicDelete() {
        return null;
    }

    public List<String> getActionList() {
        return null;
    }

}
