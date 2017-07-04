package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.common.utils.HandsontableUtils;
import net.myspring.cloud.modules.input.dto.ApPayBillDto;
import net.myspring.cloud.modules.input.dto.ApPayBillEntryDto;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.input.web.form.ApPayBillForm;
import net.myspring.cloud.modules.kingdee.domain.*;
import net.myspring.cloud.modules.kingdee.repository.*;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.exception.ServiceException;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 付款单
 * Created by lihx on 2017/6/20.
 */
@Service
@KingdeeDataSource
@Transactional(readOnly = true)
public class ApPayBillService {
    @Autowired
    private BdDepartmentRepository bdDepartmentRepository;
    @Autowired
    private BdSupplierRepository bdSupplierRepository;
    @Autowired
    private BdSettleTypeRepository bdSettleTypeRepository;
    @Autowired
    private CnBankAcntRepository cnBankAcntRepository;
    @Autowired
    private BdAccountRepository bdAccountRepository;
    @Autowired
    private KingdeeManager kingdeeManager;

    @Transactional
    private KingdeeSynDto save(ApPayBillDto apPayBillDto, KingdeeBook kingdeeBook){
        KingdeeSynDto kingdeeSynDto = new KingdeeSynDto(
                apPayBillDto.getExtendId(),
                apPayBillDto.getExtendType(),
                KingdeeFormIdEnum.AP_PAYBILL.name(),
                apPayBillDto.getJson(),
                kingdeeBook) {
            };
        kingdeeManager.save(kingdeeSynDto);
        if (!kingdeeSynDto.getSuccess()){
            throw new ServiceException("付款单失败："+kingdeeSynDto.getResult());
        }
        return kingdeeSynDto;
    }

    @Transactional
    public List<KingdeeSynDto> save(ApPayBillForm apPayBillForm, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook) {
        LocalDate billDate = apPayBillForm.getBillDate();
        String json = HtmlUtils.htmlUnescape(apPayBillForm.getJson());
        List<List<Object>> data = ObjectMapperUtils.readValue(json, ArrayList.class);
        Map<String, ApPayBillDto> payBillMap = Maps.newLinkedHashMap();

        List<String> supplierNameList = Lists.newArrayList();
        List<String> departmentNameList = Lists.newArrayList();
        List<String> bankAcntNameList = Lists.newArrayList();
        List<String> settleTypeNameList = Lists.newArrayList();
        List<String> accountNameList = Lists.newArrayList();
        for (List<Object> row : data) {
            supplierNameList.add(HandsontableUtils.getValue(row, 0));
            departmentNameList.add(HandsontableUtils.getValue(row, 1));
            bankAcntNameList.add(HandsontableUtils.getValue(row, 2));
            settleTypeNameList.add(HandsontableUtils.getValue(row, 3));
            accountNameList.add(HandsontableUtils.getValue(row, 6));
        }
        Map<String,String> supplierNameMap = bdSupplierRepository.findByNameList(supplierNameList).stream().collect(Collectors.toMap(BdSupplier::getFName,BdSupplier::getFNumber));
        Map<String, String> accountNameMap = bdAccountRepository.findByNameList(accountNameList).stream().collect(Collectors.toMap(BdAccount::getFName,BdAccount::getFNumber));
        Map<String, String> departmentNameMap = bdDepartmentRepository.findByNameList(departmentNameList).stream().collect(Collectors.toMap(BdDepartment::getFFullName,BdDepartment::getFNumber));
        Map<String, String> settleTypeNameMap = bdSettleTypeRepository.findByNameList(settleTypeNameList).stream().collect(Collectors.toMap(BdSettleType::getFName,BdSettleType::getFNumber));
        Map<String, String> bankAcntNameMap = cnBankAcntRepository.findByNameList(bankAcntNameList).stream().collect(Collectors.toMap(CnBankAcnt::getFName,CnBankAcnt::getFNumber));
        for (List<Object> row : data) {
            String supplierName = HandsontableUtils.getValue(row, 0);
            String departmentName = HandsontableUtils.getValue(row, 1);
            String bankAcntName = HandsontableUtils.getValue(row, 2);
            String settleTypeName = HandsontableUtils.getValue(row, 3);
            String amountStr = HandsontableUtils.getValue(row, 4);
            BigDecimal amount = StringUtils.isEmpty(amountStr) ? BigDecimal.ZERO : new BigDecimal(amountStr);
            String note = HandsontableUtils.getValue(row, 5);
            String accountName = HandsontableUtils.getValue(row, 6);
            String billKey = supplierName + CharConstant.COMMA + departmentName + CharConstant.COMMA + bankAcntName + CharConstant.COMMA + settleTypeName + CharConstant.COMMA + amount + CharConstant.COMMA + note + CharConstant.COMMA + accountName;
            if (!payBillMap.containsKey(billKey)) {
                ApPayBillDto payBill = new ApPayBillDto();
                payBill.setExtendType(ExtendTypeEnum.付款单_K3.name());
                payBill.setCreatorK3(accountKingdeeBook.getUsername());
                payBill.setDate(billDate);
                payBill.setDepartmentNumber(departmentNameMap.get(departmentName));
                payBill.setSupplierNumber(supplierNameMap.get(supplierName));
                payBill.setAmount(amount);
                ApPayBillEntryDto entryDto = new ApPayBillEntryDto();
                if (StringUtils.isNotBlank(bankAcntName)) {
                    entryDto.setBankAcntNumber(bankAcntNameMap.get(bankAcntName));
                }
                entryDto.setSettleTypeNumber(settleTypeNameMap.get(settleTypeName));
                entryDto.setAccountNumber(accountNameMap.get(accountName));
                entryDto.setComment(note);
                payBill.setApPayBillEntryDtoList(Lists.newArrayList(entryDto));
                payBillMap.put(billKey, payBill);
            } else {
                payBillMap.get(billKey).setAmount(amount.add(amount));
            }
        }
        List<ApPayBillDto> billList = Lists.newArrayList(payBillMap.values());
        List<KingdeeSynDto> kingdeeSynDtoList = save(billList,kingdeeBook,accountKingdeeBook);
        return kingdeeSynDtoList;
    }

