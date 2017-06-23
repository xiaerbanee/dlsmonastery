package net.myspring.cloud.modules.input.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.enums.KingdeeNameEnum;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 其他应收单
 * Created by lihx on 2017/6/20.
 */
public class ArOtherRecAbleDto {
    //附加-业务系统单据Id
    private String extendId;
    //附加-单据类型
    private String extendType;
    //创建人
    private String creatorK3;
    // 客户
    private String customerNumber;
    // 日期
    private LocalDate date;
    //部门
    private String departmentNumber;
    // 总金额
    private BigDecimal amount = BigDecimal.ZERO;
    // 账套名称
    private String kingdeeName;
    
    private List<ArOtherRecAbleFEntityDto> arOtherRecAbleFEntityDtoList = Lists.newArrayList();

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

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public List<ArOtherRecAbleFEntityDto> getArOtherRecAbleFEntityDtoList() {
        return arOtherRecAbleFEntityDtoList;
    }

    public void setArOtherRecAbleFEntityDtoList(List<ArOtherRecAbleFEntityDto> arOtherRecAbleFEntityDtoList) {
        this.arOtherRecAbleFEntityDtoList = arOtherRecAbleFEntityDtoList;
    }

    @JsonIgnore
    public String getJson() {
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", getCreatorK3());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "QTYSD01_SYS"));
        model.put("FDATE", getDate());
        model.put("FCONTACTUNITTYPE", "BD_Customer");
        model.put("FCONTACTUNIT", CollectionUtil.getMap("FNumber", getCustomerNumber()));
        model.put("FCURRENCYID", CollectionUtil.getMap("FNumber", "PRE001"));
        model.put("FAMOUNTFOR", getAmount());
        model.put("FDEPARTMENTID", CollectionUtil.getMap("FNumber", getDepartmentNumber()));
        model.put("FSALEORGID", CollectionUtil.getMap("FNumber", 100));
        model.put("FSETTLEORGID", CollectionUtil.getMap("FNumber", 100));
        model.put("FPAYORGID", CollectionUtil.getMap("FNumber", 100));
        model.put("FEXCHANGETYPE", CollectionUtil.getMap("FNumber", "HLTX01_SYS"));
        model.put("FEXCHANGERATE", 1);
        model.put("FMAINBOOKSTDCURRID", CollectionUtil.getMap("FNumber", "PRE001"));
        List<Object> entity = Lists.newArrayList();
        for (ArOtherRecAbleFEntityDto entityDto : getArOtherRecAbleFEntityDtoList()) {
            Map<String, Object> detail = Maps.newLinkedHashMap();
            detail.put("FCOSTDEPARTMENTID", CollectionUtil.getMap("FNumber", entityDto.getDepartmentNumber()));
            detail.put("F_YLG_Base", CollectionUtil.getMap("FNumber", entityDto.getAccountNumber()));
            detail.put("F_PAEC_Assistant", CollectionUtil.getMap("FNumber", entityDto.getOtherTypeNumber()));
            detail.put("F_PAEC_Assistant1", CollectionUtil.getMap("FNumber", entityDto.getExpenseTypeNumber()));
            if (StringUtils.isNotBlank(entityDto.getCustomerForNumber())) {
                if (KingdeeNameEnum.WZOPPO.name().equals(getKingdeeName())) {
                    detail.put("F_PAEC_Base", CollectionUtil.getMap("FNumber", entityDto.getCustomerForNumber()));
                } else {
                    detail.put("F_YLG_Base2", CollectionUtil.getMap("FNumber", entityDto.getCustomerForNumber()));
                }
            }
            detail.put("F_YLG_Base1", CollectionUtil.getMap("FStaffNumber", entityDto.getEmpInfoNumber()));
            detail.put("FNOTAXAMOUNTFOR", entityDto.getAmount());
            detail.put("FAMOUNTFOR_D", entityDto.getAmount());
            detail.put("FAMOUNT_D", entityDto.getAmount());
            detail.put("FCOMMENT", entityDto.getComment());
            entity.add(detail);
        }
        model.put("FEntity", entity);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        //System.out.println(result);
        return result;
    }
}
