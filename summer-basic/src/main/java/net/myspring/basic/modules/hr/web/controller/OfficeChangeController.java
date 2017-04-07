package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.modules.hr.service.OfficeChangeService;
import net.myspring.basic.modules.hr.web.form.OfficeChangeForm;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "hr/officeChange")
public class OfficeChangeController {

    @Autowired
    private OfficeChangeService officeChangeService;

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public Map<String,Object> detail(OfficeChangeForm officeChangeForm) {
        Map<String,Object> paramMap= Maps.newHashMap();
        paramMap.put("shopPrint", officeChangeForm);
        return paramMap;
    }

    @RequestMapping(value = "audit", method = RequestMethod.GET)
    public RestResponse audit(OfficeChangeForm officeChangeForm,boolean pass,String comment) {
        RestResponse restResponse=new RestResponse("广告印刷审核成功",null);
        return  restResponse;
    }

}
