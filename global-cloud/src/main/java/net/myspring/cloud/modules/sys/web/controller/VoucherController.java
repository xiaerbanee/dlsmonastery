package net.myspring.cloud.modules.sys.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.enums.VoucherStatusEnum;
import net.myspring.cloud.modules.kingdee.domain.*;
import net.myspring.cloud.modules.kingdee.service.*;
import net.myspring.cloud.modules.sys.dto.VoucherDto;
import net.myspring.cloud.modules.sys.service.VoucherService;
import net.myspring.cloud.modules.sys.web.form.VoucherForm;
import net.myspring.cloud.modules.sys.web.query.VoucherQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private BdFlexItemGroupService bdFlexItemGroupService;

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
        List<String> list = Lists.newLinkedList();
        list.add("摘要");
        list.add("科目名称");
        List<String> flexItemGroupList = bdFlexItemGroupService.getNameList();
        list.addAll(flexItemGroupList);
        list.add("借方金额");
        list.add("贷方金额");
        map.put("headerList", list);
        for (String name : flexItemGroupList){
            switch (name){
                case "供应商":  map.put("supplierNameList",bdSupplierService.findAll().stream().map(BdSupplier::getFName).collect(Collectors.toList()));
                case "客户":    map.put("customerNameList",bdCustomerService.findAll().stream().map(BdCustomer::getFName).collect(Collectors.toList()));
                case "银行账号":map.put("bankAcntNameList",cnBankAcntService.findAll().stream().map(CnBankAcnt::getFName).collect(Collectors.toList()));
                case "其他类":  map.put("otherTypeNameList",basAssistantService.findByType("其他类").stream().map(BasAssistant::getFDataValue).collect(Collectors.toList()));
                case "部门":    map.put("departmentNameList",bdDepartmentService.findAll().stream().map(BdDepartment::getFFullName).collect(Collectors.toList()));
                case "费用类":  map.put("expenseTypeNameList",basAssistantService.findByType("费用类").stream().map(BasAssistant::getFDataValue).collect(Collectors.toList()));
                case "员工":   map.put("empInfoNameList",hrEmpInfoService.findAll().stream().map(HrEmpInfo::getFName).collect(Collectors.toList()));
            }
        }
        map.put("accountNameList",bdAccountService.findAll().stream().map(BdAccount::getFNumber).collect(Collectors.toList()));
        voucherService.getForm(map);
        voucherForm.setExtra(map);
        return voucherForm;
    }
}
