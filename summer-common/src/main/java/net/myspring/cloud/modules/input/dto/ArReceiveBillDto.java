package net.myspring.cloud.modules.input.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.time.LocalDateUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 收款单
 * Created by lihx on 2017/6/24.
 */
public class ArReceiveBillDto {
    //附加-业务系统单据Id
    private String extendId;
    //附加-单据类型
    private String extendType;
    //创建人
    private String creator;
    // 日期
    private LocalDate date;
    //往来单位
    private String customerNumber;
    //销售部门
    private String departmentNumber;

    private List<ArReceiveBillEntryDto> arReceiveBillEntryDtoList = Lists.newArrayList();

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

    public List<ArReceiveBillEntryDto> getArReceiveBillEntryDtoList() {
        return arReceiveBillEntryDtoList;
    }

    public void setArReceiveBillEntryDtoList(List<ArReceiveBillEntryDto> arReceiveBillEntryDtoList) {
        this.arReceiveBillEntryDtoList = arReceiveBillEntryDtoList;
    }

    @JsonIgnore
    public String getJson() {
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", getCreator());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        BigDecimal sumAmount = BigDecimal.ZERO;
        model.put("FID", 0);
        //单据类型--
        model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "SKDLX01_SYS"));
        model.put("FDATE", LocalDateUtils.format(getDate()));
        //往来单位类型--
        model.put("FCONTACTUNITTYPE", "BD_Customer");
        //往来单位
        model.put("FCONTACTUNIT", CollectionUtil.getMap("FNumber", getCustomerNumber()));
        //付款单位类型
        model.put("FPAYUNITTYPE", "BD_Customer");
        //付款单位
        model.put("FPAYUNIT", CollectionUtil.getMap("FNumber", getCustomerNumber()));
        //币别
        model.put("FCURRENCYID", CollectionUtil.getMap("FNumber", "PRE001"));
        //结算币别
        model.put("FSETTLECUR", CollectionUtil.getMap("FNumber", "PRE001"));
        //收款组织
        model.put("FPAYORGID", CollectionUtil.getMap("FNumber",100));
        //结算组织
        model.put("FSETTLEORGID", CollectionUtil.getMap("FNumber", 100));
        //销售组织
        model.put("FSALEORGID", CollectionUtil.getMap("FNumber", 100));
        //表头-应收金额
        model.put("FRECEIVEAMOUNTFOR_H", sumAmount);
        //表头-实收金额
        model.put("FREALRECAMOUNTFOR", sumAmount);
        //结算汇率
        model.put("FSETTLERATE", 1);
        //汇率
        model.put("FEXCHANGERATE", 1);
        //业务类型
        model.put("FBUSINESSTYPE", 1);
        //销售部门
        model.put("FSALEDEPTID", CollectionUtil.getMap("FNumber", getDepartmentNumber()));
        //单据状态--已审核
        model.put("FDOCUMENTSTATUS","C");
        //作废状态--否
        model.put("FCancelStatus","A");
        List<Object> entity = Lists.newArrayList();
        for (ArReceiveBillEntryDto entryDto : getArReceiveBillEntryDtoList()) {
            Map<String, Object> detail = Maps.newLinkedHashMap();
            //结算方式--电汇
            detail.put("FSETTLETYPEID", CollectionUtil.getMap("FNumber", "JSFS04_SYS"));
            //收款用途--
            detail.put("FPURPOSEID", CollectionUtil.getMap("FNumber", "SFKYT01_SYS"));
            //对方科目代码--银行存款
            detail.put("F_YLG_Base", CollectionUtil.getMap("FNumber", "1002"));
            //表体-应收金额
            detail.put("FRECTOTALAMOUNTFOR", entryDto.getAmount());
            //折后金额
            detail.put("FSETTLERECAMOUNTFOR", entryDto.getAmount());
            //表体-实收金额
            detail.put("FREALRECAMOUNTFOR_D", entryDto.getAmount());
            //我方银行账号
            detail.put("FACCOUNTID", CollectionUtil.getMap("FNumber", entryDto.getBankAcntNumber()));
            //备注
            detail.put("FCOMMENT", entryDto.getComment());
            sumAmount = sumAmount.add(entryDto.getAmount());
            entity.add(detail);
        }
        model.put("FRECEIVEBILLENTRY", entity);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        //System.out.println(result);
        return result;
    }
}


