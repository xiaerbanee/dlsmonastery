package net.myspring.cloud.modules.input.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.enums.KingdeeNameEnum;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.time.LocalDateUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 收款退款单
 * Created by lihx on 2017/6/20.
 */
public class ArRefundBillDto {
    //附加-业务系统单据Id
    private String extendId;
    //附加-单据类型
    private String extendType;
    //创建人
    private String creatorK3;
    private LocalDate date;
    private String customerNumber;
    private String departmentNumber;
    private BigDecimal amount;
    private String kingdeeName;

    private List<ArRefundBillEntityDto> arRefundBillEntityDtoList;

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

    public String getCreatorK3() {
        return creatorK3;
    }

    public void setCreatorK3(String creatorK3) {
        this.creatorK3 = creatorK3;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public void setDepartmentNumber(String departmentNumber) {
        this.departmentNumber = departmentNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getKingdeeName() {
        return kingdeeName;
    }

    public void setKingdeeName(String kingdeeName) {
        this.kingdeeName = kingdeeName;
    }

    public List<ArRefundBillEntityDto> getArRefundBillEntityDtoList() {
        return arRefundBillEntityDtoList;
    }

    public void setArRefundBillEntityDtoList(List<ArRefundBillEntityDto> arRefundBillEntityDtoList) {
        this.arRefundBillEntityDtoList = arRefundBillEntityDtoList;
    }

    public String getJson() {
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", getCreatorK3());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "SKTKDLX01_SYS"));
        model.put("FDATE", LocalDateUtils.format(getDate(),"yyyy-MM-dd"));
        model.put("FCONTACTUNITTYPE", "BD_Customer");
        model.put("FCONTACTUNIT", CollectionUtil.getMap("FNumber", getCustomerNumber()));
        model.put("FRECTUNITTYPE", "BD_Customer");
        model.put("FRECTUNIT", CollectionUtil.getMap("FNumber", getCustomerNumber()));
        model.put("FCURRENCYID", CollectionUtil.getMap("FNumber", "PRE001"));
        model.put("FSETTLECUR", CollectionUtil.getMap("FNumber", "PRE001"));
        model.put("FPAYORGID", CollectionUtil.getMap("FNumber", 100));
        model.put("FSETTLEORGID", CollectionUtil.getMap("FNumber", 100));
        model.put("FSALEORGID", CollectionUtil.getMap("FNumber", 100));
        model.put("FSALEDEPTID", CollectionUtil.getMap("FNumber", getDepartmentNumber()));
        model.put("FREFUNDTOTALAMOUNTFOR", getAmount());
        model.put("FREALREFUNDAMOUNTFOR", getAmount());
        model.put("FSETTLERATE", 1);
        model.put("FEXCHANGERATE", 1);
        model.put("FBUSINESSTYPE", 1);
        List<Object> entity = Lists.newArrayList();
        for (ArRefundBillEntityDto entityDto : getArRefundBillEntityDtoList()){
            Map<String, Object> detail = Maps.newLinkedHashMap();
            //结算方式--现金
            if ("JSFS01_SYS".equals(entityDto.getSettleTypeNumber())){
                detail.put("FSETTLETYPEID", CollectionUtil.getMap("FNumber", "JSFS01_SYS"));
                //结算方式--电汇
            }else if ("JSFS04_SYS".equals(entityDto.getSettleTypeNumber())){
                detail.put("FSETTLETYPEID", CollectionUtil.getMap("FNumber", "JSFS04_SYS"));
                detail.put("FACCOUNTID", CollectionUtil.getMap("FNumber", entityDto.getBankAcntNumber()));
            }
            detail.put("FPURPOSEID", CollectionUtil.getMap("FNumber", "SFKYT01_SYS"));
            if (KingdeeNameEnum.WZOPPO.name().equals(getKingdeeName())) {
                detail.put("F_YLG_Base", CollectionUtil.getMap("FNumber", entityDto.getAccountNumber()));
            }
            detail.put("FREFUNDAMOUNTFOR", getAmount());
            detail.put("FREFUNDAMOUNTFOR_E", getAmount());
            detail.put("FREALREFUNDAMOUNTFOR_D", getAmount());
            detail.put("FNOTE", entityDto.getNote());
            entity.add(detail);
        }
        model.put("FREFUNDBILLENTRY", entity);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        //System.out.println(result);
        return result;
    }

}
