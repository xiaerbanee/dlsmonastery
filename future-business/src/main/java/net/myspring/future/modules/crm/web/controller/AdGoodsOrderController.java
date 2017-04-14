package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.enums.MessageTypeEnum;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.common.utils.StringUtils;
import net.myspring.future.common.activiti.ActivitiEntity;
import net.myspring.future.common.activiti.ActivitiUtils;
import net.myspring.future.common.enums.*;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.CacheUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.api.service.K3cloudSynService;
import net.myspring.future.modules.crm.domain.*;
import net.myspring.future.modules.crm.service.*;
import net.myspring.future.modules.crm.validator.AdGoodsOrderValidator;
import net.myspring.future.modules.sys.domain.ProcessFlow;
import net.myspring.future.modules.sys.domain.ProcessType;
import net.myspring.future.modules.sys.service.CompanyConfigService;
import net.myspring.future.modules.sys.service.ProcessFlowService;
import net.myspring.future.modules.sys.service.ProcessTypeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/adGoodsOrder")
public class  AdGoodsOrderController {

    @Autowired
    private AdGoodsOrderService adGoodsOrderService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private DepotService depotService;
    @Autowired
    private ProcessTypeService processTypeService;
    @Autowired
    private ProcessFlowService processFlowService;
    @Autowired
    private ExpressCompanyService expressCompanyService;
    @Autowired
    private ExpressOrderService expressOrderService;
    @Autowired
    private ShopDepositService shopDepositService;
    @Autowired
    private CompanyConfigService companyConfigService;
    @Autowired
    private K3cloudSynService k3cloudSynService;
    @Autowired
    private AdGoodsOrderValidator adGoodsOrderValidator;


