package net.myspring.cloud.modules.sys.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.enums.VoucherFlexEnum;
import net.myspring.cloud.common.enums.VoucherStatusEnum;
import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.kingdee.domain.*;
import net.myspring.cloud.modules.kingdee.service.*;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.dto.VoucherDto;
import net.myspring.cloud.modules.sys.service.AccountKingdeeBookService;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.cloud.modules.sys.service.VoucherService;
import net.myspring.cloud.modules.sys.web.form.VoucherForm;
import net.myspring.cloud.modules.sys.web.query.VoucherQuery;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * 凭证
 * Created by lihx on 2017/4/5.
 */
@RestController
@RequestMapping(value = "sys/voucher")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private BasAssistantService basAssistantService;
    @Autowired
    private BdCustomerService bdCustomerService;
    @Autowired
    private BdSupplierService bdSupplierService;
    @Autowired
    private BdAccountService bdAccountService;
    @Autowired
    private BdDepartmentService bdDepartmentService;
    @Autowired
    private CnBankAcntService cnBankAcntService;
    @Autowired
    private HrEmpInfoService hrEmpInfoService;
    @Autowired
    private KingdeeBookService kingdeeBookService;
    @Autowired
    private AccountKingdeeBookService accountKingdeeBookService;
    @Autowired
    private BdFlexItemGroupService bdFlexItemGroupService;
    @Autowired
    private BdFlexItemPropertyService bdFlexItemPropertyService;
    @Autowired
    private FaAssetTypeService faAssetTypeService;
    @Autowired
    private BdExpenseService bdExpenseService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<VoucherDto> page(Pageable pageable, VoucherQuery voucherQuery){
        Page<VoucherDto> page = voucherService.findPage(pageable,voucherQuery);
        return page;
    }

    @RequestMapping(value="getQuery")
    public  VoucherQuery getQuery(VoucherQuery voucherQuery){
        voucherQuery.getExtra().put("statusList", VoucherStatusEnum.values());
        return voucherQuery;
    }

    @RequestMapping(value = "form")
    public VoucherForm form (VoucherForm voucherForm) {
        Map<String,Object> map = Maps.newHashMap();
        VoucherDto voucherDto;
        if (StringUtils.isNotBlank(voucherForm.getId())){
            voucherDto = voucherService.findOne(voucherForm.getId());
        }else {
            voucherDto = new VoucherDto();
        }
        voucherDto.setBdAccountList(bdAccountService.findAll());
        voucherDto.setBdFlexItemGroupList(bdFlexItemGroupService.findAll());
        voucherDto.setBdFlexItemPropertyList(bdFlexItemPropertyService.findAll());
        List<String> flexItemNameList = voucherService.getHeaders(voucherDto.getBdFlexItemGroupList());
        map.put("headerList", flexItemNameList);
        map.put("accountNameToFlexGroupNamesMap",voucherService.accountNameToFlexGroupNamesMap(voucherDto.getBdAccountList(),voucherDto.getBdFlexItemGroupList()));
        List<String> accountNameList = Lists.newArrayList();
        for (BdAccount bdAccount :  bdAccountService.findAll()){
            accountNameList.add(bdAccount.getFName());
        }
        map.put("accountNameList",accountNameList);
        if (voucherDto.getId() != null){
            map.put("data",voucherService.initData(voucherDto));
        }else {
            map.put("data", Lists.newArrayList());
        }
        for (String flexItemName : flexItemNameList) {
            if (VoucherFlexEnum.供应商.name().equals(flexItemName)){
                List<String> supplierNameList = Lists.newArrayList();
                for (BdSupplier bdSupplier :  bdSupplierService.findAll()){
                    supplierNameList.add(bdSupplier.getFName());
                }
                map.put("supplierNameList",supplierNameList);
            }else if(VoucherFlexEnum.部门.name().equals(flexItemName)){
                List<String> departmentNameList = Lists.newArrayList();
                for (BdDepartment bdDepartment :  bdDepartmentService.findAll()){
                    departmentNameList.add(bdDepartment.getFFullName());
                }
                map.put("departmentNameList",departmentNameList);
            }else if(VoucherFlexEnum.客户.name().equals(flexItemName)){
                List<String> customerNameList = Lists.newArrayList();
                for (BdCustomer bdCustomer :  bdCustomerService.findAll()){
                    customerNameList.add(bdCustomer.getFName());
                }
                map.put("customerNameList",customerNameList);
            }else if(VoucherFlexEnum.其他类.name().equals(flexItemName)){
                List<String> otherTypeNameList = Lists.newArrayList();
                for (BasAssistant basAssistant :  basAssistantService.findByType("其他类")){
                    otherTypeNameList.add(basAssistant.getFDataValue());
                }
                map.put("otherTypeNameList",otherTypeNameList);
            }else if(VoucherFlexEnum.费用类.name().equals(flexItemName)){
                List<String> expenseTypeNameList = Lists.newArrayList();
                for (BasAssistant basAssistant :  basAssistantService.findByType("费用类")){
                    expenseTypeNameList.add(basAssistant.getFDataValue());
                }
                map.put("expenseTypeNameList",expenseTypeNameList);
            }else if(VoucherFlexEnum.员工.name().equals(flexItemName)){
                List<String> empInfoNameList = Lists.newArrayList();
                for (HrEmpInfo hrEmpInfo :  hrEmpInfoService.findAll()){
                    empInfoNameList.add(hrEmpInfo.getFName());
                }
                map.put("empInfoNameList",empInfoNameList);
            }else if(VoucherFlexEnum.银行账号.name().equals(flexItemName)){
                List<String> bankAcntNameList = Lists.newArrayList();
                for (CnBankAcnt cnBankAcnt :  cnBankAcntService.findAll()){
                    bankAcntNameList.add(cnBankAcnt.getFName());
                }
                map.put("bankAcntNameList",bankAcntNameList);
            }else if(VoucherFlexEnum.资产类别.name().equals(flexItemName)){
                List<String> assetTypeNameList = Lists.newArrayList();
                for (FaAssetType hrEmpInfo :  faAssetTypeService.findAll()){
                    assetTypeNameList.add(hrEmpInfo.getFName());
                }
                map.put("assetTypeNameList",assetTypeNameList);
            }else if(VoucherFlexEnum.费用项目.name().equals(flexItemName)){
                List<String> expenseNameList = Lists.newArrayList();
                for (BdExpense cnBankAcnt :  bdExpenseService.findAll()){
                    expenseNameList.add(cnBankAcnt.getFName());
                }
                map.put("expenseNameList",expenseNameList);
            }
        }
        voucherForm.setExtra(map);
        return voucherForm;
    }

    @RequestMapping(value = "detail")
    public Map<String,Object> detail (String id) {
        Map<String,Object> map = Maps.newHashMap();
        VoucherDto voucherDto = voucherService.findOne(id);
        if (voucherDto != null) {
            voucherDto.setBdAccountList(bdAccountService.findAll());
            voucherDto.setBdFlexItemGroupList(bdFlexItemGroupService.findAll());
            voucherDto.setBdFlexItemPropertyList(bdFlexItemPropertyService.findAll());
            List<String> flexItemNameList = voucherService.getHeaders(voucherDto.getBdFlexItemGroupList());
            map.put("headerList", flexItemNameList);
            map.put("data", voucherService.initData(voucherDto));
            return map;
        }
        return null;
    }

    @RequestMapping(value = "export")
    public ModelAndView export (String id) {
        VoucherDto voucherDto = voucherService.findOne(id);
        if (voucherDto != null) {
            voucherDto.setBdAccountList(bdAccountService.findAll());
            voucherDto.setBdFlexItemGroupList(bdFlexItemGroupService.findAll());
            voucherDto.setBdFlexItemPropertyList(bdFlexItemPropertyService.findAll());
            return new ModelAndView(new ExcelView(), "simpleExcelBook", voucherService.export(voucherDto));
        }
        return null;
    }

    @RequestMapping(value = "save")
    public RestResponse save(VoucherForm voucherForm) {
        List<BdAccount> bdAccountList = bdAccountService.findAll();
        List<BdFlexItemGroup> bdFlexItemGroupList = bdFlexItemGroupService.findAll();
        List<BdFlexItemProperty> bdFlexItemPropertyList = bdFlexItemPropertyService.findAll();
        RestResponse restResponse = voucherService.check(voucherForm,bdAccountList,bdFlexItemGroupList);
        if (!restResponse.getSuccess()) {
            return restResponse;
        }else{
            voucherService.save(voucherForm,bdFlexItemGroupList,bdFlexItemPropertyList);
            restResponse.setMessage("凭证保存成功");
            restResponse.setSuccess(true);
            return restResponse;
        }
    }

    @RequestMapping(value="findOne")
    public  VoucherDto findOne(String id){
        return voucherService.findOne(id);
    }

    @RequestMapping(value = "audit")
    public RestResponse audit(VoucherForm voucherForm){
        List<BdAccount> bdAccountList = bdAccountService.findAll();
        List<BdFlexItemGroup> bdFlexItemGroupList = bdFlexItemGroupService.findAll();
        List<BdFlexItemProperty> bdFlexItemPropertyList = bdFlexItemPropertyService.findAll();
        RestResponse restResponse = voucherService.check(voucherForm,bdAccountList,bdFlexItemGroupList);
        if (!restResponse.getSuccess()) {
            return restResponse;
        }else {
            try{
                KingdeeBook kingdeeBook = kingdeeBookService.findByCompanyName(RequestUtils.getCompanyName());
                AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountIdAndCompanyName(RequestUtils.getAccountId(),RequestUtils.getCompanyName());
                voucherService.audit(voucherForm, bdFlexItemGroupList, bdFlexItemPropertyList,accountKingdeeBook,kingdeeBook);
                return new RestResponse("凭证审核成功", null, true);
            }catch (Exception e){
                return new RestResponse(e.getMessage(), ResponseCodeEnum.invalid.name(), false);
            }
        }
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id){
        try {
            voucherService.delete(id);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
        return new RestResponse("删除成功",null,true);
    }


}
