package net.myspring.future.modules.basic.manager;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.modules.input.dto.CnJournalEntityForBankDto;
import net.myspring.cloud.modules.input.dto.CnJournalForBankDto;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.common.enums.SettleTypeEnum;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.Bank;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.EmployeePhoneDeposit;
import net.myspring.future.modules.basic.repository.BankRepository;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.layout.domain.ShopDeposit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 银行存款日记账
 * Created by lihx on 2017/6/30.
 */
@Component
public class CnJournalBankManager {
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private CloudClient cloudClient;

    public KingdeeSynReturnDto synForShopDeposit(ShopDeposit shopDeposit,String departMentNumber){
        List<CnJournalForBankDto> cnJournalForBankDtoList = Lists.newArrayList();
        Bank bank = bankRepository.findOne(shopDeposit.getBankId());
        Depot depot = depotRepository.findOne(shopDeposit.getShopId());
        CnJournalForBankDto cnJournalForBankDto = new CnJournalForBankDto();
        cnJournalForBankDto.setExtendId(shopDeposit.getId());
        cnJournalForBankDto.setExtendType(ExtendTypeEnum.押金列表.name());
        cnJournalForBankDto.setDate(shopDeposit.getBillDate());
        cnJournalForBankDto.setAccountNumberForBank("1002");//银行存款
        List<CnJournalEntityForBankDto> cnJournalEntityForBankDtoList = Lists.newArrayList();

        CnJournalEntityForBankDto entityForBankDto = new CnJournalEntityForBankDto();
        entityForBankDto.setDebitAmount(shopDeposit.getAmount());
        entityForBankDto.setCreditAmount(shopDeposit.getAmount().multiply(new BigDecimal(-1)));
        entityForBankDto.setDepartmentNumber(departMentNumber);
        entityForBankDto.setBankAccountNumber(bank.getCode());
        entityForBankDto.setAccountNumber("2241");//其他应付款
        entityForBankDto.setSettleTypeNumber(SettleTypeEnum.电汇.getFNumber());//电汇
        entityForBankDto.setEmpInfoNumber("0001");//员工
        entityForBankDto.setOtherTypeNumber("2241.00002B");//其他应付款-客户押金（批发）-市场保证金
        entityForBankDto.setExpenseTypeNumber("6602.000");//无
        entityForBankDto.setCustomerNumber(null);
        entityForBankDto.setComment(depot.getName()+shopDeposit.getRemarks());
        cnJournalEntityForBankDtoList.add(entityForBankDto);
        cnJournalForBankDto.setEntityForBankDtoList(cnJournalEntityForBankDtoList);
        cnJournalForBankDtoList.add(cnJournalForBankDto);

        return cloudClient.synJournalBank(cnJournalForBankDtoList).get(0);

    }

    public List<KingdeeSynReturnDto> synEmployeePhoneDeposit(List<EmployeePhoneDeposit> employeePhoneDepositList){
        List<CnJournalForBankDto> cnJournalForBankDtoList = Lists.newArrayList();
        for (EmployeePhoneDeposit employeePhoneDeposit : employeePhoneDepositList) {
            Bank bank = bankRepository.findOne(employeePhoneDeposit.getBankId());
            Depot depot = depotRepository.findOne(employeePhoneDeposit.getDepotId());
            CnJournalForBankDto cnJournalForBankDto = new CnJournalForBankDto();
            cnJournalForBankDto.setExtendId(employeePhoneDeposit.getId());
            cnJournalForBankDto.setExtendType(ExtendTypeEnum.导购用机.name());
            cnJournalForBankDto.setDate(LocalDate.now());
            List<CnJournalEntityForBankDto> cnJournalEntityForBankDtoList = Lists.newArrayList();
            CnJournalEntityForBankDto entityForBankDto = new CnJournalEntityForBankDto();
            entityForBankDto.setDebitAmount(employeePhoneDeposit.getAmount());
            entityForBankDto.setCreditAmount(employeePhoneDeposit.getAmount().multiply(new BigDecimal(-1)));
            entityForBankDto.setDepartmentNumber(employeePhoneDeposit.getDepartment());
            entityForBankDto.setBankAccountNumber(bank.getCode());
            entityForBankDto.setAccountNumber("2241");//其他应付款
            entityForBankDto.setSettleTypeNumber(SettleTypeEnum.电汇.getFNumber());//电汇
            entityForBankDto.setEmpInfoNumber("0001");//员工
            entityForBankDto.setOtherTypeNumber("2241.00029");//其他应付款-导购业务机押金
            entityForBankDto.setExpenseTypeNumber("6602.000");//无
            entityForBankDto.setCustomerNumber(null);
            entityForBankDto.setComment(depot.getName()+employeePhoneDeposit.getRemarks());
            cnJournalEntityForBankDtoList.add(entityForBankDto);
            cnJournalForBankDto.setEntityForBankDtoList(cnJournalEntityForBankDtoList);
            cnJournalForBankDtoList.add(cnJournalForBankDto);
        }
        return cloudClient.synJournalBank(cnJournalForBankDtoList);
    }

}