    @Transactional
    public KingdeeSynDto save(ApPayBillDto apPayBillDto, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        KingdeeSynDto kingdeeSynDto;
        Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
        if(isLogin) {
            kingdeeSynDto = save(apPayBillDto,kingdeeBook);
        }else{
            kingdeeSynDto = new KingdeeSynDto(false,"未登入金蝶系统");
        }
        return kingdeeSynDto;
    }

    @Transactional
    public List<KingdeeSynDto> save(List<ApPayBillDto> apPayBillDtoList, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        List<KingdeeSynDto> kingdeeSynDtoList = Lists.newArrayList();
        Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
        if(isLogin) {
            if (CollectionUtil.isNotEmpty(apPayBillDtoList)) {
                for (ApPayBillDto payBill : apPayBillDtoList) {
                    KingdeeSynDto kingdeeSynDto = save(payBill,kingdeeBook);
                    kingdeeSynDtoList.add(kingdeeSynDto);
                }
            }
        }else{
            kingdeeSynDtoList.add(new KingdeeSynDto(false,"未登入金蝶系统"));
        }
        return kingdeeSynDtoList;
    }

    public ApPayBillForm getForm(){
        ApPayBillForm payBillForm = new ApPayBillForm();
        Map<String,Object> map = Maps.newHashMap();
        map.put("supplierNameList",bdSupplierRepository.findAll().stream().map(BdSupplier::getFName).collect(Collectors.toList()));
        map.put("departmentNameList",bdDepartmentRepository.findAll().stream().map(BdDepartment::getFFullName).collect(Collectors.toList()));
        map.put("bankAcntNameList",cnBankAcntRepository.findAll().stream().map(CnBankAcnt::getFName).collect(Collectors.toList()));
        map.put("settleTypeNameList",bdSettleTypeRepository.findAll().stream().map(BdSettleType::getFName).collect(Collectors.toList()));
        map.put("accountNameList",bdAccountRepository.findAll().stream().map(BdAccount::getFName).collect(Collectors.toList()));
        payBillForm.setExtra(map);
        return payBillForm;
    }
}
