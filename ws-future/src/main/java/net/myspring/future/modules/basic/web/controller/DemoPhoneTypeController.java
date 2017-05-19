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
    @Autowired
    private ProductTypeService productTypeService;

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
    public DemoPhoneTypeForm findOne(DemoPhoneTypeForm demoPhoneTypeForm){
        demoPhoneTypeForm=demoPhoneTypeService.getForm(demoPhoneTypeForm);
//        demoPhoneTypeForm.setProductTypeList(productTypeService.findAll());
        return demoPhoneTypeForm;
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
    public List<ProductTypeDto> search(String name){
        List<ProductType> productTypeList = Lists.newArrayList();
        if(StringUtils.isNotBlank(name)){
            productTypeList = productTypeService.findByNameLike(name);
        }
        List<ProductTypeDto> productTypeDtoList = BeanUtil.map(productTypeList,ProductTypeDto.class);
        return productTypeDtoList;
    }

}
