package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.common.enums.VoucherFlexEnum;
import net.myspring.cloud.modules.input.dto.GlVoucherDto;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.kingdee.domain.*;
import net.myspring.cloud.modules.kingdee.repository.*;
import net.myspring.cloud.modules.sys.domain.*;
import net.myspring.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 凭证录入
 * Created by lihx on 2017/6/26.
 */
@Service
@KingdeeDataSource
@Transactional(readOnly = true)
public class GlVoucherService {
    @Autowired
    private KingdeeManager kingdeeManager;
    @Autowired
    private GlVoucherRepository glVoucherRepository;
    @Autowired
    private BdAccountRepository bdAccountRepository;
    @Autowired
    private BasAssistantRepository basAssistantRepository;
    @Autowired
    private BdCustomerRepository bdCustomerRepository;
    @Autowired
    private BdSupplierRepository bdSupplierRepository;
    @Autowired
    private BdDepartmentRepository bdDepartmentRepository;
    @Autowired
    private CnBankAcntRepository cnBankAcntRepository;
    @Autowired
    private HrEmpInfoRepository hrEmpInfoRepository;
    @Autowired
    private FaAssetTypeRepository faAssetTypeRepository;
    @Autowired
    private BdExpenseRepository bdExpenseRepository;

    public GlVoucher findByBillNo(String billNo){
        return glVoucherRepository.findByBillNo(billNo);
    }

    private KingdeeSynDto save(GlVoucherDto glVoucherDto, KingdeeBook kingdeeBook){
        KingdeeSynDto kingdeeSynDto = new KingdeeSynDto(
                glVoucherDto.getExtendId(),
                glVoucherDto.getExtendType(),
                KingdeeFormIdEnum.GL_VOUCHER.name(),
                glVoucherDto.getJson(),
                kingdeeBook);
        kingdeeSynDto = kingdeeManager.save(kingdeeSynDto);
        if (!kingdeeSynDto.getSuccess()){
            throw new ServiceException("凭证录入失败："+kingdeeSynDto.getResult());
        }
        return kingdeeSynDto;
    }

    @Transactional
    public KingdeeSynDto save(GlVoucherDto glVoucherDto, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        KingdeeSynDto kingdeeSynDto;
        Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
        if(isLogin) {
            kingdeeSynDto = save(glVoucherDto, kingdeeBook);
        }else{
            throw new ServiceException("登入金蝶系统失败，请检查您的账户密码是否正确");
        }
        return kingdeeSynDto;
    }

    //根据名称获取需要向金蝶接口插入的Number
    public Map<String, Map<String, String>> getFlexNameMap(List<String> flexItemNameList) {
        Map<String, Map<String, String>> result = Maps.newHashMap();
        result.put(VoucherFlexEnum.科目.name(), bdAccountRepository.findAll().stream().collect(Collectors.toMap(BdAccount::getFName,BdAccount::getFNumber)));
        for (String flexItemName : flexItemNameList) {
            if (VoucherFlexEnum.供应商.name().equals(flexItemName)){
                result.put(VoucherFlexEnum.供应商.name(), bdSupplierRepository.findAll().stream().collect(Collectors.toMap(BdSupplier::getFName,BdSupplier::getFNumber)));
            }else if(VoucherFlexEnum.部门.name().equals(flexItemName)){
                result.put(VoucherFlexEnum.部门.name(), bdDepartmentRepository.findAll().stream().collect(Collectors.toMap(BdDepartment::getFFullName,BdDepartment::getFNumber)));
            }else if(VoucherFlexEnum.客户.name().equals(flexItemName)){
                result.put(VoucherFlexEnum.客户.name(), bdCustomerRepository.findAll().stream().collect(Collectors.toMap(BdCustomer::getFName,BdCustomer::getFNumber)));
            }else if(VoucherFlexEnum.其他类.name().equals(flexItemName)){
                result.put(VoucherFlexEnum.其他类.name(), basAssistantRepository.findByType("其他类").stream().collect(Collectors.toMap(BasAssistant::getFDataValue,BasAssistant::getFNumber)));
            }else if(VoucherFlexEnum.费用类.name().equals(flexItemName)){
                result.put(VoucherFlexEnum.费用类.name(), basAssistantRepository.findByType("费用类").stream().collect(Collectors.toMap(BasAssistant::getFDataValue,BasAssistant::getFNumber)));
            }else if(VoucherFlexEnum.员工.name().equals(flexItemName)){
                result.put(VoucherFlexEnum.员工.name(), hrEmpInfoRepository.findAll().stream().collect(Collectors.toMap(HrEmpInfo::getFName,HrEmpInfo::getFNumber)));
            }else if(VoucherFlexEnum.银行账号.name().equals(flexItemName)){
                result.put(VoucherFlexEnum.银行账号.name(), cnBankAcntRepository.findAll().stream().collect(Collectors.toMap(CnBankAcnt::getFName,CnBankAcnt::getFNumber)));
            }else if(VoucherFlexEnum.资产类别.name().equals(flexItemName)){
                result.put(VoucherFlexEnum.资产类别.name(), faAssetTypeRepository.findAll().stream().collect(Collectors.toMap(FaAssetType::getFName,FaAssetType::getFNumber)));
            }else if(VoucherFlexEnum.费用项目.name().equals(flexItemName)){
                result.put(VoucherFlexEnum.费用项目.name(), bdExpenseRepository.findAll().stream().collect(Collectors.toMap(BdExpense::getFName,BdExpense::getFNumber)));
            }
        }
        return result;
    }

}
