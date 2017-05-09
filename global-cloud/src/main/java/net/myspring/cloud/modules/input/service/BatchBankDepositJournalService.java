package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.common.enums.KingdeeNameEnum;
import net.myspring.cloud.common.handsontable.HandSonTableUtils;
import net.myspring.cloud.common.utils.CacheUtils;
import net.myspring.cloud.common.utils.SecurityUtils;
import net.myspring.cloud.modules.input.domain.BasAssistant;
import net.myspring.cloud.modules.input.domain.HrEmpInfo;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.input.mapper.*;
import net.myspring.cloud.modules.input.web.query.BatchBankDepositJournalQuery;
import net.myspring.cloud.modules.sys.dto.AccountDto;
import net.myspring.cloud.modules.sys.mapper.KingdeeBookMapper;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


/**
 * 银行存款日记
 * Created by lihx on 2016/10/10.
 */
@Service
@KingdeeDataSource
public class BatchBankDepositJournalService {
    @Autowired
    private BdCustomerMapper bdCustomerMapper;
    @Autowired
    private BdDepartmentMapper bdDepartmentMapper;
    @Autowired
    private BasAssistantMapper basAssistantMapper;
    @Autowired
    private HrEmpInfoMapper hrEmpInfoMapper;
    @Autowired
    private BdSettleTypeMapper bdSettleTypeMapper;
    @Autowired
    private CnBankMapper cnBankMapper;
    @Autowired
    private BdAccountMapper bdAccountMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private KingdeeBookMapper kingdeeBookMapper;
    @Autowired
    private KingdeeManager kingdeeManager;

