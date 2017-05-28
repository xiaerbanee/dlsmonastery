package net.myspring.future.modules.basic.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.dto.AdPricesystemDto;
import net.myspring.future.modules.basic.service.AdPricesystemService;
import net.myspring.future.modules.basic.web.form.AdPricesystemForm;
import net.myspring.future.modules.basic.web.query.AdPricesystemQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "basic/adPricesystem")
public class AdPricesystemController {

    @Autowired
    private AdPricesystemService adPricesystemService;

    @RequestMapping(method = RequestMethod.GET)
    public Page list(Pageable pageable, AdPricesystemQuery adPricesystemQuery){
        Page<AdPricesystemDto> page = adPricesystemService.findPage(pageable,adPricesystemQuery);
        return page;
    }

    @RequestMapping(value = "save")
    public RestResponse save(AdPricesystemForm adPricesystemForm){
        adPricesystemService.save(adPricesystemForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        adPricesystemService.logicDelete(id);
        RestResponse restResponse=new RestResponse("删除成功",ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "findOne")
    public AdPricesystemDto findOne(String id){
        return adPricesystemService.findOne(id);
    }
}
