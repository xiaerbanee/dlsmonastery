package net.myspring.future.modules.layout.web.controller;


import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.layout.dto.ShopPrintDto;
import net.myspring.future.modules.layout.service.ShopPrintService;
import net.myspring.future.modules.layout.web.form.ShopPrintForm;
import net.myspring.future.modules.layout.web.query.ShopPrintQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "layout/shopPrint")
public class ShopPrintController {

    @Autowired
    private ShopPrintService shopPrintService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<ShopPrintDto> list(Pageable pageable, ShopPrintQuery shopPrintQuery) {
        return shopPrintService.findPage(pageable,shopPrintQuery);
    }


    @RequestMapping(value = "save", method = RequestMethod.POST)
    public RestResponse save(ShopPrintForm shopPrintForm) {
        shopPrintService.save(shopPrintForm);
            return null;
    }

    @RequestMapping(value="getQuery",method = RequestMethod.GET)
    public String getQuery() {
        return null;
    }

    @RequestMapping(value="getFormProperty",method = RequestMethod.GET)
    public String getFormProperty() {
        return null;
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public ShopPrintForm detail(ShopPrintForm shopPrintForm) {
        return shopPrintService.findForm(shopPrintForm);
    }

    @RequestMapping(value = "audit", method = RequestMethod.GET)
    public String audit() {
        return null;
    }

    private List<String> getActionList() {
        return null;
    }
}
