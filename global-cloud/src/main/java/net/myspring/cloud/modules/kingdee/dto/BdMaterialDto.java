package net.myspring.cloud.modules.kingdee.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 物料批量添加
 * Created by lihx on 2017/8/1.
 */
public class BdMaterialDto {
    //附加-业务系统单据Id
    private String extendId;
    //附加-单据类型
    private String extendType;
    //创建人
    private String creator;
    //名称
    private String FName;
    //编码
    private String FNumber;
    //物料分组
    private String FGroupNumber;
    //存货类别
    private String FCategoryNumber;
    //一级价
    private BigDecimal price1;
    //广告让利
    private BigDecimal rlPrice;

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

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getFNumber() {
        return FNumber;
    }

    public void setFNumber(String FNumber) {
        this.FNumber = FNumber;
    }

    public String getFGroupNumber() {
        return FGroupNumber;
    }

    public void setFGroupNumber(String FGroupNumber) {
        this.FGroupNumber = FGroupNumber;
    }

    public String getFCategoryNumber() {
        return FCategoryNumber;
    }

    public void setFCategoryNumber(String FCategoryNumber) {
        this.FCategoryNumber = FCategoryNumber;
    }

    public BigDecimal getPrice1() {
        return price1;
    }

    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

    public BigDecimal getRlPrice() {
        return rlPrice;
    }

    public void setRlPrice(BigDecimal rlPrice) {
        this.rlPrice = rlPrice;
    }

    @JsonIgnore
    public String getJson() {
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", getCreator());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FName", getFName());
        model.put("FNumber", getFNumber());
        //一级单价
        model.put("F_yl_Decimal",getPrice1());
        //广告让利
        model.put("F_yl_Decimal1",getRlPrice());
        Map<String, Object> detail = Maps.newLinkedHashMap();
        //存货类别
        detail.put("FCategoryID", CollectionUtil.getMap("FNumber", getFCategoryNumber()));
        //物料分组
        detail.put("FMaterialGroup", CollectionUtil.getMap("FNumber", getFGroupNumber()));
        //默认税率--
        detail.put("FTaxRateId", CollectionUtil.getMap("FNumber", "SL04_SYS"));
        model.put("SubHeadEntity", detail);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        //System.out.println(result);
        return result;
    }
}