    @ModelAttribute
    public AdGoodsOrder get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new AdGoodsOrder() : adGoodsOrderService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<AdGoodsOrder> page = adGoodsOrderService.findPage(searchEntity.getPageable(), searchEntity.getParams());
        for (AdGoodsOrder adGoodsOrder : page.getContent()) {
            adGoodsOrder.setActionList(getActionList(adGoodsOrder));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "getListProperty")
    public String getListProperty() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("billTypes", BillTypeEnum.values());
        map.put("stores", depotService.findStores());
        map.put("areaTypes", CacheUtils.findDictMapByCategory(DictMapCategoryEnum.门店_地区属性.name()));
        ProcessType processType = processTypeService.findByName(AdGoodsOrder.class.getSimpleName());
        List<ProcessFlow> processFlows = processFlowService.findByProcessType(processType.getId());
        map.put("processFlows", processFlows);
        return ObjectMapperUtils.writeValueAsString(map);
    }


    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail(AdGoodsOrder adGoodsOrder) {
        Map<String, Object> map = Maps.newHashMap();
        if (StringUtils.isNotBlank(adGoodsOrder.getProcessInstanceId())) {
            map.put("activitiEntity", ActivitiUtils.getActivitiEntity(adGoodsOrder.getProcessInstanceId()));
        }
        map.put("adGoodsOrder", adGoodsOrder);
        map.put("bools", BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "audit")
    public String audit(AdGoodsOrder adGoodsOrder, boolean pass, String comment) {
        RestResponse restResponse = new RestResponse("柜台审核成功");
        if (AdGoodsOrderStatusEnum.待开单.name().equals(adGoodsOrder.getProcessStatus()) || AdGoodsOrderStatusEnum.待发货.name().equals(adGoodsOrder.getProcessStatus()) || AdGoodsOrderStatusEnum.待签收.name().equals(adGoodsOrder.getProcessStatus())) {
            restResponse = new RestResponse(false, "柜台审核失败", MessageTypeEnum.error.name());
        }
        Task task = taskService.createTaskQuery().processInstanceId(adGoodsOrder.getProcessInstanceId()).singleResult();
        if (!ActivitiUtils.claim(task)) {
            restResponse = new RestResponse(false, "无法签收任务，您没有办理此任务的权限或者已经被其他人签收", MessageTypeEnum.error.name());
        } else {
            adGoodsOrderService.audit(adGoodsOrder, pass, comment);
        }
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "outShopChange", method = RequestMethod.GET)
    public String outShopChange(AdGoodsOrder adGoodsOrder) {
        Map<String, Object> map = Maps.newHashMap();
        adGoodsOrderService.getAdGoodsOrderDetail(adGoodsOrder);
        if (StringUtils.isNotBlank(adGoodsOrder.getOutShopId())) {
            Depot depot = depotService.findOne(adGoodsOrder.getOutShopId());
            adGoodsOrder.setOutShop(depot);
            String outId = depot.getParent() == null ? depot.getOutId() : depot.getParent().getOutId();
            if (outId != null && (DepotTypeEnum.大库_代理.getValue().equals(depot.getType()) || DepotTypeEnum.门店_直营_分店.getValue().equals(depot.getType()))) {
                map.put("adShop", "true");
            }
        }
        map.put("adGoodsOrder", adGoodsOrder);
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "findOne", method = RequestMethod.GET)
    public String findOne(AdGoodsOrder adGoodsOrder) {
        if (adGoodsOrder.getExpressOrder() == null) {
            ExpressOrder expressOrder = new ExpressOrder();
            adGoodsOrder.setExpressOrder(expressOrder);
        }
        if (adGoodsOrder.getExpressOrder().getExpressCompanyId() == null) {
            adGoodsOrder.getExpressOrder().setExpressCompanyId(companyConfigService.findByCode(CompanyConfigCodeEnum.DEFAULT_AD_EXPRESS_COMPANY_ID.getCode()).getValue());
        }
        return ObjectMapperUtils.writeValueAsString(adGoodsOrder);
    }

    @RequestMapping(value = "getFormProperty", method = RequestMethod.GET)
    public String getFormProperty() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("expressCompanys", expressCompanyService.findAll());
        map.put("bools", BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public RestResponse save(AdGoodsOrder adGoodsOrder, BindingResult bindingResult) {
        adGoodsOrderValidator.validate(adGoodsOrder,bindingResult);
        if (bindingResult.hasErrors()) {
            return new RestResponse(false, bindingResult, MessageTypeEnum.error.name());
        }
        ProcessType processType = processTypeService.findByName(AdGoodsOrder.class.getSimpleName());
        adGoodsOrder.setProcessType(processType);
        adGoodsOrder.setProcessTypeId(processType.getId());
        adGoodsOrderService.save(adGoodsOrder);
        if (adGoodsOrder.getId() != null && StringUtils.isNotBlank(adGoodsOrder.getExpressOrder().getExpressCodes())) {
            expressOrderService.save(ExpressOrderTypeEnum.物料订单.name(), adGoodsOrder.getId(), adGoodsOrder.getExpressOrder().getExpressCodes(), adGoodsOrder.getExpressOrder().getExpressCompanyId());
        }
        return new RestResponse("保存成功");
    }

    @RequestMapping(value = "getBillFormProperty", method = RequestMethod.GET)
    public String getBillFormProperty(AdGoodsOrder adGoodsOrder) {
        Map<String, Object> map = Maps.newHashMap();
        String outGroupId = companyConfigService.findByCode(CompanyConfigCodeEnum.STORE_AD_GROUP_IDS.name()).getValue();
        List<Depot> adStores = depotService.findByOutGroupId(outGroupId);
        map.put("adStores", adStores);
        map.put("billDate", LocalDate.now());
        map.put("expressCompanys", expressCompanyService.findAll());
        ShopDeposit shopDeposit = shopDepositService.findLatest(adGoodsOrder.getShopId(), ShopDepositTypeEnum.形象保证金.toString());
        BigDecimal imageDeposit = shopDeposit == null ? BigDecimal.ZERO : shopDeposit.getLeftAmount();
        map.put("imageDeposit", imageDeposit);
        map.put("bools", BoolEnum.getMap());
        map.put("activitiEntity",new ActivitiEntity() );
        if (StringUtils.isNotBlank(adGoodsOrder.getProcessInstanceId())) {
            map.put("activitiEntity", ActivitiUtils.getActivitiEntity(adGoodsOrder.getProcessInstanceId()));
        }
        map.put("type", "标准出库单");
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "bill", method = RequestMethod.GET)
    public String bill(AdGoodsOrder adGoodsOrder) {
        if (adGoodsOrder.getStoreId() == null) {
            String defaultStoreId = companyConfigService.findByCode(CompanyConfigCodeEnum.PRODUCT_COUNTER_GROUP_IDS.getCode()).getValue();
            adGoodsOrder.setStoreId(defaultStoreId);
        }
        adGoodsOrder.setBillDate(LocalDate.now());
        adGoodsOrderService.getAdGoodsOrderDetail(adGoodsOrder);
        return ObjectMapperUtils.writeValueAsString(adGoodsOrder);
    }

    @RequestMapping(value = "bill", method = RequestMethod.POST)
    public RestResponse bill(Model model, AdGoodsOrder adGoodsOrder, RedirectAttributes redirectAttributes) {
        if (!AdGoodsOrderStatusEnum.待开单.name().equals(adGoodsOrder.getProcessStatus())) {
            return new RestResponse(false, "不是待开单状态，无法开单", MessageTypeEnum.error.name());
        }
        // 检查用户
        if (adGoodsOrder.getSyn()) {
            if (StringUtils.isBlank(AccountUtils.getAccount().getOutId())) {
                return new RestResponse(false, "你没有开单权限", MessageTypeEnum.error.name());
            }
        }
        adGoodsOrderService.bill(adGoodsOrder);
        if (adGoodsOrder.getSyn()) {
            k3cloudSynService.syn(adGoodsOrder.getId(), CloudSynExtendTypeEnum.柜台订货.name(),AdGoodsOrder.class);
            if (adGoodsOrder.getExpressOrder() != null) {
                ExpressOrder expressOrder = adGoodsOrder.getExpressOrder();
                expressOrder.setOutCode(adGoodsOrder.getOutCode());
                expressOrderService.update(expressOrder);
            }
        }
        return new RestResponse("开单成功");
    }

    @RequestMapping(value = "getShipFormProperty", method = RequestMethod.GET)
    public String getShipFormProperty(AdGoodsOrder adGoodsOrder) {
        Map<String, Object> map = Maps.newHashMap();
        map.putAll(adGoodsOrderService.getAmountMap(adGoodsOrder));
        BigDecimal smallPrice = BigDecimal.ZERO;
        BigDecimal mediumPrice = BigDecimal.ZERO;
        BigDecimal bigPrice = BigDecimal.ZERO;
        String adShipPriceRule = companyConfigService.findByCode(CompanyConfigCodeEnum.AD_SHIP_PRICE_RULE.getCode()).getValue();
        if (StringUtils.isNotBlank(adShipPriceRule)) {
            List<Map<String, Object>> adShipPriceRules = ObjectMapperUtils.readValue(adShipPriceRule, List.class);
            smallPrice = new BigDecimal(String.valueOf(adShipPriceRules.get(0).get("small")));
            mediumPrice = new BigDecimal(String.valueOf(adShipPriceRules.get(0).get("medium")));
            bigPrice = new BigDecimal(String.valueOf(adShipPriceRules.get(0).get("big")));
        }
        map.put("expressCompanys", expressCompanyService.findAll());
        map.put("smallPrice", smallPrice);
        map.put("mediumPrice", mediumPrice);
        map.put("bigPrice", bigPrice);
        if (StringUtils.isNotBlank(adGoodsOrder.getProcessInstanceId())) {
            map.put("activitiEntity", ActivitiUtils.getActivitiEntity(adGoodsOrder.getProcessInstanceId()));
        }
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "ship", method = RequestMethod.GET)
    public String shipForm(AdGoodsOrder adGoodsOrder) {
        for (AdGoodsOrderDetail adGoodsOrderDetail : adGoodsOrder.getAdGoodsOrderDetailList()) {
            adGoodsOrderDetail.getExtendMap().put("waitShipQty", adGoodsOrderDetail.getBillQty() - (adGoodsOrderDetail.getShippedQty()==null?0:adGoodsOrderDetail.getShippedQty()));
        }
        return ObjectMapperUtils.writeValueAsString(adGoodsOrder);
    }

    @RequestMapping(value = "ship", method = RequestMethod.POST)
    public RestResponse ship(AdGoodsOrder adGoodsOrder) {
        Boolean success = adGoodsOrderService.ship(adGoodsOrder);
        if (success) {
            expressOrderService.save(ExpressOrderTypeEnum.物料订单.name(), adGoodsOrder.getId(), adGoodsOrder.getExpressOrder().getExpressCodes(), adGoodsOrder.getExpressOrder().getExpressCompanyId());
        }
        return new RestResponse("发货成功");
    }

    @RequestMapping(value = "sign", method = RequestMethod.GET)
    public String sign(AdGoodsOrder adGoodsOrder) {
        return ObjectMapperUtils.writeValueAsString(adGoodsOrder);
    }

    @RequestMapping(value = "sign", method = RequestMethod.POST)
    public RestResponse sign(AdGoodsOrder adGoodsOrder, RedirectAttributes redirectAttributes) {
        adGoodsOrderService.sign(adGoodsOrder);
        return new RestResponse("签收成功");
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public RestResponse delete(AdGoodsOrder adGoodsOrder, RedirectAttributes redirectAttributes) {
        adGoodsOrderService.delete(adGoodsOrder);
        return new RestResponse("删除成功");
    }

    private List<String> getActionList(AdGoodsOrder adGoodsOrder) {
        List<String> actionList = Lists.newArrayList();
        boolean isFinished=AuditTypeEnum.PASS.getValue().equals(adGoodsOrder.getProcessStatus()) || AuditTypeEnum.NOT_PASS.getValue().equals(adGoodsOrder.getProcessStatus());
        for(String authoritie : SecurityUtils.getAuthorityList()){
            switch (authoritie){
                case "crm:adGoodsOrder:view" :
                    actionList.add(Const.ITEM_ACTION_DETAIL);
                    break;
                case "crm:adGoodsOrder:bill" :
                    if(AdGoodsOrderStatusEnum.待开单.name().equals(adGoodsOrder.getProcessStatus())){
                        actionList.add(Const.ITEM_ACTION_BILL);
                    }
                    break;
                case "crm:adGoodsOrder:delete" :
                    if (!isFinished&& AccountUtils.getAccount() != null&& adGoodsOrder.getCreatedBy().equals(AccountUtils.getAccountId())&&adGoodsOrder.getBillDate()==null) {
                        actionList.add(Const.ITEM_ACTION_DELETE);
                    }
                    break;
                case "crm:adGoodsOrder:edit" :
                    if(adGoodsOrder.getProcessStatus()!=null&&adGoodsOrder.getProcessStatus().indexOf("审核")>=0){
                        actionList.add(Const.ITEM_ACTION_AUDIT);
                    }
                    if (!isFinished&& AccountUtils.getAccount() != null&& adGoodsOrder.getCreatedBy().equals(AccountUtils.getAccountId())&&adGoodsOrder.getBillDate()==null) {
                        actionList.add(Const.ITEM_ACTION_EDIT);
                    }
                    break;
                case "crm:adGoodsOrder:ship" :
                    if(AdGoodsOrderStatusEnum.待发货.name().equals(adGoodsOrder.getProcessStatus())){
                        actionList.add(Const.ITEM_ACTION_SHIP);
                    }
                    break;
                case "crm:adGoodsOrder:sign" :
                    if(AdGoodsOrderStatusEnum.待签收.name().equals(adGoodsOrder.getProcessStatus())){
                        actionList.add(Const.ITEM_ACTION_SIGN_IN);
                    }
                    break;
                default : break;
            }
        }
        return actionList;
    }
}
