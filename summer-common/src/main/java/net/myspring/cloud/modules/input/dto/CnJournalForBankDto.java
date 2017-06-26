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
    //附加-业务系统单据Id
    private String extendId;
    //附加-单据类型
    private String extendType;
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

    private List<CnJournalEntityForBankDto> entityForBankDtoList = Lists.newArrayList();

    public String getExtendId() {
        return extendId;
    }

    public void setExtendId(String extendId) {
        this.extendId = extendId;
    }

    public String getExtendType() {
        return extendType;
    }

    public void setExtendType(String extendType) {
        this.extendType = extendType;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAccountNumberForBank() {
        return accountNumberForBank;
    }

    public void setAccountNumberForBank(String accountNumberForBank) {
        this.accountNumberForBank = accountNumberForBank;
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

    public List<CnJournalEntityForBankDto> getEntityForBankDtoList() {
        return entityForBankDtoList;
    }

    public void setEntityForBankDtoList(List<CnJournalEntityForBankDto> entityForBankDtoList) {
        this.entityForBankDtoList = entityForBankDtoList;
    }

    @JsonIgnore
    public String getJson() {
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", getCreator());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FDATE",  LocalDateUtils.format(getDate(),"yyyy-M-d"));
        //单据类型--
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
        for (CnJournalEntityForBankDto entityForBankDto : getEntityForBankDtoList()){
            Map<String, Object> detail = Maps.newLinkedHashMap();
            detail.put("F_PAEC_Base", CollectionUtil.getMap("FNumber", entityForBankDto.getDepartmentNumber()));
            detail.put("F_PAEC_Base1", CollectionUtil.getMap("FStaffNumber", entityForBankDto.getEmpInfoNumber()));
            detail.put("F_PAEC_Assistant", CollectionUtil.getMap("FNumber", entityForBankDto.getOtherTypeNumber()));
            detail.put("F_PAEC_Assistant1", CollectionUtil.getMap("FNumber", entityForBankDto.getExpenseTypeNumber()));
            if (StringUtils.isNotBlank(entityForBankDto.getCustomerNumber())){
                if (KingdeeNameEnum.WZOPPO.name().equals(getKingdeeName())) {
                    detail.put("F_PAEC_Base2", CollectionUtil.getMap("FNumber", entityForBankDto.getCustomerNumber()));
                }else if (KingdeeTypeEnum.proxy.name().equals(getKingdeeType())){
                    detail.put("F_YLG_BASE", CollectionUtil.getMap("FNumber", entityForBankDto.getCustomerNumber()));
                }
            }
            detail.put("FSETTLETYPEID", CollectionUtil.getMap("FNumber", entityForBankDto.getSettleTypeNumber()));
            detail.put("FCREDITAMOUNT", entityForBankDto.getCreditAmount());
            // 借方
            detail.put("FDEBITAMOUNT", entityForBankDto.getDebitAmount());
            detail.put("FVOUCHERGROUPID", CollectionUtil.getMap("FNumber", "PRE001"));
            detail.put("FBANKACCOUNTID", CollectionUtil.getMap("FNumber", entityForBankDto.getBankAccountNumber()));
            detail.put("FOPPOSITEACCOUNTID", CollectionUtil.getMap("FNumber", entityForBankDto.getAccountNumber()));
            detail.put("FCOMMENT", entityForBankDto.getComment());
            entity.add(detail);
            debitAmounts = debitAmounts.add(entityForBankDto.getDebitAmount());
            creditAmounts = creditAmounts.add(entityForBankDto.getCreditAmount());
        }
        //贷方（负）
        model.put("FCREDITSUMAMOUNTLOC", creditAmounts);
        model.put("FCREDITSUMAMOUNT", creditAmounts);
        // 借方
        model.put("FDEBITSUMAMOUNT", debitAmounts);
        model.put("FDEBITSUMAMOUNTLOC", debitAmounts);
        model.put("FJOURNALENTRY", entity);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        //System.out.println(result);
        return result;
    }
   
    
}
