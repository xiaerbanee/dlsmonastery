package net.myspring.future.modules.crm.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.modules.crm.domain.PriceChangeIme;

import net.myspring.future.modules.crm.dto.PriceChangeImeDto;
import net.myspring.future.modules.crm.service.PriceChangeImeService;
import net.myspring.future.modules.crm.web.form.PriceChangeImeForm;
import net.myspring.future.modules.crm.web.form.PriceChangeImeUploadForm;
import net.myspring.future.modules.crm.web.query.PriceChangeImeQuery;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.excel.SimpleExcelBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/priceChangeIme")
public class PriceChangeImeController {

    @Autowired
    private PriceChangeImeService priceChangeImeService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<PriceChangeImeDto> list(Pageable pageable, PriceChangeImeQuery priceChangeImeQuery) {
        return priceChangeImeService.findPage(pageable,priceChangeImeQuery);
    }

    @RequestMapping(value = "getQuery")
    public PriceChangeImeQuery getQuery(PriceChangeImeQuery priceChangeImeQuery){
        priceChangeImeQuery.getExtra().put("statusList",AuditStatusEnum.getList());
        return priceChangeImeQuery;
    }


    @RequestMapping(value = "getForm" ,method = RequestMethod.GET)
    public PriceChangeImeForm detail(PriceChangeImeForm priceChangeImeForm){
        return priceChangeImeService.getForm(priceChangeImeForm);
    }

    @RequestMapping(value = "findOne")
    public PriceChangeImeDto findForm(String id){
        return priceChangeImeService.findOne(id);
    }

    @RequestMapping(value = "save")
    public RestResponse save(PriceChangeImeUploadForm priceChangeImeUploadForm) {
        String info = priceChangeImeService.save(priceChangeImeUploadForm);
        if(info.equalsIgnoreCase("保存成功")){
            return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        }else{
            return new RestResponse(info, ResponseCodeEnum.saved.name(),false);
        }

    }

    @RequestMapping(value = "imageUpload")
    public RestResponse imageUpload(PriceChangeImeForm priceChangeImeForm) {
        priceChangeImeService.imageUpload(priceChangeImeForm);
        return new RestResponse("上报成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "audit")
    public RestResponse audit(PriceChangeImeForm priceChangeImeForm){
        priceChangeImeService.audit(priceChangeImeForm);
        return new RestResponse("审核成功", ResponseCodeEnum.saved.name());
    }
}
