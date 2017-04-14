package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.enums.AuditTypeEnum;
import net.myspring.future.common.enums.CloudSynExtendTypeEnum;
import net.myspring.future.common.enums.EmployeePhoneDepositStatusEnum;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.*;
import net.myspring.future.modules.api.service.K3cloudSynService;
import net.myspring.future.modules.crm.domain.BankIn;
import net.myspring.future.modules.crm.service.BankInService;
import net.myspring.future.modules.crm.service.BankService;
import net.myspring.future.modules.sys.domain.ProcessType;
import net.myspring.future.modules.sys.service.ProcessFlowService;
import net.myspring.future.modules.sys.service.ProcessTypeService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/bankIn")
public class BankInController {

    @Autowired
    private BankInService bankInService;
    @Autowired
    private BankService bankService;
    @Autowired
    private ProcessTypeService processTypeService;
    @Autowired
    private K3cloudSynService k3cloudSynService;
    @Autowired
    private ProcessFlowService processFlowService;

    @ModelAttribute
    public BankIn get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new BankIn() : bankInService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<BankIn> page = bankInService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(BankIn bankIn: page.getContent()){
            bankIn.setActionList(getActionList(bankIn));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty(){
        Map<String,Object> map = Maps.newHashMap();
        map.put("banks",bankService.findByAccountId(ThreadLocalContext.get().getAccount().getId()));
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "getListProperty")
    public String getListProperty(){
        Map<String,Object> map = Maps.newHashMap();
        map.put("processStatus", EmployeePhoneDepositStatusEnum.values());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "save")
    public String save(BankIn bankIn){
        ProcessType processType = processTypeService.findByName(BankIn.class.getSimpleName());
        bankIn.setProcessType(processType);
        bankIn.setProcessTypeId(processType.getId());
        bankInService.save(bankIn);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }

    @RequestMapping(value = "delete")
    public String delete(BankIn bankIn, BindingResult bindingResult) {
        if(!AuditTypeEnum.PASS.getValue().equals(bankIn.getProcessStatus())) {
            bankInService.delete(bankIn);
            return ObjectMapperUtils.writeValueAsString(new RestResponse("删除成功"));
        } else {
            return ObjectMapperUtils.writeValueAsString(new RestResponse("删除失败"));
        }
    }

    @RequestMapping(value = "audit")
    public String audit(BankIn bankIn){
         bankInService.audit(bankIn);
        if(bankIn.getSyn()){
            //同步到金蝶
            k3cloudSynService.syn(bankIn.getId(), CloudSynExtendTypeEnum.销售收款.name(),BankIn.class);
        }
        return ObjectMapperUtils.writeValueAsString(new RestResponse("审核成功"));
    }

    @RequestMapping(value = "batchAudit")
    public String audit(@RequestParam(value = "ids[]") String[] ids,Boolean pass){
        if(ids != null && ids.length > 0){
            for(String id :ids){
                BankIn bankIn = bankInService.findOne(id);
                bankIn.setPass(pass);
                bankInService.audit(bankIn);
                if(bankIn.getSyn()){
                    //同步到金蝶
                    k3cloudSynService.syn(bankIn.getId(), CloudSynExtendTypeEnum.销售收款.name(),BankIn.class);
                }
            }
        }
        RestResponse restResponse =new RestResponse("审核成功");
        String response = ObjectMapperUtils.writeValueAsString(restResponse);
        return response;
    }

    @RequestMapping(value = "findOne")
    public String findOne(BankIn bankIn){
        return ObjectMapperUtils.writeValueAsString(bankIn);
    }

    private List<String> getActionList(BankIn bankIn) {
        List<String> actionList = Lists.newArrayList();
        if(SecurityUtils.getAuthorityList().contains("crm:bankIn:view")){
            actionList.add(Const.ITEM_ACTION_DETAIL);
        }
        if("省公司审核".equals(bankIn.getProcessStatus()) && SecurityUtils.getAuthorityList().contains("crm:bankIn:audit")){
            actionList.add(Const.ITEM_ACTION_AUDIT);
        }
        if(!"已通过".equals(bankIn.getProcessStatus()) && SecurityUtils.getAuthorityList().contains("crm:bankIn:edit")){
            actionList.add(Const.ITEM_ACTION_DELETE);
            actionList.add(Const.ITEM_ACTION_EDIT);
        }
        return actionList;
    }


}
