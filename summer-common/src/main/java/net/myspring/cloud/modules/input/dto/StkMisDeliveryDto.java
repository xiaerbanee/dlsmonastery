package net.myspring.cloud.modules.input.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 其他出库单
 * Created by lihx on 2017/5/17.
 */
public class StkMisDeliveryDto {
    //附加-业务系统单据Id
    private String extendId;
    //附加-单据类型
    private String extendType;
    //创建人
    private String creator;
    //单据类型
    private String misDeliveryType;
    //做单日期
    private LocalDate date;
    //库存方向
    private String FStockDirect;
    //领料部门
    private String FDeptIdNumber;

    private List<StkMisDeliveryFEntityDto> stkMisDeliveryFEntityDtoList = Lists.newArrayList();

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

    public String getMisDeliveryType() {
        return misDeliveryType;
    }

    public void setMisDeliveryType(String misDeliveryType) {
        this.misDeliveryType = misDeliveryType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getFStockDirect() {
        return FStockDirect;
    }

    public void setFStockDirect(String FStockDirect) {
        this.FStockDirect = FStockDirect;
    }

    public String getFDeptIdNumber() {
        return FDeptIdNumber;
    }

    public void setFDeptIdNumber(String FDeptIdNumber) {
        this.FDeptIdNumber = FDeptIdNumber;
    }

    public List<StkMisDeliveryFEntityDto> getStkMisDeliveryFEntityDtoList() {
        return stkMisDeliveryFEntityDtoList;
    }

    public void setStkMisDeliveryFEntityDtoList(List<StkMisDeliveryFEntityDto> stkMisDeliveryFEntityDtoList) {
        this.stkMisDeliveryFEntityDtoList = stkMisDeliveryFEntityDtoList;
    }

    @JsonIgnore
    public String getJson() {
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", getCreator());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FDate", getDate());
        model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "QTCKD01_SYS"));
        model.put("FStockOrgId", CollectionUtil.getMap("FNumber", "100"));
        model.put("FPickOrgId", CollectionUtil.getMap("FNumber", "100"));
        model.put("FOwnerIdHead", CollectionUtil.getMap("FNumber", "100"));
        //领料部门
        model.put("FDeptId", CollectionUtil.getMap("FNumber", getFDeptIdNumber()));
        //库存方向
        model.put("FStockDirect", getFStockDirect());
        model.put("FOwnerTypeIdHead", "BD_OwnerOrg");
        model.put("FBaseCurrId", CollectionUtil.getMap("FNumber", "PRE001"));
        List<Object> entity = Lists.newArrayList();
        for (StkMisDeliveryFEntityDto entityDto : getStkMisDeliveryFEntityDtoList()) {
            Map<String, Object> detail = Maps.newLinkedHashMap();
            detail.put("FMaterialId", CollectionUtil.getMap("FNumber", entityDto.getMaterialNumber()));
            detail.put("FStockId", CollectionUtil.getMap("FNumber", entityDto.getStockNumber()));
            detail.put("FUnitID", CollectionUtil.getMap("FNumber", "Pcs"));
            detail.put("FBaseUnitId", CollectionUtil.getMap("FNumber", "Pcs"));
            detail.put("FQty", entityDto.getQty());
            detail.put("FEntryNote", entityDto.getFEntryNote());
            entity.add(detail);
        }
        model.put("FEntity", entity);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        //System.out.println(result);
        return result;
    }

}
