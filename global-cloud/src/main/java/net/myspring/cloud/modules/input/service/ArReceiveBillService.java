package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.common.enums.SettleTypeEnum;
import net.myspring.common.utils.HandsontableUtils;
import net.myspring.cloud.modules.input.dto.ArReceiveBillDto;
import net.myspring.cloud.modules.input.dto.ArReceiveBillEntryDto;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.input.web.form.ArReceiveBillForm;
import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.kingdee.domain.BdDepartment;
import net.myspring.cloud.modules.kingdee.domain.BdSettleType;
import net.myspring.cloud.modules.kingdee.domain.CnBankAcnt;
import net.myspring.cloud.modules.kingdee.repository.BdCustomerRepository;
import net.myspring.cloud.modules.kingdee.repository.BdDepartmentRepository;
import net.myspring.cloud.modules.kingdee.repository.BdSettleTypeRepository;
import net.myspring.cloud.modules.kingdee.repository.CnBankAcntRepository;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.exception.ServiceException;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
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
 * 收款单
 * Created by lihx on 2017/6/24.
 */
@Service
@KingdeeDataSource
@Transactional(readOnly = true)
public class ArReceiveBillService {
    @Autowired
    private KingdeeManager kingdeeManager;
    @Autowired
    private BdCustomerRepository bdCustomerRepository;
    @Autowired
    private BdDepartmentRepository bdDepartmentRepository;
    @Autowired
    private CnBankAcntRepository cnBankAcntRepository;
    @Autowired
    private BdSettleTypeRepository bdSettleTypeRepository;

    private KingdeeSynDto save(ArReceiveBillDto arReceiveBillDto, KingdeeBook kingdeeBook){
        KingdeeSynDto kingdeeSynDto = new KingdeeSynDto(
                arReceiveBillDto.getExtendId(),
                arReceiveBillDto.getExtendType(),
                KingdeeFormIdEnum.AR_RECEIVEBILL.name(),
                arReceiveBillDto.getJson(),
                kingdeeBook);
        kingdeeSynDto = kingdeeManager.save(kingdeeSynDto);
        if (!kingdeeSynDto.getSuccess()){
            throw new ServiceException("收款单失败："+kingdeeSynDto.getResult());
        }
        return kingdeeSynDto;
    }

    @Transactional
    public List<KingdeeSynDto> save(List<ArReceiveBillDto> arReceiveBillDtoList, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        List<KingdeeSynDto> kingdeeSynDtoList = Lists.newArrayList();
        Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
        if(isLogin) {
            if (CollectionUtil.isNotEmpty(arReceiveBillDtoList)) {
                for (ArReceiveBillDto arReceiveBillDto : arReceiveBillDtoList) {
                    KingdeeSynDto kingdeeSynDto = save(arReceiveBillDto,kingdeeBook);
                    kingdeeSynDtoList.add(kingdeeSynDto);
                }
            }
        }else{
            throw new ServiceException("登入金蝶系统失败，请检查您的账户密码是否正确");
        }
        return kingdeeSynDtoList;
    }

