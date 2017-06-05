package net.myspring.cloud.modules.input.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.enums.SalOutStockBillTypeEnum;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.time.LocalDateUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/5/11.
 */
public class SalOutStockDto {
    private String creator;
    // 客户名称
    private String customerNumber;
    //仓库
    private String storeNumber;
    // 日期
    private LocalDate date;
    // 备注
    private String note;
    //部门名称
    private String departmentNumber;
    //单据类型
    private String billType;

    private List<SalOutStockFEntityDto> salOutStockFEntityDtoList = Lists.newArrayList();

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(String storeNumber) {
        this.storeNumber = storeNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public void setDepartmentNumber(String departmentNumber) {
        this.departmentNumber = departmentNumber;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public List<SalOutStockFEntityDto> getSalOutStockFEntityDtoList() {
        return salOutStockFEntityDtoList;
    }

    public void setSalOutStockFEntityDtoList(List<SalOutStockFEntityDto> salOutStockFEntityDtoList) {
        this.salOutStockFEntityDtoList = salOutStockFEntityDtoList;
    }

    @JsonIgnore
    public String getJson() {
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", getCreator());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FDate", LocalDateUtils.format(getDate(),"yyyy-M-d"));
        if(SalOutStockBillTypeEnum.标准销售出库单.name().equals(getBillType())) {
            model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "XSCKD01_SYS"));
        }else if (SalOutStockBillTypeEnum.现销出库单.name().equals(getBillType())){
            model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "XSCKD06_SYS"));
        }
        model.put("FDeliveryDeptID", CollectionUtil.getMap("FNumber", getDepartmentNumber()));
        model.put("FSaleOrgId", CollectionUtil.getMap("FNumber", 100));
        model.put("FStockOrgId", CollectionUtil.getMap("FNumber", 100));
        model.put("FOwnerIdHead", CollectionUtil.getMap("FNumber", 100));
        model.put("FSettleCurrID", CollectionUtil.getMap("FNumber", "PRE001"));
        model.put("FCustomerID", CollectionUtil.getMap("FNumber", getCustomerNumber()));
        model.put("FNote", getNote());
        List<Object> entity = Lists.newArrayList();
        for (SalOutStockFEntityDto salOutStockFEntityDto: getSalOutStockFEntityDtoList()) {
            if (salOutStockFEntityDto.getQty() != null && salOutStockFEntityDto.getQty() > 0) {
                Map<String, Object> detail = Maps.newLinkedHashMap();
                detail.put("FStockID", CollectionUtil.getMap("FNumber", getStoreNumber()));
                detail.put("FMaterialId", CollectionUtil.getMap("FNumber", salOutStockFEntityDto.getMaterialNumber()));
                detail.put("FStockStatusID", CollectionUtil.getMap("FNumber", "KCZT01_SYS"));
                detail.put("FUnitID", CollectionUtil.getMap("FNumber", "Pcs"));
                detail.put("FRealQty", salOutStockFEntityDto.getQty());
                detail.put("FBaseUnitQty", salOutStockFEntityDto.getQty());
                detail.put("FPriceUnitQty", salOutStockFEntityDto.getQty());
                detail.put("FTaxNetPrice", salOutStockFEntityDto.getPrice());
                detail.put("FSALBASEQTY", salOutStockFEntityDto.getQty());
                detail.put("FSALUNITQTY", salOutStockFEntityDto.getQty());
                detail.put("FPRICEBASEQTY", salOutStockFEntityDto.getQty());
                // 是否赠品
                detail.put("FIsFree", salOutStockFEntityDto.getPrice().compareTo(BigDecimal.ZERO) == 0 ? 1 : 0);
                detail.put("FPrice", salOutStockFEntityDto.getPrice());
                detail.put("FTaxPrice", salOutStockFEntityDto.getPrice());
                detail.put("FAmount", new BigDecimal(salOutStockFEntityDto.getQty()).multiply(salOutStockFEntityDto.getPrice()));
                detail.put("FBefDisAllAmt", new BigDecimal(salOutStockFEntityDto.getQty()).multiply(salOutStockFEntityDto.getPrice()));
                detail.put("FEntrynote", salOutStockFEntityDto.getEntryNote());
                entity.add(detail);
            }
        }
        model.put("FEntity", entity);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        System.out.println(result);
        return result;
    }
}
