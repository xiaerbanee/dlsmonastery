package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.enums.MessageTypeEnum;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.enums.CloudSynExtendTypeEnum;
import net.myspring.future.common.enums.EmployeePhoneDepositStatusEnum;
import net.myspring.future.common.enums.OutBillTypeEnum;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.api.service.K3cloudService;
import net.myspring.future.modules.api.service.K3cloudSynService;
import net.myspring.future.modules.crm.domain.Depot;
import net.myspring.future.modules.crm.domain.ShopGoodsDeposit;
import net.myspring.future.modules.crm.model.BdSettleTypeModel;
import net.myspring.future.modules.crm.service.BankService;
import net.myspring.future.modules.crm.service.DepotService;
import net.myspring.future.modules.crm.service.ShopGoodsDepositService;
import net.myspring.future.modules.sys.service.ProcessFlowService;
import net.myspring.future.modules.sys.service.ProcessTypeService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/shopGoodsDeposit")
public class ShopGoodsDepositController {

    @Autowired
    private ShopGoodsDepositService shopGoodsDepositService;
    @Autowired
    private K3cloudSynService k3cloudSynService;
    @Autowired
    private BankService bankService;
    @Autowired
    private K3cloudService k3cloudService;
    @Autowired
    private DepotService depotService;
    @Autowired
    private ProcessTypeService processTypeService;
    @Autowired
    private ProcessFlowService processFlowService;


    @ModelAttribute
    public ShopGoodsDeposit get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new ShopGoodsDeposit() : shopGoodsDepositService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<ShopGoodsDeposit> page = shopGoodsDepositService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(ShopGoodsDeposit shopGoodsDeposit: page.getContent()){
            shopGoodsDeposit.setActionList(getActionList());
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }


    @RequestMapping(value = "getFormProperty")
    public String form(Model model) {
        Map<String,Object> map= Maps.newHashMap();
        map.put("banks", bankService.findAll());
        map.put("departMents",k3cloudService.findAllDepartments());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "getListProperty")
    public String getListProperty(Model model) {
        Map<String,Object> map= Maps.newHashMap();
        map.put("outBillTypes", OutBillTypeEnum.values());
        map.put("status", EmployeePhoneDepositStatusEnum.values());
        return ObjectMapperUtils.writeValueAsString(map);
    }


    @RequestMapping(value = "searchDepartMent")
    public String searchDepartMent(String shopId){
        Map<String,Object> map = Maps.newHashMap();
        Depot shop = depotService.findOne(shopId);
        String departMentJson=k3cloudService.findDepartByCustomer(shop.getOutId());
        BdSettleTypeModel bdSettleTypeModel = ObjectMapperUtils.readValue(departMentJson,BdSettleTypeModel.class);
        map.put("departMent",bdSettleTypeModel==null?"":bdSettleTypeModel.getFnumber());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "save")
    public String save(ShopGoodsDeposit shopGoodsDeposit){
        shopGoodsDepositService.save(shopGoodsDeposit);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }


    @RequestMapping(value = "audit")
    @ResponseBody
    public String batchSave(@RequestParam(value = "ids[]") String[] ids) {
        List<ShopGoodsDeposit> shopGoodsDepositList = shopGoodsDepositService.findByIds(Arrays.asList(ids));
        StringBuffer sb = new StringBuffer();
        for(ShopGoodsDeposit shopGoodsDeposit:shopGoodsDepositList){
            if(StringUtils.isBlank(shopGoodsDeposit.getShop().getRealOutId())){
                sb.append("审核失败,"+shopGoodsDeposit.getShop().getName()+"没有绑定财务门店；\n");
            }
        }
        if(StringUtils.isNotBlank(sb)){
            return ObjectMapperUtils.writeValueAsString(new RestResponse(false,sb.toString(), MessageTypeEnum.error.name()));
        }
        shopGoodsDepositService.audit(ids);
        for(String id:ids){
            k3cloudSynService.syn(id, CloudSynExtendTypeEnum.定金收款.name(),ShopGoodsDeposit.class);
        }
        return ObjectMapperUtils.writeValueAsString(new RestResponse("审核成功"));
    }

    @RequestMapping(value = "delete")
    public String delete(ShopGoodsDeposit shopGoodsDeposit, BindingResult bindingResult) {
        shopGoodsDepositService.delete(shopGoodsDeposit);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("删除成功"));
    }

    @RequestMapping(value = "findOne")
    public String findOne(ShopGoodsDeposit shopGoodsDeposit){
        return ObjectMapperUtils.writeValueAsString(shopGoodsDeposit);
    }

    private List<String> getActionList() {
        List<String> actionList = Lists.newArrayList();
        if(SecurityUtils.getAuthorityList().contains("crm:shopGoodsDeposit:delete")){
            actionList.add(Const.ITEM_ACTION_DELETE);
        }
        return actionList;
    }


}
