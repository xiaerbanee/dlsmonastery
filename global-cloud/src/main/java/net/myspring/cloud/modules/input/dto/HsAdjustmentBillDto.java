package net.myspring.cloud.modules.input.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.time.LocalDateUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 成本调整单
 */
public class HsAdjustmentBillDto {
    //附加-业务系统单据Id
    private String extendId;
    //附加-单据类型
    private String extendType;
    //创建人
    private String creator;
    //日期
    private LocalDate date;

    private List<HsAdjustmentBillEntityDto> entityDtoList = Lists.newArrayList();

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

    public List<HsAdjustmentBillEntityDto> getEntityDtoList() {
        return entityDtoList;
    }

    public void setEntityDtoList(List<HsAdjustmentBillEntityDto> entityDtoList) {
        this.entityDtoList = entityDtoList;
    }

    @JsonIgnore
    public String getJson(){
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", getCreator());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        //单据类型-入库成本调整单
        model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "CGRKTZD01_SYS"));
        //业务类型-入库调整
        model.put("FBusinessType", 1);
        //业务日期
        model.put("FDate", LocalDateUtils.format(getDate(),"yyyy-M-d"));
        //核算体系编码-财务会计核算体系
        model.put("FACCTGSYSTEMID", CollectionUtil.getMap("FNumber", "KJHSTX01_SYS"));
        //核算组织编码-账套名
        model.put("FAcctOrgID", CollectionUtil.getMap("FNumber", "100"));
        //会计政策编码-中国准则会计政策
        model.put("FACCTPOLICYID", CollectionUtil.getMap("FNumber", "KJZC01_SYS"));
        //调整原因-手工调差
        model.put("FAdjustmentReason", "手工调差");
        //创建组织
        model.put("FCreateOrgId", CollectionUtil.getMap("FNumber", "100"));
        List<Object> entity = Lists.newArrayList();
        for (HsAdjustmentBillEntityDto entityDto : getEntityDtoList()) {
            Map<String, Object> detail = Maps.newLinkedHashMap();
            //库存组织-账号名
            detail.put("FStockOrgID", CollectionUtil.getMap("FNumber","100"));
            //物料编码
            detail.put("FMaterialID", CollectionUtil.getMap("FNumber",entityDto.getMaterialNumber()));
            //库存状态-可用
            detail.put("FSTOCKSTATUSID", CollectionUtil.getMap("FNumber","KCZT01_SYS"));
            //调整金额
            detail.put("FAdjustmentAMOUNT", entityDto.getAdjustmentAmount());
            //仓库编码
            detail.put("FStockID", CollectionUtil.getMap("FNumber", entityDto.getStockNumber()));
            //货主-账号名
            detail.put("FOwnerID", CollectionUtil.getMap("FNumber", "100"));
            entity.add(detail);
        }
        model.put("FEntity", entity);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        //System.out.println(result);
        return result;
    }
}
