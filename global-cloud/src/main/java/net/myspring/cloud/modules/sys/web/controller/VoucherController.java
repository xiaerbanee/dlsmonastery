package net.myspring.cloud.modules.sys.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.enums.VoucherStatusEnum;
import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.sys.dto.VoucherFormDto;
import net.myspring.cloud.modules.kingdee.service.GlVoucherService;
import net.myspring.cloud.modules.sys.dto.VoucherDto;
import net.myspring.cloud.modules.sys.service.VoucherService;
import net.myspring.cloud.modules.sys.web.form.VoucherForm;
import net.myspring.cloud.modules.sys.web.query.VoucherQuery;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lihx on 2017/4/5.
 */
@RestController
@RequestMapping(value = "sys/glVoucherDto")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @Autowired
    private GlVoucherService glVoucherService;

    @ModelAttribute
    public VoucherForm get(@RequestParam(required = false) String id) {
        VoucherDto voucherdto = StringUtils.isBlank(id) ? new VoucherDto() : voucherService.findOne(id);
        VoucherForm voucherForm = BeanUtil.map(voucherdto,VoucherForm.class);
        return voucherForm;
    }

    @RequestMapping(value = "list")
    public Map<String,Object> list(Pageable pageable, VoucherQuery voucherQuery, String companyId) {
        Map<String,Object> map = Maps.newHashMap();
        if(StringUtils.isNotBlank(companyId)) {
            voucherQuery.setCompanyId(companyId);
        }else{
            voucherQuery.setCompanyId(RequestUtils.getCompanyId());
        }
        Page<VoucherDto> page = voucherService.findPage(pageable, voucherQuery);
        for (VoucherDto voucherDto : page.getContent()) {
            voucherDto.setActionList(getActionList(voucherDto));
        }
        map.put("page", page);
        map.put("companyId", companyId);
        //用户所拥有的kingdeeName
//        List<Company> companies = accountService.getCompanyList(ThreadLocalContext.get().getAccount().getId());
//        map.put("clouds", Collections3.extractToList(companies, "name"));
        return map;
    }

    @RequestMapping(value = "getFormProperty")
    public Map<String,Object> getFormProperty(String companyId,VoucherDto voucherDto) {
        Map<String,Object> map = Maps.newHashMap();
        if (voucherDto.getId() != null) {
            voucherDto = voucherService.findOne(voucherDto.getId());
        }
        if(StringUtils.isNotBlank(companyId)){
            VoucherFormDto voucherFormDto = glVoucherService.getGlVoucherDto();
            map.putAll(glVoucherService.getFormProperty());
            map.putAll(voucherService.getFormProperty(voucherFormDto,voucherDto));
        }
        map.put("companyId", companyId);
        //用户所拥有的kingdeeName
//        map.put("kingdeeBookList",kingdeeBookService.findAll());
        return map;
    }

    @RequestMapping(value = "save")
    public RestResponse save(String data, String billDate, VoucherDto voucherDto, ServletRequest request) {
        RestResponse restResponse = null;
        if("false".equals(request.getAttribute("doubleSubmit").toString())) {
            data = HtmlUtils.htmlUnescape(data);
            List<List<Object>> datas = ObjectMapperUtils.readValue(data, ArrayList.class);
            VoucherFormDto voucherFormDto = glVoucherService.getGlVoucherDto();
            restResponse.setErrors(voucherService.check(datas, voucherFormDto));
            if (restResponse.getErrors().size()>0) {
                return restResponse;
            }else{
//                voucherDto.setfDate(LocalDate.parse(billDate, DateTimeFormatter.ofPattern(DateFormat.DATE.getValue())));
//                voucherService.save(datas,voucherDto,voucherFormDto);
                restResponse.setMessage("凭证保存成功");
                return restResponse;
            }
        }else{
            restResponse = new RestResponse("页面已经提交过，请核对系统中是否已保存",null);
        }
        return restResponse;
    }

    @RequestMapping(value = "audit")
    public RestResponse audit(VoucherForm voucherForm, String billDate,String data,RedirectAttributes redirectAttributes,ServletRequest request) {
        RestResponse restResponse = null;
        if("false".equals(request.getAttribute("doubleSubmit").toString())) {
            data = HtmlUtils.htmlUnescape(data);
            List<List<Object>> datas = ObjectMapperUtils.readValue(data, ArrayList.class);
            VoucherFormDto voucherFormDto = glVoucherService.getGlVoucherDto();
            restResponse.setErrors(voucherService.check(datas, voucherFormDto));
            if (restResponse.getErrors().size()>0) {
                voucherForm.setfDate(LocalDateUtils.parse(billDate));
//                VoucherForm voucherForm = voucherService.save(datas,voucherForm,voucherFormDto);
                if(VoucherStatusEnum.地区财务审核.name().equals(voucherForm.getStatus())){
                    voucherForm.setStatus(VoucherStatusEnum.省公司财务审核.name());
                }else{
                    voucherForm.setStatus(VoucherStatusEnum.已完成.name());
                }
//                restResponse.setMessage(voucherService.audit(voucherForm));
//                if (VoucherStatusEnum.已完成.name().equals(voucherForm.getStatus())) {
//                    String outCode = voucherService.syn(voucherForm);
//                    outCode = "序号：" + outCode + "  凭证号：" + basicDataService.findFvoucherNoByBillNo(outCode);
//                    k3cloudGlVoucherService.saveBillNo(voucherForm,outCode);
//                    message=new Message("凭证录入成功" );
//                }
            }
//            redirectAttributes.addFlashAttribute("message", message);
        }else{
//            redirectAttributes.addFlashAttribute("message", new Message("页面已经提交过，请核对系统中是否已保存"));
        }
        return restResponse;
    }

    public List<String> getActionList(VoucherDto voucherDto){
        List<String> actionList = Lists.newArrayList();
        if (VoucherStatusEnum.地区财务审核.name().equals(voucherDto.getStatus()) && RequestUtils.getAccountId() == null) {
            voucherDto.setEditable(true);
            voucherDto.setDeletable(true);
        } else if (!VoucherStatusEnum.已完成.name().equals(voucherDto.getStatus()) && RequestUtils.getAccountId() != null) {
            voucherDto.setEditable(true);
            voucherDto.setDeletable(true);
        }
        //待定
//        if(RequestUtils.getAuthorityList().contains("crm:afterSale:delete")){
//            actionList.add(Const.ITEM_ACTION_DELETE);
//        }
        return actionList;
    }

}
