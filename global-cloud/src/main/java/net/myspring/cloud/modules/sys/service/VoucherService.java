package net.myspring.cloud.modules.sys.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.DynamicDataSourceContext;
import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.common.enums.CharEnum;
import net.myspring.cloud.common.enums.VoucherStatusEnum;
import net.myspring.cloud.common.utils.HandSonTableUtils;
import net.myspring.cloud.common.utils.SecurityUtils;
import net.myspring.cloud.modules.kingdee.domain.BdAccount;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemGroup;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemProperty;
import net.myspring.cloud.modules.kingdee.dto.BdFlexItemGroupDto;
import net.myspring.cloud.modules.kingdee.mapper.BdAccountMapper;
import net.myspring.cloud.modules.kingdee.mapper.BdFlexItemMapper;
import net.myspring.cloud.modules.kingdee.model.GlVoucherModel;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.domain.Voucher;
import net.myspring.cloud.modules.sys.domain.VoucherEntry;
import net.myspring.cloud.modules.sys.domain.VoucherEntryFlow;
import net.myspring.cloud.modules.sys.dto.VoucherDto;
import net.myspring.cloud.modules.sys.dto.VoucherEntryDto;
import net.myspring.cloud.modules.sys.dto.VoucherEntryFlowDto;
import net.myspring.cloud.modules.sys.mapper.KingdeeBookMapper;
import net.myspring.cloud.modules.sys.mapper.VoucherEntryFlowMapper;
import net.myspring.cloud.modules.sys.mapper.VoucherEntryMapper;
import net.myspring.cloud.modules.sys.mapper.VoucherMapper;
import net.myspring.cloud.modules.sys.web.query.VoucherQuery;
import net.myspring.common.response.RestErrorField;
import net.myspring.common.response.RestResponse;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by lihx on 2017/4/5.
 */
@Service
@LocalDataSource
public class VoucherService {
    @Autowired
    private VoucherMapper voucherMapper;
    @Autowired
    private VoucherEntryMapper voucherEntryMapper;
    @Autowired
    private VoucherEntryFlowMapper voucherEntryFlowMapper;
    @Autowired
    private KingdeeBookMapper kingdeeBookMapper;
    @Autowired
    private SecurityUtils securityUtils;

    public Page<VoucherDto> findPage(Pageable pageable, VoucherQuery voucherQuery) {
        Page<Voucher> page = voucherMapper.findPage(pageable, voucherQuery);
        Page<VoucherDto> dictEnumDtoPage = BeanUtil.map(page,VoucherDto.class);
        return dictEnumDtoPage;
    }

    public VoucherDto findOne(String id) {
        Voucher voucher = voucherMapper.findOne(id);
        VoucherDto voucherDto = BeanUtil.map(voucher,VoucherDto.class);
        KingdeeBook kingdeeBook = kingdeeBookMapper.findOne(voucherDto.getCompanyId());
        voucherDto.setCompanyId(kingdeeBook.getCompanyId());
        List<VoucherEntry> voucherEntryList = voucherEntryMapper.findByGlVoucherId(voucherDto.getId());
        List<VoucherEntryDto> voucherEntryDtoList = BeanUtil.map(voucherEntryList,VoucherEntryDto.class);
        voucherDto.setVoucherEntryDtoList(voucherEntryDtoList);
        for(VoucherEntryDto voucherEntryDto : voucherEntryDtoList){
            List<VoucherEntryFlow> voucherEntryFlowList = voucherEntryFlowMapper.findByVoucherEntryId(voucherEntryDto.getId());
            List<VoucherEntryFlowDto> voucherEntryFlowDtoList = BeanUtil.map(voucherEntryFlowList,VoucherEntryFlowDto.class);
            voucherEntryDto.setVoucherEntryFlowDtoList(voucherEntryFlowDtoList);
        }
        return voucherDto;
    }

