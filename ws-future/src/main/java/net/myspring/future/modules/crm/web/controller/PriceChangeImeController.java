package net.myspring.future.modules.crm.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.modules.crm.dto.PriceChangeImeDto;
import net.myspring.future.modules.crm.service.PriceChangeImeService;
import net.myspring.future.modules.crm.web.PriceChangeImeValidator;
import net.myspring.future.modules.crm.web.form.PriceChangeImeForm;
import net.myspring.future.modules.crm.web.form.PriceChangeImeUploadForm;
import net.myspring.future.modules.crm.web.query.PriceChangeImeQuery;
import net.myspring.util.excel.ExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
@RequestMapping(value = "crm/priceChangeIme")
public class PriceChangeImeController {

    @Autowired
    private PriceChangeImeService priceChangeImeService;
    @Autowired
    private PriceChangeImeValidator priceChangeImeValidator;

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:priceChangeIme:view')")
    public Page<PriceChangeImeDto> list(Pageable pageable, PriceChangeImeQuery priceChangeImeQuery) {
        return priceChangeImeService.findPage(pageable,priceChangeImeQuery);
    }

    @RequestMapping(value = "getQuery")
    @PreAuthorize("hasPermission(null,'crm:priceChangeIme:view')")
    public PriceChangeImeQuery getQuery(PriceChangeImeQuery priceChangeImeQuery){
        priceChangeImeQuery.getExtra().put("statusList",AuditStatusEnum.getList());
        return priceChangeImeQuery;
    }


    @RequestMapping(value = "getForm" ,method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:priceChangeIme:view')")
    public PriceChangeImeForm detail(PriceChangeImeForm priceChangeImeForm){
        return priceChangeImeService.getForm(priceChangeImeForm);
    }

    @RequestMapping(value = "findOne")
    @PreAuthorize("hasPermission(null,'crm:priceChangeIme:view')")
    public PriceChangeImeDto findForm(String id){
        return priceChangeImeService.findOne(id);
    }

    @RequestMapping(value = "save")
    @PreAuthorize("hasPermission(null,'crm:priceChangeIme:edit')")
    public RestResponse save(PriceChangeImeUploadForm priceChangeImeUploadForm) {
        priceChangeImeService.save(priceChangeImeUploadForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete")
    @PreAuthorize("hasPermission(null,'crm:priceChangeIme:edit')")
    public RestResponse delete(String id){
        priceChangeImeService.delete(id);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "imageUpload")
    @PreAuthorize("hasPermission(null,'crm:priceChangeIme:edit')")
    public RestResponse imageUpload(PriceChangeImeForm priceChangeImeForm,BindingResult bindingResult) {
        priceChangeImeValidator.validate(priceChangeImeForm,bindingResult);
        if(bindingResult.hasErrors()){
            return  new RestResponse(bindingResult,"上报失败", null);
        }
        priceChangeImeService.imageUpload(priceChangeImeForm);
        return new RestResponse("上报成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "audit")
    @PreAuthorize("hasPermission(null,'crm:priceChangeIme:audit')")
    public RestResponse audit(PriceChangeImeForm priceChangeImeForm){
        priceChangeImeService.audit(priceChangeImeForm);
        return new RestResponse("审核成功", ResponseCodeEnum.audited.name());
    }

    @RequestMapping(value = "batchAudit")
    @PreAuthorize("hasPermission(null,'crm:priceChangeIme:audit')")
    public RestResponse batchAudit(@RequestParam(value = "ids[]")String[] ids){
        priceChangeImeService.batchAudit(ids);
        return new RestResponse("审核成功", ResponseCodeEnum.audited.name());
    }

    @RequestMapping(value = "export",method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:priceChangeIme:view')")
    public ModelAndView export(PriceChangeImeQuery priceChangeImeQuery) throws IOException{
        return new ModelAndView(new ExcelView(),"simpleExcelBook",priceChangeImeService.export(priceChangeImeQuery));
    }
}
