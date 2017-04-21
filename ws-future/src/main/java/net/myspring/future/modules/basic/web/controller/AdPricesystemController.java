package net.myspring.future.modules.basic.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.dto.AdPricesystemDto;
import net.myspring.future.modules.basic.service.AdPricesystemService;
import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.basic.web.Query.AdPricesystemQuery;
import net.myspring.future.modules.basic.web.form.AdPricesystemForm;
import net.myspring.util.json.ObjectMapperUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "basic/adPricesystem")
public class AdPricesystemController {

    @Autowired
    private AdPricesystemService adPricesystemService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Pageable pageable, AdPricesystemQuery adPricesystemQuery){
        Page<AdPricesystemDto> page = adPricesystemService.findPage(pageable,adPricesystemQuery);
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "save")
    public String save(AdPricesystemForm adPricesystemForm){
        adPricesystemService.save(adPricesystemForm);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功", ResponseCodeEnum.saved.name()));

    }

    @RequestMapping(value = "delete")
    public String delete(AdPricesystemForm adPricesystemForm) {
        adPricesystemService.delete(adPricesystemForm);
        RestResponse restResponse=new RestResponse("删除成功",ResponseCodeEnum.removed.name());
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "findOne")
    public String findOne(AdPricesystemForm adPricesystemForm){
        adPricesystemForm=adPricesystemService.findForm(adPricesystemForm);
        return ObjectMapperUtils.writeValueAsString(adPricesystemForm);
    }
}
