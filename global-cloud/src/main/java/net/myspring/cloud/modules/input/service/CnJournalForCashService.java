package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.common.enums.KingdeeNameEnum;
import net.myspring.cloud.common.enums.KingdeeTypeEnum;
import net.myspring.cloud.common.utils.HandsontableUtils;
import net.myspring.cloud.modules.input.dto.CnJournalFEntityForCashDto;
import net.myspring.cloud.modules.input.dto.CnJournalForCashDto;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.input.web.form.CnJournalForCashForm;
import net.myspring.cloud.modules.kingdee.domain.*;
import net.myspring.cloud.modules.kingdee.repository.*;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
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
 * 手工日记账之现金日记账
 * Created by lihx on 2017/6/8.
 */
@Service
@KingdeeDataSource
public class CnJournalForCashService {
    @Autowired
    private BdCustomerRepository bdCustomerRepository;
    @Autowired
    private BdDepartmentRepository bdDepartmentRepository;
    @Autowired
    private HrEmpInfoRepository hrEmpInfoRepository;
    @Autowired
    private BasAssistantRepository basAssistantRepository;
    @Autowired
    private BdAccountRepository bdAccountRepository;
    @Autowired
    private KingdeeManager kingdeeManager;

    public KingdeeSynDto save(CnJournalForCashDto cnJournalForCashDto,KingdeeBook kingdeeBook) {
        KingdeeSynDto kingdeeSynDto = new KingdeeSynDto(
                KingdeeFormIdEnum.CN_JOURNAL.name(),
                cnJournalForCashDto.getJson(),
                kingdeeBook) {
        };
        kingdeeManager.save(kingdeeSynDto);
        return kingdeeSynDto;
    }

