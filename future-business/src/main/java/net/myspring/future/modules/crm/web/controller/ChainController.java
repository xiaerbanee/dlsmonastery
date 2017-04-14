package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.crm.domain.Chain;
import net.myspring.future.modules.crm.service.ChainService;
import net.myspring.future.modules.crm.service.DepotService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/chain")
public class ChainController {

    @Autowired
    private ChainService chainService;
    @Autowired
    private DepotService depotService;


    @ModelAttribute
    public Chain get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new Chain() : chainService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Page<Chain> page = chainService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(Chain chain: page.getContent()){
            chain.setActionList(getActionList());
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "delete")
    public String delete(Chain chain, BindingResult bindingResult) {
        chainService.delete(chain);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("删除成功"));
    }

    @RequestMapping(value = "save")
    public String save(Chain chain) {
        chainService.save(chain);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }

    @RequestMapping(value = "findOne")
    public String findOne(Chain chain){
        return ObjectMapperUtils.writeValueAsString(chain);
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty(){
        Map<String,Object> map= Maps.newHashMap();
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="getListProperty")
    public String getListProperty(){
        Map<String,Object> map= Maps.newHashMap();
        return ObjectMapperUtils.writeValueAsString(map);
    }

    private List<String> getActionList() {
        List<String> actionList = Lists.newArrayList();
        for(String authoritie : SecurityUtils.getAuthorityList()){
            switch (authoritie){
                case "crm:chain:delete" : actionList.add(Const.ITEM_ACTION_DELETE); break;
                case "crm:chain:edit" : actionList.add(Const.ITEM_ACTION_EDIT); break;
                default : break;
            }
        }
        return actionList;
    }
}
