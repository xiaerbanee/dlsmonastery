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
 * 采购退料
 * Created by lihx on 2017/6/13.
 */
public class PurMrbDto {
    //创建人
    private String creator;
    // 供应商编码
    private String supplierNumber;
    // 日期
    private LocalDate date;
    //部门编码
    private String departmentNumber;

    private List<PurMrbFEntityDto> entityDtoList = Lists.newArrayList();

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

    public List<PurMrbFEntityDto> getEntityDtoList() {
        return entityDtoList;
    }

    public void setEntityDtoList(List<PurMrbFEntityDto> entityDtoList) {
        this.entityDtoList = entityDtoList;
    }

    @JsonIgnore
    public String getJson(){
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", getCreator());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "TLD01_SYS"));
        model.put("FDate", LocalDateUtils.format(getDate(),"yyyy-M-d"));
        model.put("FBusinessType", "CG");
        model.put("FMRTYPE", "B");
        model.put("FMRMODE", "A");
        model.put("FREPLENISHMODE", "B");
        model.put("FStockOrgId", CollectionUtil.getMap("FNumber", "100"));
        model.put("FRequireOrgId", CollectionUtil.getMap("FNumber", "100"));
        model.put("FPurchaseOrgId", CollectionUtil.getMap("FNumber", "100"));
        model.put("FMRDeptId", CollectionUtil.getMap("FNumber", getDepartmentNumber()));
        model.put("FPURCHASEDEPTID", CollectionUtil.getMap("FNumber", getDepartmentNumber()));
        model.put("FSupplierID", CollectionUtil.getMap("FNumber", getSupplierNumber()));
        model.put("FDESCRIPTION", "接口");
        List<Object> entity = Lists.newArrayList();
        for (PurMrbFEntityDto entityDto : getEntityDtoList()) {
            Map<String, Object> detail = Maps.newLinkedHashMap();
            detail.put("FMaterialId", CollectionUtil.getMap("FNumber", entityDto.getMaterialNumber()));
            detail.put("FRMREALQTY", entityDto.getQty());
            detail.put("FREPLENISHQTY", entityDto.getQty());
            detail.put("FKEAPAMTQTY", entityDto.getQty());
            detail.put("FPRICEUNITQTY", entityDto.getQty());
            detail.put("FStockId", CollectionUtil.getMap("FNumber", entityDto.getStockNumber()));
            detail.put("FStockStatusId", CollectionUtil.getMap("FNumber", "KCZT01_SYS"));
            detail.put("FPrice", entityDto.getPrice());
            detail.put("FAmount_LC", new BigDecimal(entityDto.getQty()).multiply(entityDto.getPrice()));
            detail.put("FCarryQty", entityDto.getQty());
            // 基本补料数量
            detail.put("FBaseReplayQty", entityDto.getQty());
            // 库存基本数量
            detail.put("FBASEUNITQTY", entityDto.getQty());
            // 扣款数量（基本单位）
            detail.put("FBaseKeapamtQty", entityDto.getQty());
            // 计价基本数量
            detail.put("FPriceBaseQty", entityDto.getQty());
            // 采购基本数量
            detail.put("FCarryBaseQty", entityDto.getQty());
            // 含税单价
            detail.put("FTAXPRICE", entityDto.getPrice());
            // 净价
            detail.put("FTAXNETPRICE", entityDto.getPrice());
            detail.put("FAmount", new BigDecimal(entityDto.getQty()).multiply(entityDto.getPrice()));
            detail.put("FALLAMOUNT", new BigDecimal(entityDto.getQty()).multiply(entityDto.getPrice()));
            detail.put("FAmount", new BigDecimal(entityDto.getQty()).multiply(entityDto.getPrice()));
            // 是否赠品
            detail.put("FGiveAway", entityDto.getPrice().compareTo(BigDecimal.ZERO) == 0 ? 1 : 0);
            detail.put("FExchangeTypeId", CollectionUtil.getMap("FNumber", "HLTX01_SYS"));
            detail.put("FLocalCurrId", CollectionUtil.getMap("FNumber", "PRE001"));
            detail.put("FNOTE", entityDto.getNote());
            detail.put("FPoRequireOrgId", CollectionUtil.getMap("FNumber", "100"));
            detail.put("FSetPriceUnitID", CollectionUtil.getMap("FNumber", "Pcs"));
            entity.add(detail);
        }
        model.put("FPURMRBENTRY", entity);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        System.out.println(result);
        return result;
    } 
}
