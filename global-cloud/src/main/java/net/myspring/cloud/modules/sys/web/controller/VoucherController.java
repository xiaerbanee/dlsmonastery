package net.myspring.cloud.modules.sys.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.DynamicDataSourceContext;
import net.myspring.cloud.common.enums.DateFormat;
import net.myspring.cloud.common.enums.VoucherStatusEnum;
import net.myspring.cloud.common.utils.SecurityUtils;
import net.myspring.cloud.modules.kingdee.model.GlVoucherModel;
import net.myspring.cloud.modules.kingdee.service.GlVoucherService;
import net.myspring.cloud.modules.sys.dto.VoucherDto;
import net.myspring.cloud.modules.sys.service.VoucherService;
import net.myspring.cloud.modules.sys.web.query.VoucherQuery;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.ServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private SecurityUtils securityUtils;
    @Autowired
    private GlVoucherService glVoucherService;

    @RequestMapping(value = "list")
    public Map<String,Object> list(Pageable pageable, VoucherQuery voucherQuery, String companyId) {
        Map<String,Object> map = Maps.newHashMap();
        if(StringUtils.isNotBlank(companyId)) {
            DynamicDataSourceContext.get().setAutomaticSetCompany(false);
            DynamicDataSourceContext.get().setCompanyId(companyId);
            voucherQuery.setCompanyId(companyId);
        }else{
            voucherQuery.setCompanyId(DynamicDataSourceContext.get().getCompanyId());
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
            DynamicDataSourceContext.get().setAutomaticSetCompany(false);
            DynamicDataSourceContext.get().setCompanyId(companyId);
        }
        if(StringUtils.isNotBlank(companyId)){
            GlVoucherModel glVoucherModel = glVoucherService.getGlVoucherModel();
            map.putAll(glVoucherService.getFormProperty());
            map.putAll(voucherService.getFormProperty(glVoucherModel,voucherDto));
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
            GlVoucherModel glVoucherModel = glVoucherService.getGlVoucherModel();
            restResponse.setErrors(voucherService.check(datas,glVoucherModel));
            if (restResponse.getErrors().size()>0) {
                return restResponse;
            }else{
                voucherDto.setfDate(LocalDate.parse(billDate, DateTimeFormatter.ofPattern(DateFormat.DATE.getValue())));
                voucherService.save(datas,voucherDto,glVoucherModel);
                restResponse.setMessage("凭证保存成功");
                return restResponse;
            }
        }else{
            restResponse = new RestResponse("页面已经提交过，请核对系统中是否已保存",null);
        }
        return restResponse;
    }

    public List<String> getActionList(VoucherDto voucherDto){
        List<String> actionList = Lists.newArrayList();
        if (VoucherStatusEnum.地区财务审核.name().equals(voucherDto.getStatus()) && securityUtils.getAccountId() == null) {
            voucherDto.setEditable(true);
            voucherDto.setDeletable(true);
        } else if (!VoucherStatusEnum.已完成.name().equals(voucherDto.getStatus()) && securityUtils.getAccountId() != null) {
            voucherDto.setEditable(true);
            voucherDto.setDeletable(true);
        }
        //待定
//        if(SecurityUtils.getAuthorityList().contains("crm:afterSale:delete")){
//            actionList.add(Const.ITEM_ACTION_DELETE);
//        }
        return actionList;
    }

}
