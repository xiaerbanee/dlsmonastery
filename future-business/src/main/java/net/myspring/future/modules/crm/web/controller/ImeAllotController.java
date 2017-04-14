package net.myspring.future.modules.crm.web.controller;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.enums.MessageTypeEnum;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.enums.AuditTypeEnum;
import net.myspring.future.common.enums.BoolEnum;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.crm.domain.ImeAllot;
import net.myspring.future.modules.crm.model.ProductImeSearchResultModel;
import net.myspring.future.modules.crm.service.ImeAllotService;
import net.myspring.future.modules.crm.service.ProductImeService;
import net.myspring.future.modules.crm.validator.ImeAllotValidator;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/imeAllot")
public class ImeAllotController {

    @Autowired
    private ImeAllotService imeAllotService;
    @Autowired
    private ImeAllotValidator imeAllotValidator;
    @Autowired
    private ProductImeService productImeService;

    @ModelAttribute
    public ImeAllot get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new ImeAllot() : imeAllotService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getOfficeFilter(AccountUtils.getAccountId()));
        Page<ImeAllot> page  = imeAllotService.findPage(searchEntity.getPageable(), searchEntity.getParams());
        for(ImeAllot imeAllot:page.getContent()) {
            imeAllot.setActionList(getActionList(imeAllot));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }
    @RequestMapping(value = "detail")
    public String detail(ImeAllot imeAllot) {
        return ObjectMapperUtils.writeValueAsString(imeAllot);
    }

    @RequestMapping(value="getListProperty")
    public String form(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("status",AuditTypeEnum.getValues());
        map.put("bools", BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(map);
    }
    @RequestMapping(value = "delete")
    public String delete(String id) {
        imeAllotService.logicDeleteOne(id);
        RestResponse restResponse =new RestResponse("删除成功");
        return ObjectMapperUtils. writeValueAsString(restResponse);
    }

    @RequestMapping(value = "save")
    public String save(ImeAllot imeAllot, BindingResult bindingResult) {
        imeAllotValidator.validate(imeAllot,bindingResult);
        RestResponse restResponse =new RestResponse("调拨成功");
        if (bindingResult.hasErrors()) {
            restResponse =new RestResponse(false,bindingResult, MessageTypeEnum.error.name());
        }else {
            imeAllotService.save(imeAllot);
        }
        String response = ObjectMapperUtils.writeValueAsString(restResponse);
        return response;
    }

    @RequestMapping(value = "audit")
    public String audit(@RequestParam(value = "ids[]") String[] ids,Boolean pass){
        if(ids != null && ids.length > 0){
            imeAllotService.audit(ids, pass);
        }
        RestResponse restResponse =new RestResponse("审核成功");
        String response = ObjectMapperUtils.writeValueAsString(restResponse);
        return response;
    }

    @RequestMapping(value = "searchImeMap")
    public String searchImeMap(String imeStr){
        List<String> imeList = net.myspring.common.utils.StringUtils.getSplitList(imeStr, "\n");
        ProductImeSearchResultModel productImeSearchResultModel = productImeService.findProductImeSearchResult(imeList);
        Map<String,Object> map = imeAllotService.searchImeMap(productImeSearchResultModel);
       return ObjectMapperUtils.writeValueAsString(map);
    }

    private List<String> getActionList(ImeAllot imeAllot) {
        List<String> actionList = Lists.newArrayList();
        if(imeAllot.getCreatedBy().equals(ThreadLocalContext.get().getAccount().getId()) && AuditTypeEnum.APPLY.getValue().equals(imeAllot.getStatus()) && SecurityUtils.getAuthorityList().contains("crm:imeAllot:edit")) {
            actionList.add(Const.ITEM_ACTION_PASS);
            actionList.add(Const.ITEM_ACTION_NOT_PASS);
        }
        return actionList;
    }
}
