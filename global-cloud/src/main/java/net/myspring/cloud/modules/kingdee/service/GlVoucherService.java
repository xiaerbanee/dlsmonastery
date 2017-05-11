package net.myspring.cloud.modules.kingdee.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.sys.dto.BdFlexItemGroupDto;
import net.myspring.cloud.modules.kingdee.domain.*;
import net.myspring.cloud.modules.kingdee.mapper.*;
import net.myspring.cloud.modules.sys.dto.VoucherFormDto;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by lihx on 2017/4/11.
 */
@Service
@KingdeeDataSource
public class GlVoucherService {
    @Autowired
    private BdAccountMapper bdAccountMapper;
    @Autowired
    private BdFlexItemMapper bdFlexItemMapper;
    @Autowired
    private BdSupplierMapper bdSupplierMapper;
    @Autowired
    private BdDepartmentMapper bdDepartmentMapper;
    @Autowired
    private BdCustomerMapper bdCustomerMapper;
    @Autowired
    private BasAssistantMapper basAssistantMapper;
    @Autowired
    private HrEmpInfoMapper hrEmpInfoMapper;
    @Autowired
    private CnBankMapper cnBankMapper;

    public VoucherFormDto getGlVoucherDto(){
        VoucherFormDto voucherFormDto = new VoucherFormDto();
        voucherFormDto.setBdAccountList(bdAccountMapper.findAllSubject());
        List<BdFlexItemGroupDto> bdFlexItemGroupDtoList = BeanUtil.map(bdFlexItemMapper.findFlexItemGroup(),BdFlexItemGroupDto.class);
        voucherFormDto.setBdFlexItemGroupDtoList(bdFlexItemGroupDtoList);
        voucherFormDto.setBdFlexItemPropertyList(bdFlexItemMapper.findBdFlexItemProperty());

        Map<String, Map<String, String>> result= Maps.newHashMap();
        //待定
//        result.put(VoucherFormDto.SubjectGroup.供应商.name(), CollectionUtil.extractToMap(bdSupplierMapper.findAll(), "fName", "fNumber"));
//        result.put(VoucherFormDto.SubjectGroup.部门.name(), CollectionUtil.extractToMap(bdDepartmentMapper.findAll(), "fname", "fnumber"));
//        result.put(VoucherFormDto.SubjectGroup.客户.name(), CollectionUtil.extractToMap(bdCustomerMapper.findAll(null), "name", "code"));
//        result.put(VoucherFormDto.SubjectGroup.其他类.name(), CollectionUtil.extractToMap(basAssistantMapper.findByType("其他类"), "fname", "fnumber"));
//        result.put(VoucherFormDto.SubjectGroup.费用类.name(), CollectionUtil.extractToMap(basAssistantMapper.findByType( "费用类"), "fname", "fnumber"));
//        result.put(VoucherFormDto.SubjectGroup.员工.name(), CollectionUtil.extractToMap(hrEmpInfoMapper.findAllUser(), "fname", "fnumber"));
//        result.put(VoucherFormDto.SubjectGroup.银行账号.name(), CollectionUtil.extractToMap(cnBankMapper.findAll(null), "name", "code"));
        voucherFormDto.setResult(result);
        return voucherFormDto;
    }

    public Map<String,Object> getFormProperty(){
        Map<String,Object> map = Maps.newHashMap();
        List<String> subjects = Lists.newArrayList();
        List<String> expenseTypes = Lists.newArrayList();
        List<String> otherTypes = Lists.newArrayList();
        List<String> bankNames = Lists.newArrayList();
        List<String> customers = Lists.newArrayList();
        List<String> departments = Lists.newArrayList();
        List<String> userNames = Lists.newArrayList();
        List<String> supplierNames = Lists.newArrayList();
        for(CnBank bank: cnBankMapper.findAll()){
            bankNames.add(bank.getfNumber() + CharConstant.SLASH_LINE + bank.getfName());
        }
        for(HrEmpInfo user: hrEmpInfoMapper.findAllUser()){
            userNames.add(user.getfNumber() + CharConstant.SLASH_LINE + user.getfName());
        }
        for(BdDepartment department: bdDepartmentMapper.findAll()){
            departments.add(department.getfNumber() + CharConstant.SLASH_LINE + department.getfName());
        }
        for(BdAccount bdAccount: bdAccountMapper.findAllSubject()){
            subjects.add(bdAccount.getfNumber() + CharConstant.SLASH_LINE + bdAccount.getfName());
        }
        for(BdSupplier bdSupplier: bdSupplierMapper.findAll()){
            supplierNames.add(bdSupplier.getfNumber() + CharConstant.SLASH_LINE + bdSupplier.getfName());
        }
        for(BdCustomer bdCustomer: bdCustomerMapper.findAll()){
            customers.add(bdCustomer.getfNumber() + CharConstant.SLASH_LINE + bdCustomer.getfName());
        }
        for(BasAssistant basAssistant: basAssistantMapper.findByType("费用类")){
            expenseTypes.add(basAssistant.getfNumber() + CharConstant.SLASH_LINE + basAssistant.getfDataValue());
        }
        for(BasAssistant basAssistant: basAssistantMapper.findByType("其他类")){
            otherTypes.add(basAssistant.getfNumber() + CharConstant.SLASH_LINE + basAssistant.getfDataValue());
        }
        map.put("otherTypes", otherTypes);
        map.put("expenseTypes", expenseTypes);
        map.put("customers", customers);
        map.put("suppliers", supplierNames);
        map.put("subjects", subjects);
        map.put("departments", departments);
        map.put("banks", bankNames);
        map.put("users", userNames);
        return map;
    }
}
