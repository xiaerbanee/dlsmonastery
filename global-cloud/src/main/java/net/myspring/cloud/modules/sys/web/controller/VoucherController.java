package net.myspring.cloud.modules.sys.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.enums.VoucherFlexEnum;
import net.myspring.cloud.common.enums.VoucherStatusEnum;
import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.service.GlVoucherService;
import net.myspring.cloud.modules.kingdee.domain.*;
import net.myspring.cloud.modules.kingdee.service.*;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.domain.Voucher;
import net.myspring.cloud.modules.sys.dto.VoucherDto;
import net.myspring.cloud.modules.sys.service.AccountKingdeeBookService;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.cloud.modules.sys.service.VoucherService;
import net.myspring.cloud.modules.sys.web.form.VoucherForm;
import net.myspring.cloud.modules.sys.web.query.VoucherQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    private GlVoucherService glVoucherService;
    @Autowired
    private KingdeeBookService kingdeeBookService;
    @Autowired
    private AccountKingdeeBookService accountKingdeeBookService;
    @Autowired
    private BdFlexItemGroupService bdFlexItemGroupService;
    @Autowired
    private BdFlexItemPropertyService bdFlexItemPropertyService;

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
        voucherForm.setBdAccountList(bdAccountService.findAll());
        voucherForm.setBdFlexItemGroupList(bdFlexItemGroupService.findAll());
        voucherForm.setBdFlexItemPropertyList(bdFlexItemPropertyService.findAll());
        List<String> flexItemNameList = voucherService.getHeaders(voucherForm.getBdFlexItemGroupList());
        map.put("headerList", flexItemNameList);
        map.put("accountNumberNameToFlexGroupNamesMap",voucherService.accountNumberNameToFlexGroupNamesMap(voucherForm.getBdAccountList(),voucherForm.getBdFlexItemGroupList()));
        List<String> accountNumberNameList = Lists.newArrayList();
        for (BdAccount bdAccount :  bdAccountService.findAll()){
            accountNumberNameList.add(bdAccount.getFNumber()+CharConstant.SLASH_LINE+bdAccount.getFName());
        }
        map.put("accountNumberNameList",accountNumberNameList);
        if (voucherForm.getId() != null){
            map.put("data",voucherService.initData(voucherForm));
        }else {
            map.put("data", Lists.newArrayList());
        }
        for (String flexItemName : flexItemNameList) {
            if (VoucherFlexEnum.供应商.name().equals(flexItemName)){
                List<String> supplierNumberNameList = Lists.newArrayList();
                for (BdSupplier bdSupplier :  bdSupplierService.findAll()){
                    supplierNumberNameList.add(bdSupplier.getFNumber()+CharConstant.SLASH_LINE+bdSupplier.getFName());
                }
                map.put("supplierNumberNameList",supplierNumberNameList);
            }else if(VoucherFlexEnum.部门.name().equals(flexItemName)){
                List<String> departmentNumberNameList = Lists.newArrayList();
                for (BdDepartment bdDepartment :  bdDepartmentService.findAll()){
                    departmentNumberNameList.add(bdDepartment.getFNumber()+CharConstant.SLASH_LINE+bdDepartment.getFFullName());
                }
                map.put("departmentNumberNameList",departmentNumberNameList);
            }else if(VoucherFlexEnum.客户.name().equals(flexItemName)){
                List<String> customerNumberNameList = Lists.newArrayList();
                for (BdCustomer bdCustomer :  bdCustomerService.findAll()){
                    customerNumberNameList.add(bdCustomer.getFNumber()+CharConstant.SLASH_LINE+bdCustomer.getFName());
                }
                map.put("customerNumberNameList",customerNumberNameList);
            }else if(VoucherFlexEnum.其他类.name().equals(flexItemName)){
                List<String> otherTypeNumberNameList = Lists.newArrayList();
                for (BasAssistant basAssistant :  basAssistantService.findByType("其他类")){
                    otherTypeNumberNameList.add(basAssistant.getFNumber()+CharConstant.SLASH_LINE+basAssistant.getFDataValue());
                }
                map.put("otherTypeNumberNameList",otherTypeNumberNameList);
            }else if(VoucherFlexEnum.费用类.name().equals(flexItemName)){
                List<String> expenseTypeNumberNameList = Lists.newArrayList();
                for (BasAssistant basAssistant :  basAssistantService.findByType("费用类")){
                    expenseTypeNumberNameList.add(basAssistant.getFNumber()+CharConstant.SLASH_LINE+basAssistant.getFDataValue());
                }
                map.put("expenseTypeNumberNameList",expenseTypeNumberNameList);
            }else if(VoucherFlexEnum.员工.name().equals(flexItemName)){
                List<String> empInfoNumberNameList = Lists.newArrayList();
                for (HrEmpInfo hrEmpInfo :  hrEmpInfoService.findAll()){
                    empInfoNumberNameList.add(hrEmpInfo.getFNumber()+CharConstant.SLASH_LINE+hrEmpInfo.getFName());
                }
                map.put("empInfoNumberNameList",empInfoNumberNameList);
            }else if(VoucherFlexEnum.银行账号.name().equals(flexItemName)){
                List<String> bankAcntNumberNameList = Lists.newArrayList();
                for (CnBankAcnt cnBankAcnt :  cnBankAcntService.findAll()){
                    bankAcntNumberNameList.add(cnBankAcnt.getFNumber()+CharConstant.SLASH_LINE+cnBankAcnt.getFName());
                }
                map.put("bankAcntNumberNameList",bankAcntNumberNameList);
            }
        }
        voucherForm.setExtra(map);
        return voucherForm;
    }

    @RequestMapping(value = "save")
    public RestResponse save(VoucherForm voucherForm) {
        voucherForm.setBdAccountList(bdAccountService.findAll());
        voucherForm.setBdFlexItemGroupList(bdFlexItemGroupService.findAll());
        voucherForm.setBdFlexItemPropertyList(bdFlexItemPropertyService.findAll());
        return voucherService.save(voucherForm);
    }

    @RequestMapping(value="findOne")
    public  VoucherDto findOne(String id){
        return voucherService.findOne(id);
    }

    @RequestMapping(value = "audit")
    public RestResponse audit(VoucherForm voucherForm){
        voucherForm.setBdAccountList(bdAccountService.findAll());
        voucherForm.setBdFlexItemGroupList(bdFlexItemGroupService.findAll());
        voucherForm.setBdFlexItemPropertyList(bdFlexItemPropertyService.findAll());
        Voucher voucher = voucherService.audit(voucherForm);
        RestResponse restResponse = new RestResponse("凭证审核成功",null,true);
        if (VoucherStatusEnum.已完成.name().equals(voucher.getStatus())) {
            KingdeeBook kingdeeBook = kingdeeBookService.findByCompanyName(RequestUtils.getCompanyName());
            AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountId(RequestUtils.getAccountId());
            KingdeeSynDto kingdeeSynDto = glVoucherService.save(voucherForm,kingdeeBook,accountKingdeeBook);
            String outCode ="序号："+kingdeeSynDto.getBillNo()+"  凭证号："+ glVoucherService.findByBillNo(kingdeeSynDto.getBillNo());
            voucher.setOutCode(outCode);
            voucherService.save(voucher);
            return new RestResponse("凭证同步成功",null,true);
        }
        return restResponse;
    }
}
