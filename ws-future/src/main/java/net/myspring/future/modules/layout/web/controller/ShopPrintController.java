package net.myspring.future.modules.layout.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
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
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value="getQuery",method = RequestMethod.GET)
    public ShopPrintQuery getQuery(ShopPrintQuery shopPrintQuery) {
        return shopPrintService.findQuery(shopPrintQuery);
    }

    @RequestMapping(value="getFormProperty",method = RequestMethod.GET)
    public String getFormProperty() {
        return null;
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public ShopPrintForm detail(ShopPrintForm shopPrintForm) {
        return shopPrintService.findForm(shopPrintForm);
    }

    @RequestMapping(value = "delete")
    public RestResponse logicDelete(ShopPrintForm shopPrintForm){
        shopPrintService.logicDelete(shopPrintForm.getId());
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "audit", method = RequestMethod.GET)
    public String audit() {
        return null;
    }

    private List<String> getActionList() {
        return null;
    }
}
