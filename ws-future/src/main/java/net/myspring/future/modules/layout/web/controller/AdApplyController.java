package net.myspring.future.modules.layout.web.controller;

import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.layout.dto.AdApplyDto;
import net.myspring.future.modules.layout.service.AdApplyService;
import net.myspring.future.modules.layout.web.form.AdApplyBillForm;
import net.myspring.future.modules.layout.web.form.AdApplyBillTypeChangeForm;
import net.myspring.future.modules.layout.web.form.AdApplyForm;
import net.myspring.future.modules.layout.web.form.AdApplyGoodsForm;
import net.myspring.future.modules.layout.web.query.AdApplyQuery;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;


@RestController
@RequestMapping(value = "layout/adApply")
public class AdApplyController {

    @Autowired
    private AdApplyService adApplyService;


    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'layout:adApply:view')")
    public Page<AdApplyDto> findPage(Pageable pageable, AdApplyQuery adApplyQuery){
        return adApplyService.findPage(pageable,adApplyQuery);
    }

    @RequestMapping(value = "getQuery", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'layout:adApply:view')")
    public AdApplyQuery getQuery(AdApplyQuery adApplyQuery) {
        return adApplyQuery;
    }

    @RequestMapping(value = "getForm", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'layout:adApply:view')")
    public AdApplyForm getForm(AdApplyForm adApplyForm) {
        return adApplyService.getForm(adApplyForm);
    }

    @RequestMapping(value = "findOne")
    @PreAuthorize("hasPermission(null,'layout:adApply:view')")
    public AdApplyDto findOne(String id){
        return adApplyService.findOne(id);
    }

    @RequestMapping(value = "save")
    @PreAuthorize("hasPermission(null,'layout:adApply:edit')")
    public RestResponse save(AdApplyForm adApplyForm){
        adApplyService.save(adApplyForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "billSave")
    @PreAuthorize("hasPermission(null,'layout:adApply:bill')")
    public RestResponse billSave(AdApplyBillForm adApplyBillForm){
        adApplyService.billSave(adApplyBillForm);
        return new RestResponse("开单申请成功", ResponseCodeEnum.saved.name());
    }


    @RequestMapping(value = "getBillForm", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'layout:adApply:bill')")
    public AdApplyBillForm getBillForm(AdApplyBillForm adApplyBillForm){
        return adApplyService.getBillForm(adApplyBillForm);
    }

    @RequestMapping(value = "findAdApplyList", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'layout:adApply:bill')")
    public AdApplyBillTypeChangeForm findAdApplyList(String billType){
        if(StringUtils.isBlank(billType)){
            throw new ServiceException("未选中开单类型");
        }
        return adApplyService.findAdApplyList(billType);
    }

    @RequestMapping(value = "getAdApplyGoodsList")
    @PreAuthorize("hasPermission(null,'layout:adApply:goods')")
    public AdApplyGoodsForm getAdApplyGoodsList(AdApplyGoodsForm adApplyGoodsForm){
        return adApplyService.getAdApplyGoodsList(adApplyGoodsForm);
    }
    @RequestMapping(value = "goodsSave")
    @PreAuthorize("hasPermission(null,'layout:adApply:goods')")
    public RestResponse  goodsSave(AdApplyGoodsForm adApplyGoodsForm){
        adApplyService.goodsSave(adApplyGoodsForm);
        return new RestResponse("分货成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'layout:adApply:view')")
    public ModelAndView export(AdApplyQuery adApplyQuery) throws IOException {
        return new ModelAndView(new ExcelView(),"simpleExcelBook",adApplyService.export(adApplyQuery));
    }
}
