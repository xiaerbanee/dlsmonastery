package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.common.utils.HandsontableUtils;
import net.myspring.cloud.modules.input.dto.GlVoucherDto;
import net.myspring.cloud.modules.input.dto.GlVoucherFEntityDto;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemGroup;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemProperty;
import net.myspring.cloud.modules.kingdee.domain.GlVoucher;
import net.myspring.cloud.modules.kingdee.repository.GlVoucherRepository;
import net.myspring.cloud.modules.sys.domain.*;
import net.myspring.cloud.modules.sys.web.form.VoucherForm;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.dto.NameValueDto;
import net.myspring.common.exception.ServiceException;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 凭证录入
 * Created by lihx on 2017/6/26.
 */
@Service
@KingdeeDataSource
@Transactional(readOnly = true)
public class GlVoucherService {
    @Autowired
    private KingdeeManager kingdeeManager;
    @Autowired
    private GlVoucherRepository glVoucherRepository;

    public GlVoucher findByBillNo(String billNo){
        return glVoucherRepository.findByBillNo(billNo);
    }

    private KingdeeSynDto save(GlVoucherDto glVoucherDto, KingdeeBook kingdeeBook){
        KingdeeSynDto kingdeeSynDto = new KingdeeSynDto(
                glVoucherDto.getExtendId(),
                glVoucherDto.getExtendType(),
                KingdeeFormIdEnum.GL_VOUCHER.name(),
                glVoucherDto.getJson(),
                kingdeeBook) {
        };
        kingdeeManager.save(kingdeeSynDto);
        if (!kingdeeSynDto.getSuccess()){
            throw new ServiceException("凭证录入失败："+kingdeeSynDto.getResult());
        }
        return kingdeeSynDto;
    }

    @Transactional
    public KingdeeSynDto save(GlVoucherDto glVoucherDto, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        KingdeeSynDto kingdeeSynDto;
        Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
        if(isLogin) {
            kingdeeSynDto = save(glVoucherDto, kingdeeBook);
        }else{
            kingdeeSynDto = new KingdeeSynDto(false,"未登入金蝶系统");
        }
        return kingdeeSynDto;
    }

    @Transactional
    public KingdeeSynDto save(VoucherForm voucherForm,List<BdFlexItemGroup> bdFlexItemGroupList,List<BdFlexItemProperty> bdFlexItemPropertyList, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        LocalDate date = voucherForm.getFdate();
        String json = HtmlUtils.htmlUnescape(voucherForm.getJson());
        List<List<Object>> data = ObjectMapperUtils.readValue(json, ArrayList.class);
        Map<String,BdFlexItemProperty> bdFlexItemPropertyNameMap = bdFlexItemPropertyList.stream().collect(Collectors.toMap(BdFlexItemProperty::getFName, BdFlexItemProperty->BdFlexItemProperty));
        List<GlVoucherFEntityDto> glVoucherFEntityDtoList = Lists.newArrayList();
        for (List<Object> row : data){
            String explanation = HandsontableUtils.getValue(row,0);
            String accountNumberName = HandsontableUtils.getValue(row,1);
            String debitStr = HandsontableUtils.getValue(row,row.size()-2);
            String creditStr = HandsontableUtils.getValue(row,row.size()-1);
            List<String> headers = getFlexItemGroupAllName(bdFlexItemGroupList);
            //核算维度明细
            List<NameValueDto> nameValueDtoList = Lists.newArrayList();
            for (int i=2; i<row.size()-2; i++){
                String numberValue = HandsontableUtils.getValue(row,i);
                String header = headers.get(i-2);
                if (StringUtils.isNotBlank(numberValue)){
                    NameValueDto nameValue = new NameValueDto();
                    String name = "FDetailID__"+bdFlexItemPropertyNameMap.get(header).getFFlexNumber();
                    nameValue.setName(name);
                    nameValue.setValue(numberValue.split(CharConstant.SLASH_LINE)[0]);//科目编码
                    nameValueDtoList.add(nameValue);
                }
            }
            GlVoucherFEntityDto entityDto = new GlVoucherFEntityDto();
            entityDto.setExplanation(explanation);
            entityDto.setAccountNumber(accountNumberName.split(CharConstant.SLASH_LINE)[0]);
            if (StringUtils.isNoneEmpty(debitStr)){
                entityDto.setDebit(new BigDecimal(debitStr));
            }else {
                entityDto.setCredit(new BigDecimal(creditStr));
            }
            entityDto.setNameValueDtoList(nameValueDtoList);
            glVoucherFEntityDtoList.add(entityDto);
        }
        GlVoucherDto glVoucherDto = new GlVoucherDto();
        glVoucherDto.setCreator(accountKingdeeBook.getUsername());
        glVoucherDto.setDate(date);
        glVoucherDto.setExtendType(ExtendTypeEnum.凭证_K3.name());
        glVoucherDto.setGlVoucherFEntityDtoList(glVoucherFEntityDtoList);
        return save(glVoucherDto,kingdeeBook,accountKingdeeBook);
    }

    //核算维度组--所有不重复的核算名称
    public List<String> getFlexItemGroupAllName(List<BdFlexItemGroup> bdFlexItemGroupList) {
        List<String> list = Lists.newLinkedList();
        for (BdFlexItemGroup flexItemGroup : bdFlexItemGroupList) {
            for (String name : flexItemGroup.getFNames()) {
                if (!list.contains(name)) {
                    list.add(name);
                }
            }
        }
        return list;
    }
}
