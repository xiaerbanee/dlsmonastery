package net.myspring.cloud.modules.input.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.enums.SalReturnStockBillTypeEnum;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 销售退货单
 * Created by liuj on 2017/5/11.
 */
public class SalReturnStockDto {
    //附加-业务系统单据Id
    private String extendId;
    //附加-单据类型
    private String extendType;
    //单据类型
    private String billType;
    //创建人
    private String creator;
    // 客户编码
    private String customerNumber;
    // 日期
    private LocalDate date;
    // 备注
    private String note;
    //部门名称
    private String departmentNumber;
    //单据类型编码
    private String FBillTypeIdNumber;

    private List<SalReturnStockFEntityDto> salReturnStockFEntityDtoList = Lists.newArrayList();

    public String getExtendId() {
        return extendId;
    }

    public void setExtendId(String extendId) {
        this.extendId = extendId;
    }

    public String getExtendType() {
        if (extendType == null){
            return getBillType()+"K3";
        }
        return extendType;
    }

    public void setExtendType(String extendType) {
        this.extendType = extendType;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

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

    public String getFBillTypeIdNumber() {
        return FBillTypeIdNumber;
    }

    public void setFBillTypeIdNumber(String FBillTypeIdNumber) {
        this.FBillTypeIdNumber = FBillTypeIdNumber;
    }

    public List<SalReturnStockFEntityDto> getSalReturnStockFEntityDtoList() {
        return salReturnStockFEntityDtoList;
    }

    public void setSalReturnStockFEntityDtoList(List<SalReturnStockFEntityDto> salReturnStockFEntityDtoList) {
        this.salReturnStockFEntityDtoList = salReturnStockFEntityDtoList;
    }

    @JsonIgnore
    public String getJson() {
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", getCreator());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FDate", getDate());
        //单据类型
        model.put("FBillTypeID", CollectionUtil.getMap("FNumber", getFBillTypeIdNumber()));
        model.put("FStockDeptId", CollectionUtil.getMap("FNumber", getDepartmentNumber()));
        model.put("FSaledeptid", CollectionUtil.getMap("FNumber", getDepartmentNumber()));
        model.put("FSaleOrgId", CollectionUtil.getMap("FNumber", 100));
        model.put("FStockOrgId", CollectionUtil.getMap("FNumber", 100));
        model.put("FOwnerIdHead", CollectionUtil.getMap("FNumber", 100));
        model.put("FRetcustId", CollectionUtil.getMap("FNumber", getCustomerNumber()));
        model.put("FHeadNote", getNote());
        List<Object> entity = Lists.newArrayList();
        for (SalReturnStockFEntityDto batchBillDetail : getSalReturnStockFEntityDtoList()) {
            if (batchBillDetail.getQty() != null && batchBillDetail.getQty() > 0) {
                Map<String, Object> detail = Maps.newLinkedHashMap();
                detail.put("FMaterialId", CollectionUtil.getMap("FNumber", batchBillDetail.getMaterialNumber()));
                detail.put("FStockId", CollectionUtil.getMap("FNumber", batchBillDetail.getStockNumber()));
                //库存状态--可用
                detail.put("FStockStatusID", CollectionUtil.getMap("FNumber", "KCZT01_SYS"));
                //库存单位--Pcs
                detail.put("FUnitID", CollectionUtil.getMap("FNumber", "Pcs"));
                detail.put("FRealQty", batchBillDetail.getQty());
                detail.put("FBaseunitQty", batchBillDetail.getQty());
                detail.put("FTaxPrice", batchBillDetail.getPrice());
                detail.put("FPrice", batchBillDetail.getPrice());
                detail.put("FPriceUnitQty", batchBillDetail.getQty());
                detail.put("FTaxNetPrice", batchBillDetail.getPrice());
                detail.put("FAmount", new BigDecimal(batchBillDetail.getQty()).multiply(batchBillDetail.getPrice()));
                detail.put("FAmount_LC", new BigDecimal(batchBillDetail.getQty()).multiply(batchBillDetail.getPrice()));
                detail.put("FAllAmount", new BigDecimal(batchBillDetail.getQty()).multiply(batchBillDetail.getPrice()));
                detail.put("FAllAmount_LC", new BigDecimal(batchBillDetail.getQty()).multiply(batchBillDetail.getPrice()));
                detail.put("FSalBaseQty", batchBillDetail.getQty());
                detail.put("FSalUnitQty", batchBillDetail.getQty());
                detail.put("FPriceBaseQty", batchBillDetail.getQty());
                detail.put("Fnote", batchBillDetail.getEntryNote());
                entity.add(detail);
            }
        }
        model.put("FEntity", entity);
        Map<String, Object> subHeadEntity = Maps.newLinkedHashMap();
        subHeadEntity.put("FExchangeRate", 1);
        subHeadEntity.put("FLocalCurrId", CollectionUtil.getMap("FNumber", "PRE001"));
        subHeadEntity.put("FExchangeTypeId", CollectionUtil.getMap("FNumber", "HLTX01_SYS"));
        //结算币别
        subHeadEntity.put("FSettleCurrId", CollectionUtil.getMap("FNumber", "PRE001"));
        model.put("SubHeadEntity", subHeadEntity);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        //System.out.println(result);
        return result;
    }
}
