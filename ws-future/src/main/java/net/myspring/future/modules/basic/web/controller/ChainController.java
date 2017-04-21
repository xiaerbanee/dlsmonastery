package net.myspring.future.modules.basic.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.domain.Chain;
import net.myspring.future.modules.basic.dto.ChainDto;
import net.myspring.future.modules.basic.service.ChainService;
import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.basic.web.Query.ChainQuery;
import net.myspring.future.modules.basic.web.form.ChainForm;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "basic/chain")
public class ChainController {

    @Autowired
    private ChainService chainService;
    @Autowired
    private DepotService depotService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<ChainDto> list(Pageable pageable, ChainQuery chainQuery){
        Page<ChainDto> page = chainService.findPage(pageable,chainQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(Chain chain, BindingResult bindingResult) {
        chainService.delete(chain);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "save")
    public RestResponse save(ChainForm chainForm) {
        chainService.save(chainForm);
        return new RestResponse("保存成功",ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public ChainForm findOne(ChainForm chainForm){
        chainForm=chainService.findForm(chainForm);
        return chainForm;
    }
}
