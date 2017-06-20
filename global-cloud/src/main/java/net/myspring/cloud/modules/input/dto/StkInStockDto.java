package net.myspring.cloud.modules.input.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.time.LocalDateUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 采购入库
 * Created by lihx on 2017/6/13.
 */
public class StkInStockDto {
    //创建人
    private String creator;
    //供应商编码
    private String supplierNumber;
    // 日期
    private LocalDate date;
    //部门编码
    private String departmentNumber;

    private List<StkInStockFEntryDto> stkInStockFEntryDtoList = Lists.newArrayList();

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSupplierNumber() {
        return supplierNumber;
    }

    public void setSupplierNumber(String supplierNumber) {
        this.supplierNumber = supplierNumber;
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

    public List<StkInStockFEntryDto> getStkInStockFEntryDtoList() {
        return stkInStockFEntryDtoList;
    }

    public void setStkInStockFEntryDtoList(List<StkInStockFEntryDto> stkInStockFEntryDtoList) {
        this.stkInStockFEntryDtoList = stkInStockFEntryDtoList;
    }

    @JsonIgnore
    public String getJson(){
        List<Object> purMrbEntry = Lists.newArrayList();
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", getCreator());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "RKD01_SYS"));
        model.put("FDate",  LocalDateUtils.format(getDate(),"yyyy-M-d"));
        model.put("FBusinessType", "CG");
        model.put("FStockOrgId", CollectionUtil.getMap("FNumber", "100"));
        model.put("FDemandOrgId", CollectionUtil.getMap("FNumber", "100"));
        model.put("FPurchaseOrgId", CollectionUtil.getMap("FNumber", "100"));
        model.put("FPurchaseDeptId", CollectionUtil.getMap("FNumber", getDepartmentNumber()));
        model.put("FStockDeptId", CollectionUtil.getMap("FNumber", getDepartmentNumber()));
        model.put("FSupplierId", CollectionUtil.getMap("FNumber", getSupplierNumber()));
        List<Object> entity = Lists.newArrayList();
        for (StkInStockFEntryDto entry : getStkInStockFEntryDtoList()){
            Map<String, Object> detail = Maps.newLinkedHashMap();
            detail.put("FMaterialId", CollectionUtil.getMap("FNumber", entry.getMaterialNumber()));
            //计价单位
            detail.put("FPriceUnitID", CollectionUtil.getMap("FNumber", "Pcs"));
            //库存单位
            detail.put("FUnitID", CollectionUtil.getMap("FNumber", "Pcs"));
            //采购单位
            detail.put("FRemainInStockUnitId", CollectionUtil.getMap("FNumber", "Pcs"));
            detail.put("FRealQty", entry.getQty());
            detail.put("FPriceUnitQty", entry.getQty());
            detail.put("FBaseUnitQty", entry.getQty());
            detail.put("FRemainInStockBaseQty", entry.getQty());
            detail.put("FPriceBaseQty", entry.getQty());
            detail.put("FRemainInStockQty", entry.getQty());
            detail.put("FTaxNetPrice", entry.getPrice());
            detail.put("FTaxPrice", entry.getPrice());
            detail.put("FPrice", entry.getPrice());
            detail.put("FAmount", new BigDecimal(entry.getQty()).multiply(entry.getPrice()));
            detail.put("FStockId", CollectionUtil.getMap("FNumber", entry.getStockNumber()));
            detail.put("FStockStatusId", CollectionUtil.getMap("FNumber", "KCZT01_SYS"));
            // 是否赠品
            detail.put("FGiveAway", entry.getPrice().compareTo(BigDecimal.ZERO) == 0 ? 1 : 0);
            detail.put("FExchangeTypeId", CollectionUtil.getMap("FNumber", "HLTX01_SYS"));
            detail.put("FLocalCurrId", CollectionUtil.getMap("FNumber", "PRE001"));
            detail.put("FNote", entry.getNote());
            entity.add(detail);
        }
        model.put("FInStockEntry", entity);
        root.put("Model", model);
        String result =  ObjectMapperUtils.writeValueAsString(root);
        System.out.println(result);
        return result;
    }
}
