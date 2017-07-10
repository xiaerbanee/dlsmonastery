package net.myspring.future.modules.layout.web.controller;


import net.myspring.cloud.modules.kingdee.domain.BdDepartment;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.OutBillTypeEnum;
import net.myspring.future.common.enums.ShopDepositTypeEnum;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.service.BankService;
import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.layout.dto.ShopDepositDto;
import net.myspring.future.modules.layout.service.ShopDepositService;
import net.myspring.future.modules.layout.web.form.ShopDepositBatchDetailForm;
import net.myspring.future.modules.layout.web.form.ShopDepositBatchForm;
import net.myspring.future.modules.layout.web.form.ShopDepositForm;
import net.myspring.future.modules.layout.web.query.ShopDepositQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "layout/shopDeposit")
public class ShopDepositController {

    @Autowired
    private ShopDepositService shopDepositService;
    @Autowired
    private CloudClient cloudClient;
    @Autowired
    private DepotService depotService;
    @Autowired
    private BankService bankService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ShopDepositDto> list(Pageable pageable, ShopDepositQuery shopDepositQuery){
        return shopDepositService.findPage(pageable, shopDepositQuery);
    }

    @RequestMapping(value = "getQuery")
    public ShopDepositQuery getQuery(ShopDepositQuery shopDepositQuery) {
        shopDepositQuery.getExtra().put("typeList", ShopDepositTypeEnum.getList());
        return shopDepositQuery;
    }

    @RequestMapping(value = "getForm")
    public ShopDepositForm getForm(ShopDepositForm shopDepositForm) {
        shopDepositForm.getExtra().put("outBillTypeList",OutBillTypeEnum.getList());
        shopDepositForm.getExtra().put("departMentList", cloudClient.findAllDepartment());
        return shopDepositForm;

    }

    @RequestMapping(value = "getBatchForm")
    public ShopDepositBatchForm getBatchForm(ShopDepositBatchForm shopDepositBatchForm) {
        shopDepositBatchForm.getExtra().put("outBillTypeList",OutBillTypeEnum.getList());
        shopDepositBatchForm.getExtra().put("departMentList", cloudClient.findAllDepartment());
        return shopDepositBatchForm;
    }

    @RequestMapping(value = "findDto")
    public ShopDepositDto findDto(String id) {
        if(StringUtils.isBlank(id)){
            return new ShopDepositDto();
        }
        return shopDepositService.findDto(id);
    }

