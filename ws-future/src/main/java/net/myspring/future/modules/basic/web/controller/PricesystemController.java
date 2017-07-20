package net.myspring.future.modules.basic.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.domain.Pricesystem;
import net.myspring.future.modules.basic.dto.PricesystemDto;
import net.myspring.future.modules.basic.service.PricesystemService;
import net.myspring.future.modules.basic.web.query.PricesystemQuery;
import net.myspring.future.modules.basic.web.form.PricesystemForm;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "basic/pricesystem")
public class PricesystemController {

    @Autowired
    private PricesystemService pricesystemService;

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:pricesystem:view')")
    public Page<PricesystemDto> list(Pageable pageable, PricesystemQuery pricesystemQuery) {
        Page<PricesystemDto> page = pricesystemService.findPage(pageable, pricesystemQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    @PreAuthorize("hasPermission(null,'crm:pricesystem:delete')")
    public RestResponse delete(PricesystemForm pricesystemForm) {
        pricesystemService.logicDelete(pricesystemForm);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "save")
    @PreAuthorize("hasPermission(null,'crm:pricesystem:edit')")
    public RestResponse save(PricesystemForm pricesystemForm) {
        pricesystemService.save(pricesystemForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getForm")
    @PreAuthorize("hasPermission(null,'crm:pricesystem:view')")
    public PricesystemForm getForm(PricesystemForm pricesystemForm) {
        pricesystemForm=pricesystemService.getForm(pricesystemForm);
        return pricesystemForm;
    }

    @RequestMapping(value = "findOne")
    @PreAuthorize("hasPermission(null,'crm:pricesystem:view')")
    public PricesystemDto findOne(String id){
        return pricesystemService.findOne(id);
    }

    @RequestMapping(value = "getQuery")
    @PreAuthorize("hasPermission(null,'crm:pricesystem:view')")
    public PricesystemQuery getQuery(PricesystemQuery pricesystemQuery){
        return pricesystemQuery;
    }

    @RequestMapping(value = "filter")
    @PreAuthorize("hasPermission(null,'crm:pricesystem:view')")
    public List<PricesystemDto> findFilter(){
        return pricesystemService.findAllEnabled();
    }
}
