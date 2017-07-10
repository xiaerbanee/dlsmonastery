package net.myspring.future.modules.basic.manager;

import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.modules.input.dto.ArReceiveBillDto;
import net.myspring.cloud.modules.input.dto.ArReceiveBillEntryDto;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.common.enums.SettleTypeEnum;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.Bank;
import net.myspring.future.modules.basic.domain.Client;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.repository.BankRepository;
import net.myspring.future.modules.basic.repository.ClientRepository;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.crm.domain.BankIn;
import net.myspring.future.modules.crm.web.form.BankInAuditForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 收款单
 * Created by lihx on 2017/6/30.
 */
@Component
public class ArReceiveBillManager {
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private CloudClient cloudClient;

    public KingdeeSynReturnDto synForBankIn(BankIn bankIn, BankInAuditForm bankInAuditForm) {
        Depot depot = depotRepository.findOne(bankIn.getShopId());
        Client client = clientRepository.findOne(depot.getId());

        ArReceiveBillDto receiveBillDto = new ArReceiveBillDto();
        receiveBillDto.setExtendId(bankIn.getId());
        receiveBillDto.setExtendType(ExtendTypeEnum.销售收款.name());
        receiveBillDto.setDate(bankIn.getBillDate());
        if(client.getOutCode() != null){
            receiveBillDto.setCustomerNumber(client.getOutCode());
        }else{
            throw new ServiceException(client.getName()+",该客户没有编码，不能开单");
        }
        ArReceiveBillEntryDto entityDto = new ArReceiveBillEntryDto();
        entityDto.setAmount(bankIn.getAmount());
        if ("0".equals(bankIn.getBankId())) {
            entityDto.setSettleTypeNumber(SettleTypeEnum.现金.getFNumber());
        } else {
            Bank bank = bankRepository.findOne(bankIn.getBankId());
            entityDto.setBankAcntNumber(bank.getCode());
            entityDto.setSettleTypeNumber(SettleTypeEnum.电汇.getFNumber());
        }
        entityDto.setComment("审：" + bankInAuditForm.getAuditRemarks() + "   申：" + bankIn.getRemarks());
        receiveBillDto.setArReceiveBillEntryDtoList(Collections.singletonList(entityDto));
         return cloudClient.synReceiveBill(Collections.singletonList(receiveBillDto)).get(0);
    }
}
