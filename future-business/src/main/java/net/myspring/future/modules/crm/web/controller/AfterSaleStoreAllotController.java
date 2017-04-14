package net.myspring.future.modules.crm.web.controller;

import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.common.utils.StringUtils;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.crm.domain.AfterSaleStoreAllot;
import net.myspring.future.modules.crm.service.AfterSaleStoreAllotService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "crm/afterSaleStoreAllot")
public class AfterSaleStoreAllotController {

    @Autowired
    private AfterSaleStoreAllotService afterSaleStoreAllotService;

    @ModelAttribute
    public AfterSaleStoreAllot get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new AfterSaleStoreAllot() : afterSaleStoreAllotService.findOne(id);
    }


    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<AfterSaleStoreAllot> page = afterSaleStoreAllotService.findPage(searchEntity.getPageable(), searchEntity.getParams());
        return ObjectMapperUtils.writeValueAsString(page);
    }

}
