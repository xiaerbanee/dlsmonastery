package net.myspring.basic.modules.sys.web.controller;

import net.myspring.basic.modules.sys.dto.DictEnumDto;
import net.myspring.basic.modules.sys.service.DictEnumService;
import net.myspring.basic.modules.sys.web.form.DictEnumForm;
import net.myspring.basic.modules.sys.web.query.DictEnumQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "sys/dictEnum")
public class DictEnumController {

    @Autowired
    private DictEnumService dictEnumService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<DictEnumDto>  list(Pageable pageable, DictEnumQuery dictEnumQuery){
        Page<DictEnumDto> page = dictEnumService.findPage(pageable,dictEnumQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        dictEnumService.logicDeleteOne(id);
        RestResponse restResponse =new RestResponse("删除成功",ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(DictEnumForm dictEnumForm) {
        dictEnumService.save(dictEnumForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findForm")
    public DictEnumForm findForm(DictEnumForm dictEnumForm){
        dictEnumForm=dictEnumService.findForm(dictEnumForm);
        dictEnumForm.setCategoryList(dictEnumService.findDistinctCategory());
        return dictEnumForm;
    }

    @RequestMapping(value="getQuery")
    public  DictEnumQuery getQuery(DictEnumQuery dictEnumQuery){
        dictEnumQuery.setCategoryList(dictEnumService.findDistinctCategory());
        return dictEnumQuery;
    }
}
