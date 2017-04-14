package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.enums.MessageTypeEnum;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.crm.domain.Dealer;
import net.myspring.future.modules.crm.service.DealerService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "crm/dealer")
public class DealerController {

    @Autowired
    private DealerService dealerService;

    @ModelAttribute
    public Dealer get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new Dealer() : dealerService.findOne(id);
    }

    @RequestMapping(method= RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<Dealer> page = dealerService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(Dealer dealer: page.getContent()){
            dealer.setActionList(getActionList(dealer));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "save")
    public RestResponse save(Dealer dealer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new RestResponse(false,bindingResult, MessageTypeEnum.error.name());
        } else {
            dealerService.save(dealer);
        }
        return new RestResponse("保存成功");
    }

    @RequestMapping(value = "findOne")
    public String findOne(Dealer dealer){
        return ObjectMapperUtils.writeValueAsString(dealer);
    }

    private List<String> getActionList(Dealer dealer) {
        List<String> actionList = Lists.newArrayList();
        //门店才允许在手机端修改和终端统计
        if(SecurityUtils.getAuthorityList().contains("crm:dealer:edit")){
            actionList.add(Const.ITEM_ACTION_EDIT);
        }
        return actionList;
    }
}
