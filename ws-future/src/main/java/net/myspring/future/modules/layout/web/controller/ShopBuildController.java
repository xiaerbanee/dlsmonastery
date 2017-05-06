package net.myspring.future.modules.layout.web.controller;

import net.myspring.future.modules.layout.dto.ShopBuildDto;
import net.myspring.future.modules.layout.service.ShopBuildService;
import net.myspring.future.modules.layout.web.form.ShopBuildForm;
import net.myspring.future.modules.layout.web.query.ShopBuildQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "layout/shopBuild")
public class ShopBuildController {

    @Autowired
    private ShopBuildService shopBuildService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<ShopBuildDto> list(Pageable pageable, ShopBuildQuery shopBuildQuery) {
        Page<ShopBuildDto> page = shopBuildService.findPage(pageable,shopBuildQuery);
        return page;
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty() {
        return null;
    }

    @RequestMapping(value = "getQuery")
    public String getQuery() {
        return null;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save() {
        return null;
    }

    @RequestMapping(value = "batchAudit", method = RequestMethod.GET)
    public String batchAudit() {
        return null;
    }

    @RequestMapping(value = "delete")
    public String delete() {
        return null;
    }
    @RequestMapping(value = "detail")
    public ShopBuildForm detail(ShopBuildForm shopBuildForm) {
        return shopBuildService.findForm(shopBuildForm);
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export() {
        return null;
    }

    private List<String> getActionList() {
        return null;
    }
}
