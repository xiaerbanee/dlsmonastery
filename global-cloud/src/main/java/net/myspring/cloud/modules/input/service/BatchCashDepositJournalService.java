package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.K3CloudFormIdEnum;
import net.myspring.cloud.common.enums.KingdeeNameEnum;
import net.myspring.cloud.common.handsontable.HandSonTableUtils;
import net.myspring.cloud.common.utils.CacheUtils;
import net.myspring.cloud.common.utils.SecurityUtils;
import net.myspring.cloud.modules.input.domain.BasAssistant;
import net.myspring.cloud.modules.input.domain.HrEmpInfo;
import net.myspring.cloud.modules.input.dto.K3CloudSaveDto;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import net.myspring.cloud.modules.input.mapper.BasAssistantMapper;
import net.myspring.cloud.modules.input.mapper.BdCustomerMapper;
import net.myspring.cloud.modules.input.mapper.BdDepartmentMapper;
import net.myspring.cloud.modules.input.mapper.HrEmpInfoMapper;
import net.myspring.cloud.modules.remote.dto.AccountDto;
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
 * 现金存款日记
 * Created by lihx on 2016/10/10.
 */
@Service
@KingdeeDataSource
public class BatchCashDepositJournalService {
    @Autowired
    private BdCustomerMapper bdCustomerMapper;
    @Autowired
    private BdDepartmentMapper bdDepartmentMapper;
    @Autowired
    private BasAssistantMapper basAssistantMapper;
    @Autowired
    private HrEmpInfoMapper hrEmpInfoMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private KingdeeBookMapper kingdeeBookMapper;

    //手工日记账(现金日记账)
    public String saveCashJournal(LocalDate billDate, List<List<Object>> datas) {
        Map<String, String> departMap = Maps.newHashMap();
        Map<String, String> secUserMap = Maps.newHashMap();
        Map<String, String> otherTypeMap = Maps.newHashMap();
        Map<String, String> expenseMap = Maps.newHashMap();
        Map<String, String> customerMap = Maps.newHashMap();
        for (NameNumberDto bdCustomer : bdCustomerMapper.findNameAndNumber()) {
            customerMap.put(bdCustomer.getName(), bdCustomer.getNumber());
        }
        for (NameNumberDto bdSettleType : bdDepartmentMapper.findNameAndNumber()) {
            departMap.put(bdSettleType.getName(), bdSettleType.getNumber());
        }
        for (BasAssistant bdSettleType : basAssistantMapper.findByType("其他类")) {
            otherTypeMap.put(bdSettleType.getfDataValue(), bdSettleType.getfNumber());
        }
        for (BasAssistant bdSettleType : basAssistantMapper.findByType("费用类")) {
            expenseMap.put(bdSettleType.getfDataValue(), bdSettleType.getfNumber());
        }
        for (HrEmpInfo secUser : hrEmpInfoMapper.findAllUser()) {
            secUserMap.put(secUser.getfName(), secUser.getfNumber());
        }
        AccountDto accountDto = new AccountDto();
        cacheUtils.initCacheInput(accountDto);
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", accountDto.getName());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FDATE", LocalDateUtils.format(billDate));
        model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "SGRJZ02_SYS"));
        model.put("FPAYORGID", CollectionUtil.getMap("FNumber", "100"));
        model.put("FAcctBookId", CollectionUtil.getMap("FNumber", "001"));
        model.put("FSTARTPERIOD", LocalDateUtils.format(billDate,LocalDateUtils.FORMATTER_MONTH_SINGLE));
        model.put("FACCOUNTID", CollectionUtil.getMap("FNumber", "1001"));
        model.put("FCURRENCYID", CollectionUtil.getMap("FNumber", "PRE001"));
        model.put("FMAINBOOKID", CollectionUtil.getMap("FNumber", "PRE001"));
        model.put("FEXCHANGETYPE", CollectionUtil.getMap("FNumber", "HLTX01_SYS"));
        model.put("FEXCHANGERATE", 1);

        BigDecimal debitAmounts = BigDecimal.ZERO;
        BigDecimal creditAmounts = BigDecimal.ZERO;
        List<Object> entity = Lists.newArrayList();
        for (List<Object> row : datas) {
            String subject = HandSonTableUtils.getValue(row, 0);
            String debitAmountStr =  HandSonTableUtils.getValue(row, 1);
            BigDecimal debitAmount = StringUtils.isEmpty(debitAmountStr) ? BigDecimal.ZERO : new BigDecimal(debitAmountStr);
            String creditAmountStr =  HandSonTableUtils.getValue(row, 2);
            BigDecimal creditAmount = StringUtils.isEmpty(creditAmountStr) ? BigDecimal.ZERO : new BigDecimal(creditAmountStr);
            String remarks =  HandSonTableUtils.getValue(row, 3);
            String user = "";
            String department = "";
            String otherType = "";
            String expenseType = "";
            String F_PAEC_Base2 = "";
            user =  HandSonTableUtils.getValue(row, 5);
            department = HandSonTableUtils.getValue(row, 6);
            otherType =  HandSonTableUtils.getValue(row, 7);
            expenseType =  HandSonTableUtils.getValue(row, 8);
            if (row.size() > 9) {
                F_PAEC_Base2 =  HandSonTableUtils.getValue(row, 9);
            }
            debitAmounts = debitAmounts.add(debitAmount);
            creditAmounts = creditAmounts.add(creditAmount);

            Map<String, Object> detail = Maps.newLinkedHashMap();
            detail.put("F_PAEC_Base", CollectionUtil.getMap("FNumber", departMap.get(department)));
            detail.put("F_PAEC_Base1", CollectionUtil.getMap("FStaffNumber", secUserMap.get(user)));
            detail.put("F_PAEC_Assistant", CollectionUtil.getMap("FNumber", otherTypeMap.get(otherType)));
            detail.put("F_PAEC_Assistant1", CollectionUtil.getMap("FNumber", expenseMap.get(expenseType)));
            if (KingdeeNameEnum.WZOPPO.name().equals(kingdeeBookMapper.findNameByCompanyId(SecurityUtils.getCompanyId())) && StringUtils.isNotBlank(F_PAEC_Base2)) {
                detail.put("F_PAEC_Base2", CollectionUtil.getMap("FNumber", customerMap.get(F_PAEC_Base2)));
            }
            detail.put("FSETTLETYPEID", CollectionUtil.getMap("FNumber", "JSFS01_SYS"));
            detail.put("FCREDITAMOUNT", creditAmount);

            // 借方金额
            detail.put("FDEBITAMOUNT", debitAmount);
            detail.put("FVOUCHERGROUPID", CollectionUtil.getMap("FNumber", "PRE001"));
            detail.put("FOPPOSITEACCOUNTID", CollectionUtil.getMap("FNumber", subject));
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
        String JournalResult = ObjectMapperUtils.writeValueAsString(root);
        K3CloudSaveDto k3CloudSaveDto = new K3CloudSaveDto(K3CloudFormIdEnum.CN_JOURNAL.name(), JournalResult);
        String billNo =null;
        return billNo;
    }
}
