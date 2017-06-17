package net.myspring.cloud.modules.input.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.enums.KingdeeNameEnum;
import net.myspring.cloud.common.enums.KingdeeTypeEnum;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 手工日记账之銀行存取款日记账
 * Created by lihx on 2017/6/9.
 */
public class CnJournalForBankDto {
    //创建人
    private String creator;
    //日期
    private LocalDate date;
    //对方账户（科目）
    private String accountNumberForBank;
    //账套名称
    private String kingdeeName;
    //账套类型
    private String kingdeeType;

    private List<CnJournalFEntityForBankDto> fEntityDtoList = Lists.newArrayList();

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getAccountNumberForBank() {
        return accountNumberForBank;
    }

    public void setAccountNumberForBank(String accountNumberForBank) {
        this.accountNumberForBank = accountNumberForBank;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getKingdeeName() {
        return kingdeeName;
    }

    public void setKingdeeName(String kingdeeName) {
        this.kingdeeName = kingdeeName;
    }

    public String getKingdeeType() {
        return kingdeeType;
    }

    public void setKingdeeType(String kingdeeType) {
        this.kingdeeType = kingdeeType;
    }

    public List<CnJournalFEntityForBankDto> getfEntityDtoList() {
        return fEntityDtoList;
    }

    public void setfEntityDtoList(List<CnJournalFEntityForBankDto> fEntityDtoList) {
        this.fEntityDtoList = fEntityDtoList;
    }

    @JsonIgnore
    public String getJson() {
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", getCreator());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FDATE",  LocalDateUtils.format(getDate(),"yyyy-M-d"));
        model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "SGRJZ01_SYS"));
        model.put("FPAYORGID", CollectionUtil.getMap("FNumber", "100"));
        model.put("FAcctBookId", CollectionUtil.getMap("FNumber", "001"));
        model.put("FSTARTPERIOD", LocalDateUtils.format(getDate(),"yyyy.M"));
        model.put("FACCOUNTID", CollectionUtil.getMap("FNumber", getAccountNumberForBank()));
        model.put("FCURRENCYID", CollectionUtil.getMap("FNumber", "PRE001"));
        model.put("FMAINBOOKID", CollectionUtil.getMap("FNumber", "PRE001"));
        model.put("FEXCHANGETYPE", CollectionUtil.getMap("FNumber", "HLTX01_SYS"));
        model.put("FEXCHANGERATE", 1);
        BigDecimal debitAmounts = BigDecimal.ZERO;
        BigDecimal creditAmounts = BigDecimal.ZERO;
        List<Object> entity = Lists.newArrayList();
        for (CnJournalFEntityForBankDto fEntityDto : fEntityDtoList){
            Map<String, Object> detail = Maps.newLinkedHashMap();
            detail.put("F_PAEC_Base", CollectionUtil.getMap("FNumber", fEntityDto.getDepartmentNumber()));
            detail.put("F_PAEC_Base1", CollectionUtil.getMap("FStaffNumber", fEntityDto.getEmpInfoNumber()));
            detail.put("F_PAEC_Assistant", CollectionUtil.getMap("FNumber", fEntityDto.getOtherTypeNumber()));
            detail.put("F_PAEC_Assistant1", CollectionUtil.getMap("FNumber", fEntityDto.getExpenseTypeNumber()));
            if (StringUtils.isNotBlank(fEntityDto.getCustomerNumberFor())){
                if (KingdeeNameEnum.WZOPPO.name().equals(getKingdeeName())) {
                    detail.put("F_PAEC_Base2", CollectionUtil.getMap("FNumber", fEntityDto.getCustomerNumberFor()));
                }else if (KingdeeTypeEnum.proxy.name().equals(getKingdeeType())){
                    detail.put("F_YLG_BASE", CollectionUtil.getMap("FNumber", fEntityDto.getCustomerNumberFor()));
                }
            }
            detail.put("FSETTLETYPEID", CollectionUtil.getMap("FNumber", fEntityDto.getSettleTypeNumber()));
            detail.put("FCREDITAMOUNT", fEntityDto.getCreditAmount());
            // 借方
            detail.put("FDEBITAMOUNT", fEntityDto.getDebitAmount());
            detail.put("FVOUCHERGROUPID", CollectionUtil.getMap("FNumber", "PRE001"));
            detail.put("FBANKACCOUNTID", CollectionUtil.getMap("FNumber", fEntityDto.getBankAccountNumber()));
            detail.put("FOPPOSITEACCOUNTID", CollectionUtil.getMap("FNumber", fEntityDto.getAccountNumber()));
            detail.put("FCOMMENT", fEntityDto.getComment());
            entity.add(detail);
            debitAmounts = debitAmounts.add(fEntityDto.getDebitAmount());
            creditAmounts = creditAmounts.add(fEntityDto.getCreditAmount());
        }
        model.put("FCREDITSUMAMOUNTLOC", creditAmounts);
        model.put("FCREDITSUMAMOUNT", creditAmounts);
        // 借方
        model.put("FDEBITSUMAMOUNT", debitAmounts);
        model.put("FDEBITSUMAMOUNTLOC", debitAmounts);
        model.put("FJOURNALENTRY", entity);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        System.out.println(result);
        return result;
    }
   
    
}
