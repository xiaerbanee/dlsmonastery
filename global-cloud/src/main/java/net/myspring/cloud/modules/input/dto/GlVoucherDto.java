package net.myspring.cloud.modules.input.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.modules.sys.dto.VoucherEntryFlowDto;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by lihx on 2017/6/26.
 */
public class GlVoucherDto {
    //附加-业务系统单据Id
    private String extendId;
    //附加-单据类型
    private String extendType;
    //创建人
    private String creator;
    //
    private LocalDate date;
    
    private List<GlVoucherFEntityDto> glVoucherFEntityDtoList = Lists.newArrayList();

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


    public List<GlVoucherFEntityDto> getGlVoucherFEntityDtoList() {
        return glVoucherFEntityDtoList;
    }

    public void setGlVoucherFEntityDtoList(List<GlVoucherFEntityDto> glVoucherFEntityDtoList) {
        this.glVoucherFEntityDtoList = glVoucherFEntityDtoList;
    }

    @JsonIgnore
    public String getJson() {
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", getCreator());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FVOUCHERID", 0);
        //账簿--
        model.put("FAccountBookID", CollectionUtil.getMap("FNumber", "001"));
        model.put("FDate", getDate());
        model.put("FYEAR", LocalDateUtils.format(getDate(),"yyyy"));
        model.put("FPERIOD",LocalDateUtils.format(getDate(),"M"));
        //来源系统--
        model.put("FSystemID", CollectionUtil.getMap("FNumber", "gl"));
        //凭证字--
        model.put("FVOUCHERGROUPID", CollectionUtil.getMap("FNumber", "PRE001"));

        List<Object> entity = Lists.newArrayList();
        for(GlVoucherFEntityDto entityDto :getGlVoucherFEntityDtoList()){
            Map<String, Object> detail = Maps.newLinkedHashMap();
            //摘要
            detail.put("FEXPLANATION", entityDto.getExplanation());
            //科目编码
            detail.put("FACCOUNTID", CollectionUtil.getMap("FNumber", entityDto.getAccountNumber()));
            //核算维度
            Map<String, Object> FDetailID = Maps.newLinkedHashMap();
            for(VoucherEntryFlowDto flowDto:entityDto.getVoucherEntryFlowDtoList()){
                FDetailID.put(flowDto.getName(),  flowDto.getCode());
            }
//            FDetailID.put("FDETAILID__FF100002","");//其他类
//            FDetailID.put("FDETAILID__FF100003","");//管理费用
//            FDetailID.put("FDETAILID__FF100004","");//银行账号
//            FDetailID.put("FDETAILID__FFLEX4","");//供应商
//            FDetailID.put("FDETAILID__FFLEX5","");//供应商
//            FDetailID.put("FDETAILID__FFLEX6","");//客户
//            FDetailID.put("FDETAILID__FFLEX7","");//员工
//            FDetailID.put("FDETAILID__FFLEX8","");//物料
//            FDetailID.put("FDETAILID__FFLEX9","");//费用项目
//            FDetailID.put("FDETAILID__FFLEX10","");//资产类别
//            FDetailID.put("FDETAILID__FFLEX11","");//组织机构
//            FDetailID.put("FDETAILID__FOPCODE","");//
            detail.put("FDetailID", FDetailID);
            //贷方总金额
            detail.put("FCREDIT", entityDto.getCredit());
            //借方总金额
            detail.put("FDEBIT", entityDto.getDebit());
            entity.add(detail);
        }
        model.put("FEntity", entity);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        return result;
    }
}
