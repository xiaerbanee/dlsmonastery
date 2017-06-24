package net.myspring.cloud.modules.sys.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.enums.VoucherFlexEnum;
import net.myspring.cloud.common.enums.VoucherStatusEnum;
import net.myspring.cloud.modules.kingdee.domain.*;
import net.myspring.cloud.modules.kingdee.service.*;
import net.myspring.cloud.modules.sys.dto.VoucherDto;
import net.myspring.cloud.modules.sys.dto.VoucherModel;
import net.myspring.cloud.modules.sys.service.VoucherService;
import net.myspring.cloud.modules.sys.web.form.VoucherForm;
import net.myspring.cloud.modules.sys.web.query.VoucherQuery;
import net.myspring.common.response.RestResponse;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
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
        VoucherModel voucherModel = getVoucherModel();
        map.put("headerList", voucherService.getHeaders(voucherModel.getBdFlexItemGroupList()));
        map.put("accountNameToFlexGroupNamesMap",voucherService.accountNameToFlexGroupNamesMap(voucherModel.getBdAccountList(),voucherModel.getBdFlexItemGroupList()));
        map.put("accountNameList",bdAccountService.findAll().stream().map(BdAccount::getFName).collect(Collectors.toList()));
        if (voucherForm.getId() != null){
            VoucherDto voucherDto = BeanUtil.map(voucherForm,VoucherDto.class);

            map.put("data",voucherService.initData(voucherDto,voucherModel));
        }else {
            map.put("data", Lists.newArrayList());
        }
        Map<String, Map<String, String>> result = voucherModel.getResult();
        for (Map<String, String> list : result.values()) {
            if (result.get(VoucherFlexEnum.供应商.name()).equals(list)){
                map.put("supplierNameList",list.keySet());
            }else if(result.get(VoucherFlexEnum.部门.name()).equals(list)){
                map.put("departmentNameList",list.keySet());
            }else if(result.get(VoucherFlexEnum.客户.name()).equals(list)){
                map.put("customerNameList",list.keySet());
            }else if(result.get(VoucherFlexEnum.其他类.name()).equals(list)){
                map.put("otherTypeNameList",list.keySet());
            }else if(result.get(VoucherFlexEnum.费用类.name()).equals(list)){
                map.put("expenseTypeNameList",list.keySet());
            }else if(result.get(VoucherFlexEnum.员工.name()).equals(list)){
                map.put("empInfoNameList",list.keySet());
            }else if(result.get(VoucherFlexEnum.银行账号.name()).equals(list)){
                map.put("bankAcntNameList",list.keySet());
            }
        }
        voucherForm.setExtra(map);
        return voucherForm;
    }

    @RequestMapping(value = "save")
    public RestResponse save(VoucherForm voucherForm) {
        VoucherModel voucherModel = getVoucherModel();
        return voucherService.save(voucherForm,voucherModel);
    }

    public VoucherModel getVoucherModel(){
        VoucherModel model = new VoucherModel();
        model.setBdAccountList(bdAccountService.findAll());
        model.setBdFlexItemGroupList(bdFlexItemGroupService.findAll());
        model.setBdFlexItemPropertyList(bdFlexItemPropertyService.findAll());
        Map<String, Map<String, String>> result=Maps.newHashMap();
        result.put(VoucherFlexEnum.供应商.name(), bdSupplierService.findAll().stream().collect(Collectors.toMap(BdSupplier::getFName,BdSupplier::getFNumber)));
        result.put(VoucherFlexEnum.部门.name(), bdDepartmentService.findAll().stream().collect(Collectors.toMap(BdDepartment::getFFullName,BdDepartment::getFNumber)));
        result.put(VoucherFlexEnum.客户.name(), bdCustomerService.findAll().stream().collect(Collectors.toMap(BdCustomer::getFName,BdCustomer::getFNumber)));
        result.put(VoucherFlexEnum.其他类.name(), basAssistantService.findByType("其他类").stream().collect(Collectors.toMap(BasAssistant::getFDataValue,BasAssistant::getFNumber)));
        result.put(VoucherFlexEnum.费用类.name(), basAssistantService.findByType("费用类").stream().collect(Collectors.toMap(BasAssistant::getFDataValue,BasAssistant::getFNumber)));
        result.put(VoucherFlexEnum.员工.name(), hrEmpInfoService.findAll().stream().collect(Collectors.toMap(HrEmpInfo::getFName,HrEmpInfo::getFNumber)));
        result.put(VoucherFlexEnum.银行账号.name(), cnBankAcntService.findAll().stream().collect(Collectors.toMap(CnBankAcnt::getFName,CnBankAcnt::getFNumber)));
        model.setResult(result);
        return model;
    }
}
