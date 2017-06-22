package net.myspring.cloud.modules.sys.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdAccount;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemGroup;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemProperty;
import net.myspring.cloud.modules.sys.domain.Voucher;
import net.myspring.cloud.modules.sys.domain.VoucherEntry;
import net.myspring.cloud.modules.sys.domain.VoucherEntryFlow;
import net.myspring.cloud.modules.sys.dto.VoucherDto;
import net.myspring.cloud.modules.sys.dto.VoucherModel;
import net.myspring.cloud.modules.sys.repository.VoucherEntryFlowRepository;
import net.myspring.cloud.modules.sys.repository.VoucherEntryRepository;
import net.myspring.cloud.modules.sys.repository.VoucherRepository;
import net.myspring.cloud.modules.sys.web.form.VoucherForm;
import net.myspring.cloud.modules.sys.web.query.VoucherQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.response.RestResponse;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 凭证
 * Created by lihx on 2017/4/5.
 */
@Service
@LocalDataSource
public class VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private VoucherEntryRepository voucherEntryRepository;
    @Autowired
    private VoucherEntryFlowRepository voucherEntryFlowRepository;

    public Page<VoucherDto> findPage(Pageable pageable, VoucherQuery voucherQuery) {
        Page<VoucherDto> page = voucherRepository.findPage(pageable, voucherQuery);
        return page;
    }

    public VoucherDto findOne(String id) {
        VoucherDto voucherDto;
        if (StringUtils.isBlank(id)) {
            voucherDto = new VoucherDto();
        } else {
            Voucher voucher = voucherRepository.findOne(id);
            voucherDto = BeanUtil.map(voucher,VoucherDto.class);
        }
        return voucherDto;
    }

    public RestResponse save(VoucherForm voucherForm){
        LocalDate date = voucherForm.getBillDate();
        String json = HtmlUtils.htmlUnescape(voucherForm.getJson());
        List<List<Object>> data = ObjectMapperUtils.readValue(json, ArrayList.class);
        RestResponse message = check(data);
        if (StringUtils.isNotBlank(message.getMessage())) {
            return message;
        }else{
//            k3cloudGlVoucher.setFdate(LocalDate.parse(billDate, DateTimeFormatter.ofPattern(DateFormat.DATE.getValue())));
//            k3cloudGlVoucherService.save(datas,k3cloudGlVoucher,getGlVoucher());
             message.setMessage("凭证保存成功");
            return message;
        }
    }

    public RestResponse check(List<List<Object>> datas) {
        RestResponse message;
        return null;
    }

    //将headers和data赋值
    public List<List<String>>  initData(VoucherDto voucherDto, VoucherModel voucherModel,List<String> flexItemGroupNameList) {
        if (voucherDto.getId() != null) {
            List<List<String>> datas = Lists.newArrayList();
            //所有科目
            Map<String, BdAccount> accountNumberMap = voucherModel.getBdAccountList().stream().collect(Collectors.toMap(BdAccount::getFNumber, BdAccount -> BdAccount));
            //所有核算维度
            Map<String, BdFlexItemProperty> flexItemPropertyNameMap = voucherModel.getBdFlexItemPropertyList().stream().collect(Collectors.toMap(BdFlexItemProperty::getFName, BdFlexItemProperty -> BdFlexItemProperty));
            Map<String, BdFlexItemProperty> flexItemPropertyFlexNumberMap = voucherModel.getBdFlexItemPropertyList().stream().collect(Collectors.toMap(BdFlexItemProperty::getFFlexNumber, BdFlexItemProperty -> BdFlexItemProperty));
            //所有使用的核算维度组
            List<String> headers = Lists.newLinkedList();
            for (String header : flexItemGroupNameList) {
                headers.add("FDetailID__" + flexItemPropertyNameMap.get(header).getFFlexNumber());
            }
            //设置名称
            Map<String, Map<String, String>> map = voucherModel.getResult();
            Map<String, Map<String, String>> reverseMap = Maps.newHashMap();
            for (String key : map.keySet()) {
                reverseMap.put(key, Maps.<String, String>newHashMap());
                for (String tempKey : map.get(key).keySet()) {
                    reverseMap.get(key).put(map.get(key).get(tempKey), tempKey);
                }
            }
            List<VoucherEntry> voucherEntryList = voucherEntryRepository.findByVoucherId(voucherDto.getId());
            for (VoucherEntry voucherEntry : voucherEntryList) {
                List<String> list = Lists.newArrayList();
                list.add(voucherEntry.getFExplanation());//摘要
                BdAccount bdAccount = accountNumberMap.get(voucherEntry.getFAccountId());
                list.add(bdAccount.getFNumber() + CharConstant.SLASH_LINE + bdAccount.getFName());//科目
                Map<String, VoucherEntryFlow> voucherEntryFlowNameMap = Maps.newHashMap();
                List<VoucherEntryFlow> voucherEntryFlowList = voucherEntryFlowRepository.findByVoucherEntryId(voucherEntry.getId());
                if (CollectionUtil.isNotEmpty(voucherEntryFlowList)) {
                    voucherEntryFlowNameMap = voucherEntryFlowList.stream().collect(Collectors.toMap(VoucherEntryFlow::getName,VoucherEntryFlow->VoucherEntryFlow));
                    for (VoucherEntryFlow voucherEntryFlow : voucherEntryFlowList) {
                        voucherEntryFlow.setFlexName(flexItemPropertyFlexNumberMap.get(voucherEntryFlow.getFlexNumber()).getFName());
                        String value = reverseMap.get(voucherEntryFlow.getFlexName()).get(voucherEntryFlow.getCode());
                        voucherEntryFlow.setValue(voucherEntryFlow.getCode() + CharConstant.SLASH_LINE + value);
                    }
                }
                for (String header : headers) {
                    if (voucherEntryFlowNameMap.containsKey(header)) {
                        list.add(voucherEntryFlowNameMap.get(header).getValue());
                    } else {
                        list.add("");
                    }
                }
                if (voucherEntry.getFCredit() == null) {
                    list.add(voucherEntry.getFDebit().toString());
                    list.add("");
                } else {
                    list.add("");
                    list.add(voucherEntry.getFCredit().toString());
                }
                datas.add(list);
            }
            return datas;
        }
        return null;
    }

    //获取所有科目名称及其对应的核算维度
    public Map<String, List<String>> accountNameToFlexGroupNamesMap(List<BdAccount> bdAccountList, List<BdFlexItemGroup> bdFlexItemGroupList) {
        Map<String, List<String>> result = Maps.newHashMap();
        Map<String, BdFlexItemGroup> flexItemgroupIdMap = bdFlexItemGroupList.stream().collect(Collectors.toMap(BdFlexItemGroup::getFId,BdFlexItemGroup->BdFlexItemGroup));
        for (BdAccount bdAccount : bdAccountList) {
            if (!"0".equals(bdAccount.getFItemDetailId())) {
                result.put( bdAccount.getFName(), flexItemgroupIdMap.get(bdAccount.getFItemDetailId()).getFNames());
            }
        }
        return result;
    }
}
