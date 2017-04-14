package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.crm.domain.Pricesystem;
import net.myspring.future.modules.crm.service.PricesystemService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "crm/pricesystem")
public class PricesystemController {

    @Autowired
    private PricesystemService pricesystemService;

    @ModelAttribute
    public Pricesystem find(@RequestParam(required = false)String id){
        return StringUtils.isBlank(id)?new Pricesystem():pricesystemService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Page<Pricesystem> page = pricesystemService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(Pricesystem pricesystem: page.getContent()){
            pricesystem.setActionList(getActionList());
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "delete")
    public String delete(Pricesystem pricesystem) {
        pricesystemService.logicDeleteOne(pricesystem);
        RestResponse restResponse =new RestResponse("删除成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "save")
    public String save(  Pricesystem pricesystem) {
        pricesystemService.save(pricesystem);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }

    @RequestMapping(value = "findOne")
    public String findOne(Pricesystem pricesystem){
        if(pricesystem.isCreate()){
            pricesystemService.initPricesystemDetail(pricesystem);
        }
        return ObjectMapperUtils.writeValueAsString(pricesystem);
    }

    private List<String> getActionList() {
        List<String> actionList = Lists.newArrayList();
        if(SecurityUtils.getAuthorityList().contains("crm:pricesystem:edit")){
            actionList.add(Const.ITEM_ACTION_EDIT);
            actionList.add(Const.ITEM_ACTION_DELETE);
        }
        return actionList;
    }

}
