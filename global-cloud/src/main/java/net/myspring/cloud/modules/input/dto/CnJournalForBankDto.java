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
    private String creatorK3;
    //日期
    private LocalDate dateK3;
    //对方账户（科目）
    private String accountNumberForBankK3;
    //账套名称
    private String kingdeeNameK3;
    //账套类型
    private String kingdeeTypeK3;

    private List<CnJournalFEntityForBankDto> fEntityDtoList = Lists.newArrayList();

    public String getCreatorK3() {
        return creatorK3;
    }

    public void setCreatorK3(String creatorK3) {
        this.creatorK3 = creatorK3;
    }

    public LocalDate getDateK3() {
        return dateK3;
    }

    public void setDateK3(LocalDate dateK3) {
        this.dateK3 = dateK3;
    }

    public String getAccountNumberForBankK3() {
        return accountNumberForBankK3;
    }

    public void setAccountNumberForBankK3(String accountNumberForBankK3) {
        this.accountNumberForBankK3 = accountNumberForBankK3;
    }

    public String getKingdeeNameK3() {
        return kingdeeNameK3;
    }

    public void setKingdeeNameK3(String kingdeeNameK3) {
        this.kingdeeNameK3 = kingdeeNameK3;
    }

    public String getKingdeeTypeK3() {
        return kingdeeTypeK3;
    }

    public void setKingdeeTypeK3(String kingdeeTypeK3) {
        this.kingdeeTypeK3 = kingdeeTypeK3;
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
        root.put("Creator", getCreatorK3());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FDATE",  LocalDateUtils.format(getDateK3(),"yyyy-M-d"));
        model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "SGRJZ01_SYS"));
        model.put("FPAYORGID", CollectionUtil.getMap("FNumber", "100"));
        model.put("FAcctBookId", CollectionUtil.getMap("FNumber", "001"));
        model.put("FSTARTPERIOD", LocalDateUtils.format(getDateK3(),"yyyy.M"));
        model.put("FACCOUNTID", CollectionUtil.getMap("FNumber", getAccountNumberForBankK3()));
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
            detail.put("F_PAEC_Base1", CollectionUtil.getMap("FStaffNumber", fEntityDto.getEmpInfoNumberK3()));
            detail.put("F_PAEC_Assistant", CollectionUtil.getMap("FNumber", fEntityDto.getOtherTypeNumberK3()));
            detail.put("F_PAEC_Assistant1", CollectionUtil.getMap("FNumber", fEntityDto.getExpenseTypeNumberK3()));
            if (StringUtils.isNotBlank(fEntityDto.getCustomerNumberK3())){
                if (KingdeeNameEnum.WZOPPO.name().equals(getKingdeeNameK3())) {
                    detail.put("F_PAEC_Base2", CollectionUtil.getMap("FNumber", fEntityDto.getCustomerNumberK3()));
                }else if (KingdeeTypeEnum.proxy.name().equals(getKingdeeTypeK3())){
                    detail.put("F_YLG_BASE", CollectionUtil.getMap("FNumber", fEntityDto.getCustomerNumberK3()));
                }
            }
            detail.put("FSETTLETYPEID", CollectionUtil.getMap("FNumber", fEntityDto.getSettleTypeNumberK3()));
            detail.put("FCREDITAMOUNT", fEntityDto.getCreditAmount());
            // 借方
            detail.put("FDEBITAMOUNT", fEntityDto.getDebitAmount());
            detail.put("FVOUCHERGROUPID", CollectionUtil.getMap("FNumber", "PRE001"));
            detail.put("FBANKACCOUNTID", CollectionUtil.getMap("FNumber", fEntityDto.getBankAccountNumber()));
            detail.put("FOPPOSITEACCOUNTID", CollectionUtil.getMap("FNumber", fEntityDto.getAccountNumberK3()));
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