    public KingdeeSynDto save(CnJournalForCashForm cnJournalForCashForm, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook) {
        LocalDate billDate = cnJournalForCashForm.getBillDate();
        String json = HtmlUtils.htmlUnescape(cnJournalForCashForm.getJson());
        List<List<Object>> data = ObjectMapperUtils.readValue(json, ArrayList.class);
        List<String> empInfoNameList = Lists.newArrayList();
        List<String> departmentNameList = Lists.newArrayList();
        List<String> otherTypeNameList = Lists.newArrayList();
        List<String> expenseTypeNameList = Lists.newArrayList();
        List<String> customerNameForList = Lists.newArrayList();
        for (List<Object> row : data){
            empInfoNameList.add(HandsontableUtils.getValue(row, 5));
            departmentNameList.add(HandsontableUtils.getValue(row, 6));
            otherTypeNameList.add(HandsontableUtils.getValue(row, 7));
            expenseTypeNameList.add(HandsontableUtils.getValue(row, 8));
            if (row.size() > 9) {
                customerNameForList.add(HandsontableUtils.getValue(row, 9));
            }
        }
        Map<String, String> customerNameMap = Maps.newHashMap();
        Map<String, String> empInfoNameMap = hrEmpInfoRepository.findByNameList(empInfoNameList).stream().collect(Collectors.toMap(HrEmpInfo::getFName,HrEmpInfo::getFNumber));
        Map<String, String> departmentNameMap  = bdDepartmentRepository.findByNameList(departmentNameList).stream().collect(Collectors.toMap(BdDepartment::getFFullName,BdDepartment::getFNumber));
        Map<String, String> otherTypeNameMap = basAssistantRepository.findByNameList(otherTypeNameList).stream().collect(Collectors.toMap(BasAssistant::getFDataValue,BasAssistant::getFNumber));
        Map<String, String> expenseNameMap = basAssistantRepository.findByNameList(expenseTypeNameList).stream().collect(Collectors.toMap(BasAssistant::getFDataValue,BasAssistant::getFNumber));
        if (customerNameForList.size() > 0){
            customerNameMap = bdCustomerRepository.findByNameList(customerNameForList).stream().collect(Collectors.toMap(BdCustomer::getFName,BdCustomer::getFNumber));
        }
        CnJournalForCashDto cnJournalForCashDto = new CnJournalForCashDto();
        cnJournalForCashDto.setCreator(accountKingdeeBook.getUsername());
        cnJournalForCashDto.setDate(billDate);
        cnJournalForCashDto.setKingdeeBook(kingdeeBook);
        for (List<Object> row : data) {
            String accountNumber = HandsontableUtils.getValue(row, 0);
            String debitAmountStr =  HandsontableUtils.getValue(row, 1);
            BigDecimal debitAmount = StringUtils.isBlank(debitAmountStr) ? BigDecimal.ZERO : new BigDecimal(debitAmountStr);
            String creditAmountStr =  HandsontableUtils.getValue(row, 2);
            BigDecimal creditAmount = StringUtils.isBlank(creditAmountStr) ? BigDecimal.ZERO : new BigDecimal(creditAmountStr);
            String remarks =  HandsontableUtils.getValue(row, 3);
            String empInfoName =  HandsontableUtils.getValue(row, 5);
            String departmentName = HandsontableUtils.getValue(row, 6);
            String otherTypeName =  HandsontableUtils.getValue(row, 7);
            String expenseTypeName =  HandsontableUtils.getValue(row, 8);
            String customerNameFor = "";
            if (row.size() > 9) {
                customerNameFor =  HandsontableUtils.getValue(row, 9);
            }
            CnJournalFEntityForCashDto cnJournalFEntityForCashDto = new CnJournalFEntityForCashDto();
            cnJournalFEntityForCashDto.setAccountNumber(accountNumber);
            cnJournalFEntityForCashDto.setDebitAmount(debitAmount);
            cnJournalFEntityForCashDto.setCreditAmount(creditAmount);
            cnJournalFEntityForCashDto.setRemarks(remarks);
            cnJournalFEntityForCashDto.setStaffNumber(empInfoNameMap.get(empInfoName));
            cnJournalFEntityForCashDto.setDepartmentNumber(departmentNameMap.get(departmentName));
            cnJournalFEntityForCashDto.setOtherTypeNumber(otherTypeNameMap.get(otherTypeName));
            cnJournalFEntityForCashDto.setExpenseTypeNumber(expenseNameMap.get(expenseTypeName));
            cnJournalFEntityForCashDto.setCustomerNumberFor(customerNameMap.get(customerNameFor));
            cnJournalForCashDto.getfEntityDtoList().add(cnJournalFEntityForCashDto);
        }
        KingdeeSynDto kingdeeSynDto;
        Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
        if(isLogin) {
            kingdeeSynDto = save(cnJournalForCashDto, kingdeeBook);
        }else{
            kingdeeSynDto = new KingdeeSynDto(false,"未登入金蝶系统");
        }
        return kingdeeSynDto;
    }

    public CnJournalForCashForm getForm(CnJournalForCashForm cnJournalForCashForm,KingdeeBook kingdeeBook){
        Map<String,Object> map = Maps.newHashMap();
        map.put("accountNumberList",bdAccountRepository.findAll().stream().map(BdAccount::getFNumber).collect(Collectors.toList()));
        map.put("staffNameList",hrEmpInfoRepository.findAll().stream().map(HrEmpInfo::getFName).collect(Collectors.toList()));
        map.put("departmentNameList",bdDepartmentRepository.findAll().stream().map(BdDepartment::getFFullName).collect(Collectors.toList()));
        map.put("otherTypeNameList",basAssistantRepository.findByType("其他类").stream().map(BasAssistant::getFDataValue).collect(Collectors.toList()));
        map.put("expenseTypeNameList",basAssistantRepository.findByType("费用类").stream().map(BasAssistant::getFDataValue).collect(Collectors.toList()));
        //是否为对方关联客户
        if (KingdeeNameEnum.WZOPPO.name().equals(kingdeeBook.getName()) || KingdeeTypeEnum.proxy.name().equals(kingdeeBook.getType())) {
            map.put("customerForFlag",true);
            map.put("customerNameForList",bdCustomerRepository.findAll().stream().map(BdCustomer::getFName).collect(Collectors.toList()));
        }else {
            map.put("customerForFlag",false);
        }
        cnJournalForCashForm.setExtra(map);
        return cnJournalForCashForm;
    }
}
