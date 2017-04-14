package net.myspring.future.modules.crm.web.controller;

import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.Collections3;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.service.OfficeUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.crm.domain.ImeSaleReport;
import net.myspring.future.modules.crm.model.InventoryMainModel;
import net.myspring.future.modules.crm.service.ImeSaleReportService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/imeSaleReport")
public class ImeSaleReportController {

    @Autowired
    private ImeSaleReportService imeSaleReportService;

    @ModelAttribute
    public ImeSaleReport get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new ImeSaleReport() : imeSaleReportService.findOne(id);
    }

    @RequestMapping(value = "officeInventory")
    public String findOfficeInventory(HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Map<String,Object> paramMap = searchEntity.getParams();
        paramMap.putAll(FilterUtils.getOfficeFilter(AccountUtils.getAccountId()));
        List<String> parentOfficeIds= OfficeUtils.getFirstLevelOfficeIds((List<String>) paramMap.get("officeIds"));
        if(paramMap.get("parentOfficeId")==null&&Collections3.isNotEmpty(parentOfficeIds)&&parentOfficeIds.size()==1){
            paramMap.put("parentOfficeId",parentOfficeIds.get(0));
        }
        InventoryMainModel inventoryMainModel = imeSaleReportService.findOfficeInventoryMainModel(paramMap);
        String parentOfficeId = StringUtils.toString(paramMap.get("parentOfficeId"));
        String nextType=OfficeUtils.getNextOfficeType(parentOfficeId);
        if(nextType.equals(OfficeUtils.getLastOfficeType((List<String>) paramMap.get("officeIds")))){
            inventoryMainModel.setHasNext(false);
        } else {
            inventoryMainModel.setHasNext(true);
        }
        return ObjectMapperUtils.writeValueAsString(inventoryMainModel);
    }

    @RequestMapping(value = "productInventory")
    public String findProductInventory(HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Map<String,Object> paramMap = searchEntity.getParams();
        paramMap.putAll(FilterUtils.getOfficeFilter(AccountUtils.getAccountId()));
        InventoryMainModel inventoryMainModel = imeSaleReportService.findProductInventoryMainModel(paramMap);
        return ObjectMapperUtils.writeValueAsString(inventoryMainModel);
    }

}