    // 手工日记账(银行存取款日记账)
    public String saveBankJournal(LocalDate billDate, String subject, List<List<Object>> datas) {
        Map<String, String> departMap = Maps.newHashMap();
        Map<String, String> settleTypeMap = Maps.newHashMap();
        Map<String, String> otherTypeMap = Maps.newHashMap();
        Map<String, String> expenseMap = Maps.newHashMap();
        Map<String, String> bankMap = Maps.newHashMap();
        Map<String, String> secUserMap = Maps.newHashMap();
        Map<String, String> customerMap = Maps.newHashMap();
        for (NameNumberDto bdCustomer : bdCustomerMapper.findNameAndNumber()) {
            customerMap.put(bdCustomer.getName(), bdCustomer.getNumber());
        }
        for (NameNumberDto department : bdDepartmentMapper.findNameAndNumber()) {
            departMap.put(department.getName(), department.getNumber());
        }
        for (NameNumberDto bdSettleType : bdSettleTypeMapper.findNameAndNumber()) {
            settleTypeMap.put(bdSettleType.getName(), bdSettleType.getNumber());
        }
        for (BasAssistant otherType : basAssistantMapper.findByType("其他类")) {
            otherTypeMap.put(otherType.getfDataValue(), otherType.getfNumber());
        }
        for (BasAssistant feeType : basAssistantMapper.findByType("费用类")) {
            expenseMap.put(feeType.getfDataValue(), feeType.getfNumber());
        }
        for (NameNumberDto bank : cnBankMapper.findNameAndNumber()) {
            bankMap.put(bank.getName(), bank.getNumber());
        }
        for (HrEmpInfo employee : hrEmpInfoMapper.findAllUser()) {
            secUserMap.put(employee.getfName(), employee.getfNumber());
        }
        AccountDto accountDto = new AccountDto();
        cacheUtils.initCacheInput(accountDto);
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", accountDto.getName());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FDATE", LocalDateUtils.format(billDate));
        model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "SGRJZ01_SYS"));
        model.put("FPAYORGID", CollectionUtil.getMap("FNumber", "100"));
        model.put("FAcctBookId", CollectionUtil.getMap("FNumber", "001"));
        model.put("FSTARTPERIOD", LocalDateUtils.format(billDate,LocalDateUtils.FORMATTER_MONTH_SINGLE));
        model.put("FACCOUNTID", CollectionUtil.getMap("FNumber", subject));
        model.put("FCURRENCYID", CollectionUtil.getMap("FNumber", "PRE001"));
        model.put("FMAINBOOKID", CollectionUtil.getMap("FNumber", "PRE001"));
        model.put("FEXCHANGETYPE", CollectionUtil.getMap("FNumber", "HLTX01_SYS"));
        model.put("FEXCHANGERATE", 1);
        BigDecimal debitAmounts = BigDecimal.ZERO;
        BigDecimal creditAmounts = BigDecimal.ZERO;
        List<Object> entity = Lists.newArrayList();
        for (List<Object> row : datas) {
            String otherSubject =  HandSonTableUtils.getValue(row, 0);
            String settleType =  HandSonTableUtils.getValue(row, 1);
            String debitAmountStr =  HandSonTableUtils.getValue(row,2);
            BigDecimal debitAmount = StringUtils.isEmpty(debitAmountStr) ? BigDecimal.ZERO : new BigDecimal(debitAmountStr);
            String creditAmountStr =  HandSonTableUtils.getValue(row, 3);
            BigDecimal creditAmount = StringUtils.isEmpty(creditAmountStr) ? BigDecimal.ZERO : new BigDecimal(creditAmountStr);
            String bankId =  HandSonTableUtils.getValue(row, 4);
            String remarks =  HandSonTableUtils.getValue(row, 5);
            String secUser = "";
            String department = "";
            String otherType = "";
            String expenseType = "";
            String F_PAEC_Base2 = "";
            secUser = HandSonTableUtils.getValue(row, 7);
            department =  HandSonTableUtils.getValue(row, 8);
            otherType = HandSonTableUtils.getValue(row, 9);
            expenseType = HandSonTableUtils.getValue(row, 10);
            if (row.size() > 11) {
                F_PAEC_Base2 =  HandSonTableUtils.getValue(row, 11);
            }
            debitAmounts = debitAmounts.add(debitAmount);
            creditAmounts = creditAmounts.add(creditAmount);

            Map<String, Object> detail = Maps.newLinkedHashMap();
            detail.put("F_PAEC_Base", CollectionUtil.getMap("FNumber", departMap.get(department)));
            detail.put("F_PAEC_Base1", CollectionUtil.getMap("FStaffNumber", secUserMap.get(secUser)));
            detail.put("F_PAEC_Assistant", CollectionUtil.getMap("FNumber", otherTypeMap.get(otherType)));
            detail.put("F_PAEC_Assistant1", CollectionUtil.getMap("FNumber", expenseMap.get(expenseType)));
            if (KingdeeNameEnum.WZOPPO.name().equals(kingdeeBookMapper.findNameByCompanyId(SecurityUtils.getCompanyId())) && StringUtils.isNotBlank(F_PAEC_Base2)) {
                detail.put("F_PAEC_Base2", CollectionUtil.getMap("FNumber", customerMap.get(F_PAEC_Base2)));
            }
            detail.put("FSETTLETYPEID", CollectionUtil.getMap("FNumber", settleTypeMap.get(settleType)));
            detail.put("FCREDITAMOUNT", creditAmount);

            // 借方金额
            detail.put("FDEBITAMOUNT", debitAmount);
            detail.put("FVOUCHERGROUPID", CollectionUtil.getMap("FNumber", "PRE001"));
            detail.put("FBANKACCOUNTID", CollectionUtil.getMap("FNumber", bankMap.get(bankId)));
            detail.put("FOPPOSITEACCOUNTID", CollectionUtil.getMap("FNumber", otherSubject));
            detail.put("FCOMMENT", remarks);
            entity.add(detail);
        }

        model.put("FCREDITSUMAMOUNTLOC", creditAmounts);
        model.put("FCREDITSUMAMOUNT", creditAmounts);
        // 借方
        model.put("FDEBITSUMAMOUNT", debitAmounts);
        model.put("FDEBITSUMAMOUNTLOC", debitAmounts);
        model.put("CN_JOURNAL__FJOURNALENTRY", entity);
        root.put("Model", model);
        String BankJournalResult = ObjectMapperUtils.writeValueAsString(root);
        KingdeeSynDto kingdeeSynDto = new KingdeeSynDto(KingdeeFormIdEnum.CN_JOURNAL.name(), BankJournalResult);
        return null;
    }

    public BatchBankDepositJournalQuery getFormProperty(BatchBankDepositJournalQuery batchBankDepositJournalQuery){
        List<NameNumberDto> accountSubjectList = bdAccountMapper.findForIsBank();
        batchBankDepositJournalQuery.setAccountSubject(accountSubjectList);
        return batchBankDepositJournalQuery;
    }

}