    @Transactional
    public List<KingdeeSynDto> save(ArReceiveBillForm arReceiveBillForm, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook) {
        String json = HtmlUtils.htmlUnescape(arReceiveBillForm.getJson());
        List<List<Object>> data = ObjectMapperUtils.readValue(json, ArrayList.class);

        List<String> bankAcntNameList = Lists.newArrayList();
        List<String> customerNameList = Lists.newArrayList();
        for (List<Object> row : data){
            customerNameList.add(HandsontableUtils.getValue(row, 0));
            bankAcntNameList.add(HandsontableUtils.getValue(row,1));
        }
        Map<String, String> bankAcntNameMap = cnBankAcntRepository.findByNameList(bankAcntNameList).stream().collect(Collectors.toMap(CnBankAcnt::getFName,CnBankAcnt::getFNumber));
        List<BdCustomer> customerList = bdCustomerRepository.findByNameList(customerNameList);
        Map<String, String> customerNameMap = Maps.newHashMap();
        List<String> customerSalDeptIdList = Lists.newArrayList();
        for (BdCustomer customer : customerList){
            customerNameMap.put(customer.getFName(),customer.getFNumber());
            customerSalDeptIdList.add(customer.getFSalDeptId());
        }
        Map<String,String> customerNameToDepartmentNumberMap = Maps.newHashMap();
        List<BdDepartment> departmentList = bdDepartmentRepository.findBySalDeptIdList(customerSalDeptIdList);
        for (BdDepartment department: departmentList){
            for (BdCustomer customer : customerList){
                if(customer.getFSalDeptId().equals(department.getFDeptId())){
                    customerNameToDepartmentNumberMap.put(customer.getFName(),department.getFNumber());
                }
            }
        }
        Map<String, ArReceiveBillDto> refundBillForBankMap = Maps.newLinkedHashMap();
        Map<String, ArReceiveBillDto> refundBillForCashMap = Maps.newLinkedHashMap();
        for (List<Object> row : data) {
            String customerName = HandsontableUtils.getValue(row,0);
            String bankAcntName = HandsontableUtils.getValue(row, 1);
            LocalDate billDate = LocalDateUtils.parse(HandsontableUtils.getValue(row, 2), "yyyy-MM-dd");
            String amountStr = HandsontableUtils.getValue(row, 3);
            BigDecimal amount = StringUtils.isEmpty(amountStr) ? BigDecimal.ZERO : new BigDecimal(amountStr);
            String settleType = HandsontableUtils.getValue(row, 4);
            String remarks = HandsontableUtils.getValue(row, 5);
            String billKey = "";
            if (SettleTypeEnum.电汇.name().equals(settleType)) {
                billKey = customerName + CharConstant.COMMA + bankAcntName + CharConstant.COMMA + billDate + CharConstant.COMMA + remarks;
                if (!refundBillForBankMap.containsKey(billKey)) {
                    ArReceiveBillDto arRefundBill = new ArReceiveBillDto();
                    arRefundBill.setExtendType(ExtendTypeEnum.收款单_k3.name());
                    arRefundBill.setCreator(accountKingdeeBook.getUsername());
                    arRefundBill.setCustomerNumber(customerNameMap.get(customerName));
                    arRefundBill.setDate(billDate);
                    arRefundBill.setDepartmentNumber(customerNameToDepartmentNumberMap.get(customerName));
                    ArReceiveBillEntryDto arRefundBillEntityDto = new ArReceiveBillEntryDto();
                    arRefundBillEntityDto.setAmount(amount);
                    arRefundBillEntityDto.setFSettleTypeIdNumber(SettleTypeEnum.电汇.getFNumber());//电汇
                    if (StringUtils.isNotBlank(bankAcntName)){
                        arRefundBillEntityDto.setBankAcntNumber(bankAcntNameMap.get(bankAcntName));
                    }
                    arRefundBill.setArReceiveBillEntryDtoList(Lists.newArrayList(arRefundBillEntityDto));
                    refundBillForBankMap.put(billKey, arRefundBill);
                }
            } else {
                billKey = customerName + CharConstant.COMMA + billDate + CharConstant.COMMA + remarks;
                if (!refundBillForCashMap.containsKey(billKey)) {
                    ArReceiveBillDto arRefundBill = new ArReceiveBillDto();
                    arRefundBill.setExtendType(ExtendTypeEnum.收款单_k3.name());
                    arRefundBill.setCreator(accountKingdeeBook.getUsername());
                    arRefundBill.setCustomerNumber(customerNameMap.get(customerName));
                    arRefundBill.setDate(billDate);
                    arRefundBill.setDepartmentNumber(customerNameToDepartmentNumberMap.get(customerName));
                    ArReceiveBillEntryDto arRefundBillEntityDto = new ArReceiveBillEntryDto();
                    arRefundBillEntityDto.setAmount(amount);
                    arRefundBillEntityDto.setFSettleTypeIdNumber(SettleTypeEnum.现金.getFNumber());//现金
                    arRefundBill.setArReceiveBillEntryDtoList(Lists.newArrayList(arRefundBillEntityDto));
                    refundBillForCashMap.put(billKey, arRefundBill);
                }
            }
        }
        List<ArReceiveBillDto> billList = Lists.newArrayList(refundBillForBankMap.values());
        List<KingdeeSynDto> kingdeeSynDtoList = save(billList,kingdeeBook,accountKingdeeBook);
        List<ArReceiveBillDto> cashBillList = Lists.newArrayList(refundBillForCashMap.values());
        kingdeeSynDtoList.addAll(save(cashBillList,kingdeeBook,accountKingdeeBook));
        return kingdeeSynDtoList;
    }


    @Transactional
    public List<KingdeeSynDto> saveForWS(List<ArReceiveBillDto> arReceiveBillDtoList, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        if (arReceiveBillDtoList.size() > 0) {
            for (ArReceiveBillDto arReceiveBillDto : arReceiveBillDtoList) {
                arReceiveBillDto.setCreator(accountKingdeeBook.getUsername());
                String customerNumber = arReceiveBillDto.getCustomerNumber();
                BdCustomer bdCustomer = bdCustomerRepository.findByNumber(customerNumber);
                BdDepartment bdDepartment = bdDepartmentRepository.findByCustId(bdCustomer.getFCustId());
                arReceiveBillDto.setDepartmentNumber(bdDepartment.getFNumber());
            }
        }
        return save(arReceiveBillDtoList,kingdeeBook,accountKingdeeBook);
    }

    public ArReceiveBillForm getForm(){
        Map<String,Object> map = Maps.newHashMap();
        ArReceiveBillForm arRefundBillForm = new ArReceiveBillForm();
        map.put("banAcntNameList",cnBankAcntRepository.findAll().stream().map(CnBankAcnt::getFName).collect(Collectors.toList()));
        map.put("customerNameList",bdCustomerRepository.findAll().stream().map(BdCustomer::getFName).collect(Collectors.toList()));
        map.put("settleTypeNameList",bdSettleTypeRepository.findAllForDefault().stream().map(BdSettleType::getFName).collect(Collectors.toList()));
        arRefundBillForm.setExtra(map);
        return arRefundBillForm;
    }
}