    @RequestMapping(value = "save")
    public RestResponse save(ShopDepositForm shopDepositForm) {
        shopDepositService.save(shopDepositForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "batchSave")
    public RestResponse batchSave(ShopDepositBatchForm shopDepositBatchForm) {

        if(CollectionUtil.isEmpty(shopDepositBatchForm.getShopDepositBatchDetailFormList())){
            throw new ServiceException("请录入需要添加的押金信息");
        }

        List<BdDepartment> bdDepartmentList = cloudClient.findAllDepartment();
        Map<String, BdDepartment> departmentMap = CollectionUtil.extractToMap(bdDepartmentList, "ffullName");

        Map<String, String> depotNameMap = depotService.findDepotNameMap(CollectionUtil.extractToList(shopDepositBatchForm.getShopDepositBatchDetailFormList(), "shopName"));
        Map<String, String> bankNameMap = bankService.findBankNameMap(CollectionUtil.extractToList(shopDepositBatchForm.getShopDepositBatchDetailFormList(), "bankName"));

        for(ShopDepositBatchDetailForm shopDepositBatchDetailForm : shopDepositBatchForm.getShopDepositBatchDetailFormList()){
            if(StringUtils.isBlank(shopDepositBatchDetailForm.getShopName())){
                throw new ServiceException("门店不可以为空");
            }
            if(StringUtils.isBlank(shopDepositBatchDetailForm.getBankName())){
                throw new ServiceException("银行不可以为空");
            }
            if(StringUtils.isBlank(shopDepositBatchDetailForm.getOutBillType())){
                throw new ServiceException("开单类型不可以为空");
            }
            if(!OutBillTypeEnum.getList().contains(shopDepositBatchDetailForm.getOutBillType())){
                throw new ServiceException("开单类型不合法");
            }
            if(shopDepositBatchDetailForm.getImageAmount() == null){
                throw new ServiceException("形象保证金不能为空");
            }
            if(shopDepositBatchDetailForm.getMarketAmount() == null){
                throw new ServiceException("市场保证金不能为空");
            }
            if(shopDepositBatchDetailForm.getDemoPhoneAmount() == null){
                throw new ServiceException("演示机押金不能为空");
            }
            if(shopDepositBatchDetailForm.getBillDate() == null){
                throw new ServiceException("开单日期不能为空");
            }
            if(StringUtils.isBlank(shopDepositBatchDetailForm.getDepartMentName())){
                throw new ServiceException("部门不可以为空");
            }
            if(!depotNameMap.containsKey(shopDepositBatchDetailForm.getShopName())){
                throw new ServiceException("门店："+shopDepositBatchDetailForm.getShopName()+"不存在");
            }
            if(!bankNameMap.containsKey(shopDepositBatchDetailForm.getBankName())){
                throw new ServiceException("银行："+shopDepositBatchDetailForm.getBankName()+"不存在");
            }
            if(!departmentMap.containsKey(shopDepositBatchDetailForm.getDepartMentName())){
                throw new ServiceException("部门："+shopDepositBatchDetailForm.getDepartMentName()+"不存在");
            }
        }

        StringBuilder errMsg =  new StringBuilder();

        for(int i = 0 ; i<shopDepositBatchForm.getShopDepositBatchDetailFormList().size(); i++){
            ShopDepositBatchDetailForm shopDepositBatchDetailForm= shopDepositBatchForm.getShopDepositBatchDetailFormList().get(i);
            try{
                ShopDepositForm shopDepositForm = new ShopDepositForm();
                shopDepositForm.setShopId(depotNameMap.get(shopDepositBatchDetailForm.getShopName()));
                shopDepositForm.setBankId(bankNameMap.get(shopDepositBatchDetailForm.getBankName()));
                shopDepositForm.setDepartMent(departmentMap.get(shopDepositBatchDetailForm.getDepartMentName()).getFNumber());
                shopDepositForm.setBillDate(shopDepositBatchDetailForm.getBillDate());
                shopDepositForm.setDemoPhoneAmount(shopDepositBatchDetailForm.getDemoPhoneAmount());
                shopDepositForm.setImageAmount(shopDepositBatchDetailForm.getImageAmount());
                shopDepositForm.setMarketAmount(shopDepositBatchDetailForm.getMarketAmount());
                shopDepositForm.setRemarks(shopDepositBatchDetailForm.getRemarks());

                save(shopDepositForm);
            }catch(Exception e){
                errMsg.append("第").append(i + 1).append("条记录保存失败，失败原因：").append(e.getMessage()).append("；");
                e.printStackTrace();
            }
        }

        if(StringUtils.isNotBlank(errMsg)){
            return new RestResponse("部分保存成功，成功的记录请勿重复提交。部分保存失败，失败明细为："+errMsg, ResponseCodeEnum.invalid.name(), false);
        }else{
            return new RestResponse("保存全部成功", ResponseCodeEnum.saved.name());
        }
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        shopDepositService.logicDelete(id);
        return new RestResponse("删除成功",ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "findLeftAmount")
    public BigDecimal findLeftAmount(String type, String depotId) {
        return shopDepositService.findLeftAmount(type, depotId);
    }

    @RequestMapping(value="exportLatest")
    public ModelAndView exportLatest() {
        return new ModelAndView(new ExcelView(), "simpleExcelBook", shopDepositService.exportLatest());
    }

}
