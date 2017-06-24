package net.myspring.cloud.modules.input.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 直接调拨单
 * Created by lihx on 2017/6/22.
 */
public class StkTransferDirectDto {
    //附加-业务系统单据Id
    private String extendId;
    //附加-单据类型
    private String extendType;
    //创建人
    private String creator;
    // 日期
    private LocalDate date;
    // 备注
    private String note;
    
    private List<StkTransferDirectFBillEntryDto> stkTransferDirectFBillEntryDtoList = Lists.newArrayList();

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<StkTransferDirectFBillEntryDto> getStkTransferDirectFBillEntryDtoList() {
        return stkTransferDirectFBillEntryDtoList;
    }

    public void setStkTransferDirectFBillEntryDtoList(List<StkTransferDirectFBillEntryDto> stkTransferDirectFBillEntryDtoList) {
        this.stkTransferDirectFBillEntryDtoList = stkTransferDirectFBillEntryDtoList;
    }

    @JsonIgnore
    public String getJson() {
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", getCreator());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "ZJDB01_SYS"));
        model.put("FTransferDirect", "GENERAL");
        model.put("FDate", LocalDateUtils.format(getDate(),"yyyy-MM-dd"));
        model.put("FTransferBizType", "InnerOrgTransfer");
        model.put("FStockOutOrgId", CollectionUtil.getMap("FNumber", 100));
        model.put("FOwnerTypeOutIdHead", "BD_OwnerOrg");
        model.put("FOwnerOutIdHead", CollectionUtil.getMap("FNumber", 100));
        model.put("FStockOrgId", CollectionUtil.getMap("FNumber", 100));
        model.put("FOwnerTypeIdHead", "BD_OwnerOrg");
        model.put("FOwnerIdHead", CollectionUtil.getMap("FNumber", 100));
        model.put("FBaseCurrId", CollectionUtil.getMap("FNumber", "PRE001"));
        model.put("FSETTLECURRID", CollectionUtil.getMap("FNumber", "PRE001"));
        model.put("FExchangeRate", 1);
        model.put("FExchangeTypeId", CollectionUtil.getMap("FNumber", "HLTX01_SYS"));
        model.put("FNote", getNote());
        List<Object> entity = Lists.newArrayList();
        for (StkTransferDirectFBillEntryDto billEntryDto : getStkTransferDirectFBillEntryDtoList()) {
            if (billEntryDto.getQty() != null && billEntryDto.getQty() > 0) {
                Map<String, Object> detail = Maps.newLinkedHashMap();
                detail.put("FMaterialId", CollectionUtil.getMap("FNumber", billEntryDto.getMaterialNumber()));
                detail.put("FSrcStockId", CollectionUtil.getMap("FNumber", billEntryDto.getSrcStockNumber()));
                detail.put("FDestStockId", CollectionUtil.getMap("FNumber", billEntryDto.getDestStockNumber()));
                detail.put("FQty", billEntryDto.getQty());
                detail.put("FBaseQty", billEntryDto.getQty());
                detail.put("FSaleQty", billEntryDto.getQty());
                detail.put("FSalBaseQty", billEntryDto.getQty());
                detail.put("FPriceBaseQty", billEntryDto.getQty());
                detail.put("FPriceQty", billEntryDto.getQty());
                detail.put("FSrcStockStatusId", CollectionUtil.getMap("FNumber", "KCZT01_SYS"));
                detail.put("FDestStockStatusId", CollectionUtil.getMap("FNumber", "KCZT01_SYS"));
                detail.put("FNoteEntry", "api接口");
                entity.add(detail);
            }
        }
        model.put("FBillEntry", entity);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        //System.out.println(result);
        return result;
    }
}
