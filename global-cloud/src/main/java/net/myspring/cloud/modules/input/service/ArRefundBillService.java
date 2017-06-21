package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.common.enums.KingdeeNameEnum;
import net.myspring.cloud.common.utils.HandsontableUtils;
import net.myspring.cloud.modules.input.dto.ApPayBillDto;
import net.myspring.cloud.modules.input.dto.ArRefundBillDto;
import net.myspring.cloud.modules.input.dto.ArRefundBillEntityDto;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.input.web.form.ArRefundBillForm;
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
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 收款退款单
 * Created by lihx on 2017/6/20.
 */
@Service
@KingdeeDataSource
public class ArRefundBillService {
    @Autowired
    private BdCustomerRepository bdCustomerRepository;
    @Autowired
    private CnBankAcntRepository cnBankAcntRepository;
    @Autowired
    private BdDepartmentRepository bdDepartmentRepository;
    @Autowired
    private BdSettleTypeRepository bdSettleTypeRepository;
    @Autowired
    private KingdeeManager kingdeeManager;

    private KingdeeSynDto save(ArRefundBillDto arRefundBillDto, KingdeeBook kingdeeBook){
        KingdeeSynDto kingdeeSynDto = new KingdeeSynDto(
                arRefundBillDto.getExtendId(),
                arRefundBillDto.getExtendType(),
                KingdeeFormIdEnum.AR_REFUNDBILL.name(),
                arRefundBillDto.getJson(),
                kingdeeBook) {
        };
        kingdeeManager.save(kingdeeSynDto);
        return kingdeeSynDto;
    }

    public List<KingdeeSynDto> save(ArRefundBillForm arRefundBillForm, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook) {
        String json = HtmlUtils.htmlUnescape(arRefundBillForm.getJson());
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
        Map<String, ArRefundBillDto> refundBillForDHMap = Maps.newLinkedHashMap();
        Map<String, ArRefundBillDto> refundBillForCashMap = Maps.newLinkedHashMap();
        for (List<Object> row : data) {
            String customerName = HandsontableUtils.getValue(row,0);
            String bankAcntName = HandsontableUtils.getValue(row, 1);
            LocalDate billDate = LocalDateUtils.parse(HandsontableUtils.getValue(row, 2), "yyyy-MM-dd");
            String amountStr = HandsontableUtils.getValue(row, 3);
            BigDecimal amount = StringUtils.isEmpty(amountStr) ? BigDecimal.ZERO : new BigDecimal(amountStr);
            String settleType = HandsontableUtils.getValue(row, 4);
            String remarks = HandsontableUtils.getValue(row, 5);
            String billKey = "";
            if ("电汇".equals(settleType)) {
                billKey = customerName + CharConstant.COMMA + bankAcntName + CharConstant.COMMA + billDate + CharConstant.COMMA + amount + CharConstant.COMMA + remarks;
                if (!refundBillForDHMap.containsKey(billKey)) {
                    ArRefundBillDto arRefundBill = new ArRefundBillDto();
                    arRefundBill.setCreatorK3(accountKingdeeBook.getUsername());
                    arRefundBill.setKingdeeName(kingdeeBook.getName());
                    arRefundBill.setCustomerNumber(customerNameMap.get(customerName));
                    arRefundBill.setDate(billDate);
                    arRefundBill.setAmount(amount);
                    arRefundBill.setDepartmentNumber(customerNameToDepartmentNumberMap.get(customerName));
                    ArRefundBillEntityDto arRefundBillEntityDto = new ArRefundBillEntityDto();
                    arRefundBillEntityDto.setSettleTypeNumber("JSFS04_SYS");//电汇
                    if (StringUtils.isNotBlank(bankAcntName)){
                        arRefundBillEntityDto.setBankAcntNumber(bankAcntNameMap.get(bankAcntName));
                    }
                    if (KingdeeNameEnum.WZOPPO.name().equals(kingdeeBook.getName())) {
                        arRefundBillEntityDto.setAccountNumber("1002");//银行存款
                    }
                    arRefundBillEntityDto.setNote(remarks);
                    arRefundBill.setArRefundBillEntityDtoList(Lists.newArrayList(arRefundBillEntityDto));
                    refundBillForDHMap.put(billKey, arRefundBill);
                } else {
                    refundBillForDHMap.get(billKey).setAmount(amount.add(amount));
                }
            } else {
                billKey = customerName + CharConstant.COMMA + billDate + CharConstant.COMMA + amount + CharConstant.COMMA + remarks;
                if (!refundBillForCashMap.containsKey(billKey)) {
                    ArRefundBillDto arRefundBill = new ArRefundBillDto();
                    arRefundBill.setCreatorK3(accountKingdeeBook.getUsername());
                    arRefundBill.setKingdeeName(kingdeeBook.getName());
                    arRefundBill.setCustomerNumber(customerNameMap.get(customerName));
                    arRefundBill.setDate(billDate);
                    arRefundBill.setAmount(amount);
                    arRefundBill.setDepartmentNumber(customerNameToDepartmentNumberMap.get(customerName));
                    ArRefundBillEntityDto arRefundBillEntityDto = new ArRefundBillEntityDto();
                    arRefundBillEntityDto.setNote(remarks);
                    arRefundBillEntityDto.setSettleTypeNumber("JSFS01_SYS");//现金
                    arRefundBillEntityDto.setAccountNumber("1001");//库存现金
                    arRefundBill.setArRefundBillEntityDtoList(Lists.newArrayList(arRefundBillEntityDto));
                    refundBillForCashMap.put(billKey, arRefundBill);
                } else {
                    refundBillForCashMap.get(billKey).setAmount(amount.add(amount));
                }
            }

        }
        List<ArRefundBillDto> billList = Lists.newArrayList(refundBillForDHMap.values());
        List<KingdeeSynDto> kingdeeSynDtoList = save(billList,kingdeeBook,accountKingdeeBook);
        List<ArRefundBillDto> cashBillList = Lists.newArrayList(refundBillForCashMap.values());
        kingdeeSynDtoList.addAll(save(cashBillList,kingdeeBook,accountKingdeeBook));
        return kingdeeSynDtoList;
    }

    public List<KingdeeSynDto> save(List<ArRefundBillDto> arRefundBillDtoList, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        List<KingdeeSynDto> kingdeeSynDtoList = Lists.newArrayList();
        Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
        if(isLogin) {
            if (CollectionUtil.isNotEmpty(arRefundBillDtoList)) {
                for (ArRefundBillDto arRefundBillDto : arRefundBillDtoList) {
                    KingdeeSynDto kingdeeSynDto = save(arRefundBillDto,kingdeeBook);
                    kingdeeSynDtoList.add(kingdeeSynDto);
                }
            }
        }else{
            kingdeeSynDtoList.add(new KingdeeSynDto(false,"未登入金蝶系统"));
        }
        return kingdeeSynDtoList;
    }

    public ArRefundBillForm getForm(){
        Map<String,Object> map = Maps.newHashMap();
        ArRefundBillForm arRefundBillForm = new ArRefundBillForm();
        map.put("banAcntNameList",cnBankAcntRepository.findAll().stream().map(CnBankAcnt::getFName).collect(Collectors.toList()));
        map.put("customerNameList",bdCustomerRepository.findAll().stream().map(BdCustomer::getFName).collect(Collectors.toList()));
        map.put("settleTypeNameList",bdSettleTypeRepository.findAllForDefault().stream().map(BdSettleType::getFName).collect(Collectors.toList()));
        arRefundBillForm.setExtra(map);
        return arRefundBillForm;
    }
}