    //
    public Map<String,Object> getFormProperty(GlVoucherModel glVoucherModel,VoucherDto voucherDto) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("headers", getHeaders(glVoucherModel));
        if(voucherDto.getId() == null){
            map.put("data", Lists.newArrayList());
        }else {
            map.put("data", initData(glVoucherModel, voucherDto));
        }
        //科目列表（包含核算维度）
        map.put("bdVourchers", getBdVoucherList(glVoucherModel.getBdAccountList(),glVoucherModel.getBdFlexItemGroupDtoList()));
        return map;
    }

    //添加将headers页面的头部获取
    public List<String> getHeaders(GlVoucherModel glVoucherModel) {
        List<String> list = Lists.newLinkedList();
        list.add("摘要");
        list.add("科目名称");
        list.addAll(getBdFlexItemProperties(glVoucherModel.getBdFlexItemGroupDtoList()));
        list.add("借方金额");
        list.add("贷方金额");
        return list;
    }

    //查询所有使用中的核算维度
    public List<String> getBdFlexItemProperties(List<BdFlexItemGroupDto> bdFlexItemGroupList) {
        List<String> list = Lists.newLinkedList();
        for (BdFlexItemGroupDto bdFlexItemGroup : bdFlexItemGroupList) {
            for (String name : bdFlexItemGroup.getFNames()) {
                if (!list.contains(name)) {
                    list.add(name);
                }
            }
        }
        return list;
    }

    //将headers和data赋值
    public List<List<String>>  initData(GlVoucherModel glVoucherModel,VoucherDto voucherDto) {
        List<List<String>> datas = Lists.newArrayList();
        //所有科目
        Map<String, BdAccount> bdVoucherMap = CollectionUtil.extractToMap(glVoucherModel.getBdAccountList(), "fNumber");
        //所有核算维度
        Map<String, BdFlexItemProperty> bdFlexItemPropertyMap = CollectionUtil.extractToMap(glVoucherModel.getBdFlexItemPropertyList(), "fName");
        Map<String,BdFlexItemProperty> flexNumberMap = CollectionUtil.extractToMap(glVoucherModel.getBdFlexItemPropertyList(),"fFlexNumber");
        //所有使用的核算维度
        List<String> headerSubjects = getBdFlexItemProperties(glVoucherModel.getBdFlexItemGroupDtoList());
        List<String> headers = Lists.newLinkedList();
        for (String header : headerSubjects) {
            headers.add("FDetailID__" + bdFlexItemPropertyMap.get(header).getfFlexNumber());
        }
        //设置名称
        Map<String, Map<String, String>> map = glVoucherModel.getResult();
        Map<String, Map<String, String>> reverseMap = Maps.newHashMap();
        for(String key:map.keySet()) {
            reverseMap.put(key,Maps.<String, String>newHashMap());
            for(String tempKey:map.get(key).keySet()) {
                reverseMap.get(key).put(map.get(key).get(tempKey),tempKey);
            }
        }
        for (VoucherEntryDto voucherEntryDto : voucherDto.getVoucherEntryDtoList()) {
            List<String> list = Lists.newArrayList();
            list.add(voucherEntryDto.getfExplanation());
            BdAccount bdAccount = bdVoucherMap.get(voucherEntryDto.getfAccountId());
            list.add(bdAccount.getfNumber() + CharEnum.CHAR_SLASH_LINE.getValue() + bdAccount.getfName());
            Map<String, VoucherEntryFlowDto> voucherEntryFlowMap = Maps.newHashMap();
            List<VoucherEntryFlowDto> voucherEntryFlowDtoList = voucherEntryDto.getVoucherEntryFlowDtoList();
            if (CollectionUtil.isNotEmpty(voucherEntryFlowDtoList)) {
                voucherEntryFlowMap = CollectionUtil.extractToMap(voucherEntryFlowDtoList, "name");
                for(VoucherEntryFlowDto voucherEntryFlowDto: voucherEntryFlowDtoList) {
                    voucherEntryFlowDto.setFlexName(flexNumberMap.get(voucherEntryFlowDto.getFlexNumber()).getfName());
                    String value=reverseMap.get(voucherEntryFlowDto.getFlexName()).get(voucherEntryFlowDto.getCode());
                    voucherEntryFlowDto.setValue(voucherEntryFlowDto.getCode()+CharEnum.CHAR_SLASH_LINE.getValue()+value);
                }
            }
            for (String header : headers) {
                if (voucherEntryFlowMap.containsKey(header)) {
                    list.add(voucherEntryFlowMap.get(header).getValue());
                } else {
                    list.add("");
                }
            }
            if (voucherEntryDto.getfCredit() == null) {
                list.add(voucherEntryDto.getfDebit().toString());
                list.add("");
            } else {
                list.add("");
                list.add(voucherEntryDto.getfCredit().toString());
            }
            datas.add(list);
        }
        return datas;
    }

    //获取所有科目名称及其对应的核算维度
    public Map<String, List<String>> getBdVoucherList( List<BdAccount> bdAccountList , List<BdFlexItemGroupDto> bdFlexItemGroupDtoList) {
        Map<String, List<String>> result = Maps.newHashMap();
        Map<String, BdFlexItemGroupDto> map = CollectionUtil.extractToMap(bdFlexItemGroupDtoList, "fId");
        for (BdAccount bdAccount : bdAccountList) {
            if (!"0".equals(bdAccount.getfItemDetailId())) {
                result.put(bdAccount.getfNumber() + CharEnum.CHAR_SLASH_LINE.getValue() + bdAccount.getfName(), map.get(bdAccount.getfItemDetailId()).getFNames());
            }
        }
        return result;
    }

    public List<RestErrorField> check(List<List<Object>> datas, GlVoucherModel glVoucherModel) {
        List<RestErrorField> restErrorFieldList = Lists.newArrayList();
        //所有科目
        Map<String, List<String>> map = getBdVoucherList(glVoucherModel.getBdAccountList(),glVoucherModel.getBdFlexItemGroupDtoList());
        List<String> header = getHeaders(glVoucherModel);
        BigDecimal debitAmount = BigDecimal.ZERO;
        BigDecimal creditAmount = BigDecimal.ZERO;
        for (int i = 0; i < datas.size(); i++) {
            int index = i + 1;
            List<Object> row = datas.get(i);
            //科目
            String subject = HandSonTableUtils.getValue(row,1);
            if (map.containsKey(subject)) {
                //科目对应核算维度
                List<String> bdFlexitemproperty = map.get(subject);
                for (int j = 2; j < row.size() - 2; j++) {
                    String value = HandSonTableUtils.getValue(row,j);
                    String subjectName = header.get(j);
                    if (StringUtils.isBlank(value)) {
                        if (bdFlexitemproperty.contains(subjectName)) {
                            restErrorFieldList.add(new RestErrorField("第" + index + "行，" + subjectName + "为必填<br/>",null,null));
                        }
                    } else {
                        if (!bdFlexitemproperty.contains(subjectName)) {
                            restErrorFieldList.add(new RestErrorField("第" + index + "行，" + subjectName + "必须为空<br/>",null,null));
                        }
                    }
                }
            }
            //借方金额
            String debitStr = HandSonTableUtils.getValue(row,header.size() - 2);
            if(StringUtils.isNotEmpty(debitStr)){
                debitAmount = debitAmount.add(new BigDecimal(debitStr));
            }
            //贷方金额
            String creditStr = HandSonTableUtils.getValue(row,header.size() - 1);
            if(StringUtils.isNotEmpty(creditStr)){
                creditAmount = creditAmount.add(new BigDecimal(creditStr));
            }
            if ((StringUtils.isBlank(debitStr) && StringUtils.isBlank(creditStr)) || (StringUtils.isNotBlank(debitStr) && StringUtils.isNotBlank(creditStr))) {
                restErrorFieldList.add(new RestErrorField("第" + index + "行，借方和贷方请填写一项！<br/>",null,null));
            }
        }
        if (debitAmount.compareTo(creditAmount) != 0) {
            restErrorFieldList.add(new RestErrorField("借贷方金额必须相等",null,null));
        }
        return restErrorFieldList;
    }

    @Transactional
    public Voucher save(List<List<Object>> datas,VoucherDto voucherDto,GlVoucherModel glVoucherModel) {
        Boolean isCreate = StringUtils.isBlank(voucherDto.getId());
        if (isCreate) {
            voucherDto.setCompanyId(DynamicDataSourceContext.get().getCompanyId());
            //待写accountName；
            voucherDto.setCreatedName(securityUtils.getAccountId());
            if(securityUtils.getAccountId() != null){//问题ThreadLocalContext.get().getAccount() != null
                voucherDto.setStatus(VoucherStatusEnum.省公司财务审核.name());
            }else{
                voucherDto.setStatus(VoucherStatusEnum.地区财务审核.name());
            }
            voucherMapper.save(voucherDto);
        } else {
            LocalDate date = voucherDto.getfDate();
            voucherDto = findOne(voucherDto.getId());
            voucherDto.setfDate(date);
            //删除原有数据
            if (CollectionUtil.isNotEmpty(voucherDto.getVoucherEntryDtoList())) {
                for (VoucherEntryDto voucherEntryDto : voucherDto.getVoucherEntryDtoList()) {
                    if (CollectionUtil.isNotEmpty(voucherEntryDto.getVoucherEntryFlowDtoList())) {
                        voucherEntryFlowMapper.deleteByIds(voucherEntryDto.getVoucherEntryFlowIdList());
                    }
                }
                voucherEntryMapper.deleteByIds(voucherDto.getVoucherEntryIdList());
                voucherDto.setVoucherEntryDtoList(Lists.<VoucherEntryDto>newArrayList());
            }
            voucherMapper.update(voucherDto);
        }
        List<String> headers = getHeaders(glVoucherModel);
        //核算维度分组
        List<BdFlexItemProperty> bdFlexItemPropertyList = glVoucherModel.getBdFlexItemPropertyList();
        Map<String, BdFlexItemProperty> bdFlexItemPropertyMap = CollectionUtil.extractToMap(bdFlexItemPropertyList, "fName");
        for (List<Object> row : datas) {
            VoucherEntry voucherEntry = new VoucherEntry();
            //摘要
            voucherEntry.setfExplanation(HandSonTableUtils.getValue(row,0));
            String FAcctFullName = HandSonTableUtils.getValue(row,1);
            //科目编码
            voucherEntry.setfAccountId(FAcctFullName.substring(0,FAcctFullName.indexOf(CharEnum.CHAR_SLASH_LINE.getValue())));
            //借方
            String debitStr = HandSonTableUtils.getValue(row,headers.size() - 2);
            //贷方
            String creditStr = HandSonTableUtils.getValue(row,headers.size() - 1);
            if (StringUtils.isNoneEmpty(debitStr)) {
                voucherEntry.setfDebit(new BigDecimal(debitStr));
            } else {
                voucherEntry.setfCredit(new BigDecimal(creditStr));
            }
            voucherEntry.setGlVoucherId(voucherDto.getId());
            voucherEntryMapper.save(voucherEntry);
            //核算维度明细
            List<VoucherEntryFlow> voucherEntryFlowList = Lists.newArrayList();
            for (int i = 2; i < row.size() - 2; i++) {
                String value = HandSonTableUtils.getValue(row,i);
                String header = headers.get(i);
                if (StringUtils.isNotBlank(value)) {
                    String name = "FDetailID__" + bdFlexItemPropertyMap.get(header).getfFlexNumber();
                    VoucherEntryFlow voucherEntryFlow = new VoucherEntryFlow();
                    voucherEntryFlow.setName(name);
                    voucherEntryFlow.setValue(value.substring(value.indexOf(CharEnum.CHAR_SLASH_LINE.getValue())+1));
                    voucherEntryFlow.setCode(value.substring(0,value.indexOf(CharEnum.CHAR_SLASH_LINE.getValue())));
                    voucherEntryFlow.setGlVoucherEntryId(voucherEntry.getId());
                    voucherEntryFlowList.add(voucherEntryFlow);
                }
            }
            //页面传送voucherDto，还是voucher
//            if(CollectionUtil.isNotEmpty(voucherEntryFlowList)){
//                voucherEntryFlowMapper.batchSave(voucherEntryFlowList);
//               voucherEntry.setK3cloudGlVoucherEntryFlowList(voucherEntryFlowList);
//            }
//            voucherEntryMapper.update(voucherEntry);
//            voucherDto.getVoucherEntryDtoList().add(voucherEntry);
        }
        voucherMapper.update(voucherDto);
        return voucherDto;
    }
}
