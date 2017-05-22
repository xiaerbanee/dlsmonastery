package net.myspring.future.modules.crm.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.modules.crm.domain.PriceChangeIme;

import net.myspring.future.modules.crm.dto.PriceChangeImeDto;
import net.myspring.future.modules.crm.service.PriceChangeImeService;
import net.myspring.future.modules.crm.web.form.PriceChangeImeForm;
import net.myspring.future.modules.crm.web.query.PriceChangeImeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
        priceChangeImeQuery.setStatusList(AuditStatusEnum.getList());
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
    public RestResponse save(PriceChangeImeForm priceChangeImeForm) {
        priceChangeImeService.save(priceChangeImeForm);
        return new RestResponse("上传成功", ResponseCodeEnum.saved.name());
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

    private List<String> getActionList(PriceChangeIme priceChangeIme){
        return null;
    }
}
