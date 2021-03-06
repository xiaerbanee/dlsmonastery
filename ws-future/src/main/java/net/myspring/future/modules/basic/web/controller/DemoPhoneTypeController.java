package net.myspring.future.modules.basic.web.controller;

import com.google.common.collect.Lists;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.dto.DemoPhoneTypeDto;
import net.myspring.future.modules.basic.dto.ProductTypeDto;
import net.myspring.future.modules.basic.service.DemoPhoneTypeService;
import net.myspring.future.modules.basic.service.ProductTypeService;
import net.myspring.future.modules.basic.web.query.DemoPhoneTypeQuery;
import net.myspring.future.modules.basic.web.form.DemoPhoneTypeDetailForm;
import net.myspring.future.modules.basic.web.form.DemoPhoneTypeForm;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "crm/demoPhoneType")
public class DemoPhoneTypeController {

    @Autowired
    private DemoPhoneTypeService demoPhoneTypeService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<DemoPhoneTypeDto> list(Pageable pageable, DemoPhoneTypeQuery demoPhoneTypeQuery) {
        Page<DemoPhoneTypeDto> page=demoPhoneTypeService.findPage(pageable, demoPhoneTypeQuery);
        return page;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public RestResponse save(DemoPhoneTypeForm demoPhoneTypeForm){
        demoPhoneTypeService.save(demoPhoneTypeForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getForm")
    public DemoPhoneTypeForm getForm(DemoPhoneTypeForm demoPhoneTypeForm){
        return demoPhoneTypeService.getForm(demoPhoneTypeForm);
    }

    @RequestMapping(value = "findOne")
    public DemoPhoneTypeDto findOne(String id){
        return demoPhoneTypeService.findOne(id);
    }

    @RequestMapping(value = "getQuery")
    public DemoPhoneTypeQuery getQuery(DemoPhoneTypeQuery demoPhoneTypeQuery){
        return demoPhoneTypeQuery;
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public DemoPhoneTypeDetailForm DemoPhoneTypeDetailForm(DemoPhoneTypeDetailForm demoPhoneTypeDetailForm){
        demoPhoneTypeDetailForm=demoPhoneTypeService.findDetailForm(demoPhoneTypeDetailForm);
        return demoPhoneTypeDetailForm;
    }


    @RequestMapping(value = "delete")
    public RestResponse delete(DemoPhoneTypeForm demoPhoneTypeForm){
        demoPhoneTypeService.delete(demoPhoneTypeForm);
        return new RestResponse("删除成功",ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public List<DemoPhoneTypeDto> search(String name){
        return demoPhoneTypeService.findNameLike(name);
    }

    @RequestMapping(value = "searchById")
    public List<DemoPhoneTypeDto> searchById(String id){
        List<DemoPhoneTypeDto> demoPhoneTypeDtos = Lists.newArrayList();
        demoPhoneTypeDtos.add(demoPhoneTypeService.findOne(id));
        return demoPhoneTypeDtos;
    }

}
