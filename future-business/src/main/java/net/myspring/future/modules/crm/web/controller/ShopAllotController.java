package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.enums.MessageTypeEnum;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.enums.AuditTypeEnum;
import net.myspring.future.common.enums.CloudSynExtendTypeEnum;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.api.service.K3cloudSynService;
import net.myspring.future.modules.crm.domain.ShopAllot;
import net.myspring.future.modules.crm.domain.ShopAllotDetail;
import net.myspring.future.modules.crm.service.ShopAllotService;
import net.myspring.future.modules.hr.domain.Account;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/shopAllot")
public class ShopAllotController {

    @Autowired
    private ShopAllotService shopAllotService;
    @Autowired
    private K3cloudSynService k3cloudSynService;

    @ModelAttribute
    public ShopAllot get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new ShopAllot() : shopAllotService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<ShopAllot> page = shopAllotService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(ShopAllot shopAllot: page.getContent()){
            shopAllot.setActionList(getActionList(shopAllot));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "delete")
    public String delete(ShopAllot shopAllot) {
        shopAllotService.logicDeleteOne(shopAllot.getId());
        return ObjectMapperUtils.writeValueAsString(new RestResponse("删除成功"));
    }

    @RequestMapping(value = "save")
    public String save(ShopAllot shopAllot) {
        shopAllotService.save(shopAllot);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }

    @RequestMapping(value = "audit")
    public String audit(ShopAllot shopAllot){
        Account account = AccountUtils.getAccount();
        if(account.getOutId()==null){
            return ObjectMapperUtils.writeValueAsString(new RestResponse(false,"没有金蝶账户", MessageTypeEnum.error.name()));
        }
        shopAllotService.audit(shopAllot);
        if(shopAllot.getSyn()){
            //同步到金蝶
            k3cloudSynService.syn(shopAllot.getId(), CloudSynExtendTypeEnum.门店调拨.name());
        }
        return ObjectMapperUtils.writeValueAsString(new RestResponse("审核成功"));
    }

    @RequestMapping(value = "findOne")
    public String findOne(ShopAllot shopAllot){
        return ObjectMapperUtils.writeValueAsString(shopAllot);
    }

    @RequestMapping(value="getProducts")
    public String getProducts(String fromShopId,String toShopId){
        Map<String,Object> map= Maps.newHashMap();
        List<ShopAllotDetail> shopAllotDetailList = Lists.newArrayList();
        String message = shopAllotService.checkShop(fromShopId,toShopId);
        if(StringUtils.isEmpty(message)){
            shopAllotDetailList = shopAllotService.getProducts(fromShopId,toShopId);
        }
        map.put("success",StringUtils.isEmpty(message));
        map.put("message",message);
        map.put("shopAllotDetailList",shopAllotDetailList);
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="getEditFormData")
    public String getEditFormData(ShopAllot shopAllot){
        shopAllot = shopAllotService.getEditFormData(shopAllot);
        return ObjectMapperUtils.writeValueAsString(shopAllot);
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty(){
        Map<String,Object> map= Maps.newHashMap();
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="getListProperty")
    public String getListProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("status",AuditTypeEnum.getValues());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    private List<String> getActionList(ShopAllot shopAllot) {
        List<String> actionList = Lists.newArrayList();
        if(SecurityUtils.getAuthorityList().contains("crm:shopAllot:view")){
            actionList.add(Const.ITEM_ACTION_DETAIL);
        }
        if(AuditTypeEnum.APPLY.getValue().equals(shopAllot.getStatus())){
            for(String authoritie : SecurityUtils.getAuthorityList()){
                switch (authoritie){
                    case "crm:shopAllot:audit" : actionList.add(Const.ITEM_ACTION_AUDIT); break;
                    case "crm:shopAllot:delete" : actionList.add(Const.ITEM_ACTION_DELETE); break;
                    case "crm:shopAllot:edit" : actionList.add(Const.ITEM_ACTION_EDIT); break;
                    default : break;
                }
            }
        }
        return actionList;
    }

}
