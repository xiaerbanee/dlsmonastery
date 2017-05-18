package net.myspring.future.modules.layout.web.controller;

import net.myspring.future.modules.layout.domain.AdPricesystemChange;
import net.myspring.future.modules.layout.dto.AdPricesystemChangeDto;
import net.myspring.future.modules.layout.service.AdPricesystemChangeService;
import net.myspring.future.modules.layout.web.form.AdPricesystemChangeForm;
import net.myspring.future.modules.layout.web.query.AdPricesystemChangeQuery;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "layout/adPricesystemChange")
public class AdPricesystemChangeController {

    @Autowired
    private AdPricesystemChangeService adPricesystemChangeService;

    @ModelAttribute
    public AdPricesystemChange get(@RequestParam(required = false) String id) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<AdPricesystemChangeDto> list(Pageable pageable, AdPricesystemChangeQuery adPricesystemChangeQuery){

        return adPricesystemChangeService.findPage(pageable,adPricesystemChangeQuery);
    }

    @RequestMapping(value="findFilter", method = RequestMethod.GET)
    public List<AdPricesystemChangeForm> findFilter(AdPricesystemChangeQuery adPricesystemChangeQuery){
        return adPricesystemChangeService.findFilter(adPricesystemChangeQuery);
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(String data){


        return null;
    }
    @RequestMapping(value = "getQuery")
    public AdPricesystemChangeQuery getQuery(AdPricesystemChangeQuery adPricesystemChangeQuery){
        return new AdPricesystemChangeQuery();
    }


}
