package net.myspring.future.modules.crm.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.DepotChangeEnum;
import net.myspring.future.modules.crm.dto.DepotChangeDto;
import net.myspring.future.modules.crm.service.DepotChangeService;
import net.myspring.future.modules.crm.web.form.DepotChangeForm;
import net.myspring.future.modules.crm.web.query.DepotChangeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping(value = "crm/depotChange")
public class DepotChangeController {

    @Autowired
    private DepotChangeService depotChangeService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<DepotChangeDto> list(Pageable pageable, DepotChangeQuery depotChangeQuery){
        return depotChangeService.findPage(pageable,depotChangeQuery);
    }

    @RequestMapping(value = "save")
    public RestResponse save() {
        return null;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        depotChangeService.logicDelete(id);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "getForm")
    public DepotChangeForm getForm(DepotChangeForm depotChangeForm) {
        depotChangeForm.getExtra().put("types",DepotChangeEnum.getList());
        return depotChangeForm;
    }

    @RequestMapping(value = "getQuery")
    public DepotChangeQuery getQuery(DepotChangeQuery depotChangeQuery) {
        depotChangeQuery.getExtra().put("types", DepotChangeEnum.getList());
        return depotChangeQuery;
    }

    @RequestMapping(value = "findOne")
    public DepotChangeDto findOne(String id){
        return depotChangeService.findOne(id);
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail( ) {
        return null;
    }

    @RequestMapping(value = "audit")
    public String audit( ) {
        return null;
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export( ) {
        return null;
    }

}
