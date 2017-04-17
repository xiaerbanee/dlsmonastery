package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.modules.sys.domain.DictMap;
import net.myspring.basic.modules.sys.dto.DictMapDto;
import net.myspring.basic.modules.sys.web.form.DictMapForm;
import net.myspring.basic.modules.sys.web.query.DictMapQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import net.myspring.basic.modules.sys.service.DictMapService;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "sys/dictMap")
public class DictMapController {

    @Autowired
    private DictMapService dictMapService;

    @RequestMapping(value = "getQuery")
    public DictMapQuery getQuery(DictMapQuery dictMapQuery) {
        dictMapQuery.setCategoryList(dictMapService.findDistinctCategory());
        return dictMapQuery;
    }

    @RequestMapping
    public Page<DictMapDto> list(Pageable pageable, DictMapQuery dictMapQuery) {
        Page<DictMapDto> page = dictMapService.findPage(pageable, dictMapQuery);
        return page;
    }

    @RequestMapping(value = "findOne")
    public DictMapForm findOne(DictMapForm dictMapForm) {
        dictMapForm=dictMapService.findForm(dictMapForm);
        dictMapForm.setCategoryList(dictMapService.findDistinctCategory());
        return dictMapForm;
    }

    @RequestMapping(value = "save")
    public RestResponse save(DictMapForm dictMapForm) {
        dictMapService.save(dictMapForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        dictMapService.logicDeleteOne(id);
        RestResponse restResponse = new RestResponse("刪除成功",ResponseCodeEnum.removed.name());
        return restResponse;
    }


}
