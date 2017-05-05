package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.enums.StoreAllotStatusEnum;
import net.myspring.future.common.enums.StoreAllotTypeEnum;
import net.myspring.future.modules.basic.service.ExpressCompanyService;
import net.myspring.future.modules.crm.dto.StoreAllotDto;
import net.myspring.future.modules.crm.service.StoreAllotDetailService;
import net.myspring.future.modules.crm.service.StoreAllotService;
import net.myspring.future.modules.crm.web.form.StoreAllotForm;
import net.myspring.future.modules.crm.web.query.StoreAllotQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "crm/storeAllot")
public class StoreAllotController {

    @Autowired
    private StoreAllotService storeAllotService;

    @Autowired
    private ExpressCompanyService expressCompanyService;

    @Autowired
    private StoreAllotDetailService storeAllotDetailService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<StoreAllotDto> list(Pageable pageable, StoreAllotQuery storeAllotQuery){
        Page<StoreAllotDto> page = storeAllotService.findPage(pageable, storeAllotQuery);
        return page;
    }

    @RequestMapping(value = "save")
    public RestResponse save() {
        return null;
    }

    @RequestMapping(value = "findForm")
    public StoreAllotForm findForm(StoreAllotForm storeAllotForm) {
        StoreAllotForm result = storeAllotService.findFormWithoutStoreAllotDetails(storeAllotForm);
        result.setAllotTypeList(StoreAllotTypeEnum.getList());
        result.setShipTypeList(ShipTypeEnum.getList());
        result.setStoreAllotDetailFormList(storeAllotDetailService.genStoreAllotDetailListForEdit(storeAllotForm));
        result.setShowAllotType(Boolean.FALSE);
        return result;
    }

    @RequestMapping(value = "getStoreAllotData")
    public String getStoreAllotData() {
        return null;
    }

    @RequestMapping(value = "getCloudQty")
    public String getCloudQty() {
        return null;
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty() {
        return null;
    }

    @RequestMapping(value="getQuery")
    public StoreAllotQuery getQuery(StoreAllotQuery storeAllotQuery) {
        storeAllotQuery.setStatusList(StoreAllotStatusEnum.getList());
        return storeAllotQuery;
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export() {
        return null;
    }

    @RequestMapping(value = "ship", method=RequestMethod.GET)
    public String shipForm() {
        return null;
    }

    @RequestMapping(value = "shipBoxAndIme", method = RequestMethod.GET)
    public String shipBoxAndIme() {
        return null;
    }

    @RequestMapping(value = "ship", method=RequestMethod.POST)
    public RestResponse ship() {
        return null;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete() {
        return null;
    }


}
