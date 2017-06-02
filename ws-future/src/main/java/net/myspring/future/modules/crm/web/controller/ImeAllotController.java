package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.constant.CharConstant;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.modules.crm.domain.ImeAllot;
import net.myspring.future.modules.crm.dto.ExpressOrderDto;
import net.myspring.future.modules.crm.dto.ImeAllotDto;
import net.myspring.future.modules.crm.service.ImeAllotService;
import net.myspring.future.modules.crm.web.form.ImeAllotForm;
import net.myspring.future.modules.crm.web.query.ImeAllotQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "crm/imeAllot")
public class ImeAllotController {


    @Autowired
    private ImeAllotService imeAllotService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ImeAllotDto> list(Pageable pageable, ImeAllotQuery imeAllotQuery) {
        return imeAllotService.findPage(pageable, imeAllotQuery);
    }

    @RequestMapping(value = "detail")
    public String detail(ImeAllot imeAllot) {
        return null;
    }

    @RequestMapping(value = "checkForImeAllot")
    public String checkForImeAllot(String imeStr) {
        List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
        if(imeList.size() == 0){
            return null;
        }
        return imeAllotService.checkForImeAllot(imeList, true);
    }



    @RequestMapping(value="getQuery")
    public  ImeAllotQuery getQuery(ImeAllotQuery imeAllotQuery){
        imeAllotQuery.setStatusList(AuditStatusEnum.getList());
        return imeAllotQuery;
    }

    @RequestMapping(value = "save")
    public RestResponse save(ImeAllotForm imeAllotForm) {

        List<String> imeList = imeAllotForm.getImeList();
        if(CollectionUtil.isEmpty(imeList)){
            return new RestResponse("没有输入任何有效的IME", ResponseCodeEnum.invalid.name(), false);
        }

        String errMsg = imeAllotService.checkForImeAllot(imeList, true);
        if(StringUtils.isNotBlank(errMsg)){
            return new RestResponse(errMsg, ResponseCodeEnum.invalid.name(), false);
        }

        imeAllotService.save(imeAllotForm);

        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "audit")
    public RestResponse audit(@RequestParam(value = "ids[]") String[] ids, boolean pass){
        if(ids == null || ids.length < 1){
            return new RestResponse("没有选中任何记录", ResponseCodeEnum.invalid.name(), false);

        }
        imeAllotService.audit(ids, pass);
        return new RestResponse("审核成功", ResponseCodeEnum.audited.name());

    }

    @RequestMapping(value = "findDto")
    public ImeAllotDto findDto(String id) {
        if(StringUtils.isBlank(id)){
            return new ImeAllotDto();
        }

        return imeAllotService.findDto(id);
    }

    @RequestMapping(value="export")
    public String export(ImeAllotQuery imeAllotQuery) {

        return imeAllotService.export(imeAllotQuery);
    }

}
