package net.myspring.future.modules.layout.web.controller;


import net.myspring.future.modules.layout.dto.ShopImageDto;
import net.myspring.future.modules.layout.service.ShopImageService;
import net.myspring.future.modules.layout.web.query.ShopImageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "layout/shopImage")
public class ShopImageController {

    @Autowired
    private ShopImageService shopImageService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<ShopImageDto> list(Pageable pageable, ShopImageQuery shopImageQuery) {
        return shopImageService.findPage(pageable,shopImageQuery);
    }

    @RequestMapping(value = "findOne")
    public String findOne() {
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
