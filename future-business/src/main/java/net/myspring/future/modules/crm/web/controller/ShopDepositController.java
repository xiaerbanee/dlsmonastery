package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.enums.CloudSynExtendTypeEnum;
import net.myspring.future.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.enums.OutBillTypeEnum;
import net.myspring.future.common.enums.ShopDepositTypeEnum;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.api.service.K3cloudSynService;
import net.myspring.future.modules.crm.domain.Depot;
import net.myspring.future.modules.crm.domain.ShopDeposit;
import net.myspring.future.modules.crm.manager.CloudClientManager;
import net.myspring.future.modules.crm.service.BankService;
import net.myspring.future.modules.crm.service.DepotService;
import net.myspring.future.modules.crm.service.ShopDepositService;
import net.myspring.future.modules.sys.service.CompanyConfigService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/shopDeposit")
public class ShopDepositController {

    @Autowired
    private ShopDepositService shopDepositService;
    @Autowired
    private BankService bankService;
    @Autowired
    private K3cloudSynService k3cloudSynService;
    @Autowired
    private CloudClientManager cloudClientManager;
    @Autowired
    private CompanyConfigService companyConfigService;
    @Autowired
    private DepotService depotService;

    @ModelAttribute
    public ShopDeposit get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new ShopDeposit() : shopDepositService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<ShopDeposit> page = shopDepositService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(ShopDeposit shopDeposit: page.getContent()){
            shopDeposit.setActionList(getActionList(shopDeposit));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }


    @RequestMapping(value = "getDepartmentByShopId")
    public String getDepartmentByShopId(String shopId){
        String dbName=companyConfigService.findByCode(CompanyConfigCodeEnum.CLOUD_DB_NAME.getCode()).getValue();
        Depot depot=depotService.findOne(shopId);
        String result = cloudClientManager.findDepartByCustomer(dbName, depot.getOutId());
        return result;
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty(){
        Map<String,Object> map = Maps.newHashMap();
        map.put("outBillTypes", OutBillTypeEnum.values());
        map.put("banks",bankService.findAll());
        map.put("departMents", shopDepositService.getDepartments());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "getListProperty")
    public String getListProperty(){
        Map<String,Object> map = Maps.newHashMap();
        map.put("types", ShopDepositTypeEnum.values());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "delete")
    public String delete(ShopDeposit shopDeposit){
        ShopDeposit latest = shopDepositService.findLatest(shopDeposit.getShopId(), shopDeposit.getType());
        if(!latest.getId().equals(shopDeposit.getId())){
            return ObjectMapperUtils.writeValueAsString(new RestResponse("删除失败"));
        }else{
            shopDepositService.delete(shopDeposit);
            return ObjectMapperUtils.writeValueAsString(new RestResponse("删除成功"));
        }
    }

    @RequestMapping(value = "findOne")
    public String findOne(ShopDeposit shopDeposit){
        return ObjectMapperUtils.writeValueAsString(shopDeposit);
    }

    @RequestMapping(value = "save")
    public String save(ShopDeposit shopDeposit){
        Boolean isCreate= shopDeposit.getId()==null?true:false;
        if(isCreate){
            List<String> idList = shopDepositService.save(shopDeposit);
            if(!OutBillTypeEnum.不同步到金蝶.name().equals(shopDeposit.getOutBillType())){
                k3cloudSynService.syn(idList, CloudSynExtendTypeEnum.押金列表.name(),ShopDeposit.class);
            }
        }else{
            ShopDeposit latest = shopDepositService.findLatest(shopDeposit.getShopId(), shopDeposit.getType());
            if (!latest.getId().equals(shopDeposit.getId())) {
                return ObjectMapperUtils.writeValueAsString(new RestResponse("修改失败"));
            } else {
                shopDepositService.update(shopDeposit);
            }
        }

        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }

    private List<String> getActionList(ShopDeposit shopDeposit) {
        List<String> actionList = Lists.newArrayList();
        for(String authoritie : SecurityUtils.getAuthorityList()){
            switch (authoritie){
                case "crm:shopDeposit:delete" :
                    if(!shopDeposit.getLocked()){
                        actionList.add(Const.ITEM_ACTION_DELETE);
                    }
                    break;
                case "crm:shopDeposit:edit" :
                    if(!shopDeposit.getLocked()){
                        actionList.add(Const.ITEM_ACTION_EDIT);
                    }
                    break;
                default : break;
            }
        }
        return actionList;
    }

}
