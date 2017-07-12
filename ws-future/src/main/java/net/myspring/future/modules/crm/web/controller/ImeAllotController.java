package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.constant.CharConstant;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.modules.crm.dto.ImeAllotDto;
import net.myspring.future.modules.crm.service.ImeAllotService;
import net.myspring.future.modules.crm.web.form.ImeAllotBatchForm;
import net.myspring.future.modules.crm.web.form.ImeAllotForm;
import net.myspring.future.modules.crm.web.form.ImeAllotSimpleForm;
import net.myspring.future.modules.crm.web.query.ImeAllotQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping(value = "checkForImeAllot")
    public String checkForImeAllot(String imeStr) {
        List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
        if(imeList.size() == 0){
            return null;
        }
        return imeAllotService.checkForImeAllot(imeList, true);
    }

    @RequestMapping(value = "getForm")
    public ImeAllotForm getForm(ImeAllotForm imeAllotForm) {
        return imeAllotForm;
    }
    @RequestMapping(value="getQuery")
    public  ImeAllotQuery getQuery(ImeAllotQuery imeAllotQuery){
        imeAllotQuery.getExtra().put("statusList",AuditStatusEnum.getList());
        imeAllotQuery.setCrossArea(true);
        return imeAllotQuery;
    }

    @RequestMapping(value = "save")
    public RestResponse save(ImeAllotForm imeAllotForm) {

        List<String> imeList = imeAllotForm.getImeList();
        if(CollectionUtil.isEmpty(imeList)){
            throw new ServiceException("没有输入任何有效的串码");
        }

        String errMsg = imeAllotService.checkForImeAllot(imeList, false);
        if(StringUtils.isNotBlank(errMsg)){
            throw new ServiceException(errMsg);
        }

        imeAllotService.allot(imeAllotForm);

        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getImeAllotBatchForm")
    public ImeAllotBatchForm getImeAllotBatchForm(ImeAllotBatchForm  imeAllotBatchForm){

        imeAllotBatchForm.getExtra().put("toDepotNameList" , imeAllotService.findToDepotNameList());
        return imeAllotBatchForm;

    }

    @RequestMapping(value = "batchAllot")
    public RestResponse batchAllot(ImeAllotBatchForm  imeAllotBatchForm){


        if(CollectionUtil.isEmpty(imeAllotBatchForm.getImeAllotSimpleFormList())){
            throw new ServiceException("请录入需要调拨的串码信息");
        }

        for(ImeAllotSimpleForm imeAllotSimpleForm : imeAllotBatchForm.getImeAllotSimpleFormList()){
            if(StringUtils.isBlank(imeAllotSimpleForm.getIme())){
                throw new ServiceException("串码不可以为空");
            }
            if(StringUtils.isBlank(imeAllotSimpleForm.getToDepotName())){
                throw new ServiceException("调拨后门店不可以为空");
            }
        }

        imeAllotService.batchAllot(imeAllotBatchForm);
        return new RestResponse("串码批量调拨成功", ResponseCodeEnum.saved.name());

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
    public ModelAndView export(ImeAllotQuery imeAllotQuery) {
        return new ModelAndView(new ExcelView(), "simpleExcelBook", imeAllotService.export(imeAllotQuery));
    }

}
