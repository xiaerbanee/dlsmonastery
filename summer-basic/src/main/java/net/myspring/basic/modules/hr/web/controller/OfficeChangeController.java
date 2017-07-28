package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.AccountChangeTypeEnum;
import net.myspring.basic.common.enums.OfficeChnageTypeEnum;
import net.myspring.basic.modules.hr.dto.AccountChangeDto;
import net.myspring.basic.modules.hr.dto.OfficeChangeDto;
import net.myspring.basic.modules.hr.dto.OfficeChangeFormDto;
import net.myspring.basic.modules.hr.service.OfficeChangeService;
import net.myspring.basic.modules.hr.web.form.OfficeChangeForm;
import net.myspring.basic.modules.hr.web.query.AccountChangeQuery;
import net.myspring.basic.modules.hr.web.query.OfficeChangeQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "hr/officeChange")
public class OfficeChangeController {

    @Autowired
    private OfficeChangeService officeChangeService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<OfficeChangeDto> list(Pageable pageable, OfficeChangeQuery officeChangeQuery){
        Page<OfficeChangeDto> page = officeChangeService.findPage(pageable,officeChangeQuery);
        return page;
    }

    @RequestMapping(value = "findByOfficeId")
    public List<OfficeChangeFormDto> findByOfficeId(String officeId){
        List<OfficeChangeFormDto> officeChangeDtoList=officeChangeService.findByOfficeId(officeId);
        return officeChangeDtoList;
    }

    @RequestMapping(value="getQuery")
    public OfficeChangeQuery getQuery(OfficeChangeQuery officeChangeQuery){
        officeChangeQuery.getExtra().put("typeList", OfficeChnageTypeEnum.getList());
        return officeChangeQuery;
    }

    @RequestMapping(value = "save",method = RequestMethod.POST)
    public RestResponse saveChange(String id,String json){
        if (StringUtils.isNotBlank(json)) {
            officeChangeService.save(id,json);
            return new RestResponse("保存成功",null,true);
        }
        return new RestResponse("数据不能为空",null,false);
    }

    @RequestMapping(value = "batchPass", method = RequestMethod.GET)
    public RestResponse batchPass(@RequestParam(value = "ids[]") String[] ids, boolean pass) {
        RestResponse restResponse=new RestResponse("审核成功", ResponseCodeEnum.audited.name());
        officeChangeService.batchPass(ids,pass);
        return restResponse;
    }

    @RequestMapping(value="audit",method=RequestMethod.GET)
    public RestResponse audit(String id,boolean pass,String commit){
        RestResponse restResponse=new RestResponse("审核成功",ResponseCodeEnum.audited.name());
        officeChangeService.audit(id,pass,commit);
        return restResponse;
    }



}
