package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.dto.CompanyConfigDto;
import net.myspring.basic.modules.sys.dto.DictMapDto;
import net.myspring.basic.modules.sys.service.CompanyConfigService;
import net.myspring.basic.modules.sys.web.form.CompanyConfigForm;
import net.myspring.basic.modules.sys.web.query.CompanyConfigQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zhucc on 2017/4/17.
 */
@RestController
@RequestMapping("sys/companyConfig")
public class CompanyConfigController {


    @Autowired
    private CompanyConfigService companyConfigService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<CompanyConfigDto> list(Pageable pageable, CompanyConfigQuery companyConfigQuery){
        Page<CompanyConfigDto> page=companyConfigService.findPage(pageable,companyConfigQuery);
        return page;
    }

    @RequestMapping(value = "save")
    public RestResponse save(CompanyConfigForm companyConfigForm){
        companyConfigService.save(companyConfigForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getForm")
    public CompanyConfigDto getForm(String id) {
        CompanyConfigDto companyConfigDto=companyConfigService.getForm(id);
        return companyConfigDto;
    }

    @RequestMapping(value = "getValueByCode")
    public String getValueByCode(String code) {
        if(StringUtils.isNotBlank(code)){
            String value=companyConfigService.getValueByCode(code);
            return value;
        }
        return "";
    }
}
