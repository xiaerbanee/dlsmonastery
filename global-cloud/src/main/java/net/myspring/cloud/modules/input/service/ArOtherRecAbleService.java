package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.BillTypeEnum;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.common.utils.HandsontableUtils;
import net.myspring.cloud.modules.input.dto.ArOtherRecAbleDto;
import net.myspring.cloud.modules.input.dto.ArOtherRecAbleFEntityDto;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.input.web.form.ArOtherRecAbleForm;
import net.myspring.cloud.modules.kingdee.domain.*;
import net.myspring.cloud.modules.kingdee.repository.*;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 其他应收单
 * Created by lihx on 2017/6/20.
 */
@Service
@KingdeeDataSource
public class ArOtherRecAbleService {
    @Autowired
    private BdCustomerRepository bdCustomerRepository;
    @Autowired
    private BdDepartmentRepository bdDepartmentRepository;
    @Autowired
    private HrEmpInfoRepository hrEmpInfoRepository;
    @Autowired
    private BasAssistantRepository basAssistantRepository;
    @Autowired
    private BdAccountRepository bdAccountRepository;
    @Autowired
    private KingdeeManager kingdeeManager;

    private KingdeeSynDto save(ArOtherRecAbleDto arOtherRecAbleDto, KingdeeBook kingdeeBook){
        KingdeeSynDto kingdeeSynDto = new KingdeeSynDto(
                arOtherRecAbleDto.getExtendId(),
                arOtherRecAbleDto.getExtendType(),
                KingdeeFormIdEnum.AR_OtherRecAble.name(),
                arOtherRecAbleDto.getJson(),
                kingdeeBook) {
            };
            kingdeeManager.save(kingdeeSynDto);
        return kingdeeSynDto;
    }

    public List<KingdeeSynDto> save(ArOtherRecAbleForm arOtherRecAbleForm, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook) {
        LocalDate billDate = arOtherRecAbleForm.getBillDate();
        String json = HtmlUtils.htmlUnescape(arOtherRecAbleForm.getJson());
        List<List<Object>> data = ObjectMapperUtils.readValue(json, ArrayList.class);
        Map<String, ArOtherRecAbleDto> arOtherRecAbleDtoMap = Maps.newLinkedHashMap();
       
        List<String> customerNameList = Lists.newArrayList();
        List<String> departmentNameList = Lists.newArrayList();
        List<String> accountNameList = Lists.newArrayList();
        List<String> empInfoNameList = Lists.newArrayList();
        List<String> assistantNameList = Lists.newArrayList();
        for (List<Object> row : data) {
            customerNameList.add(HandsontableUtils.getValue(row, 0));
            departmentNameList.add(HandsontableUtils.getValue(row, 1));
            accountNameList.add(HandsontableUtils.getValue(row, 2));
            empInfoNameList.add(HandsontableUtils.getValue(row, 5));
            assistantNameList.add(HandsontableUtils.getValue(row, 6));
            assistantNameList.add(HandsontableUtils.getValue(row, 7));
            customerNameList.add(HandsontableUtils.getValue(row, 8));
        }
        Map<String, String> accountNameMap = bdAccountRepository.findByNameList(accountNameList).stream().collect(Collectors.toMap(BdAccount::getFName,BdAccount::getFNumber));
        Map<String, String> departmentNameMap = bdDepartmentRepository.findByNameList(departmentNameList).stream().collect(Collectors.toMap(BdDepartment::getFFullName,BdDepartment::getFNumber));
        List<BasAssistant> basAssistantList = basAssistantRepository.findByNameList(assistantNameList);
        Map<String, String> otherTypeNameMap = Maps.newHashMap();
        Map<String, String> expenseTypeNameMap = Maps.newHashMap();
        for (BasAssistant basAssistant :basAssistantList){
            if ("其他类".equals(basAssistant.getFType())){
               otherTypeNameMap.put(basAssistant.getFDataValue(),basAssistant.getFNumber());
            }else if("费用类".equals(basAssistant.getFType())){
                expenseTypeNameMap.put(basAssistant.getFDataValue(),basAssistant.getFNumber());
            }
        }
        Map<String, String> empInfoNameMap = hrEmpInfoRepository.findByNameList(empInfoNameList).stream().collect(Collectors.toMap(HrEmpInfo::getFName,HrEmpInfo::getFNumber));
        Map<String, String> customerNameMap = bdCustomerRepository.findByNameList(customerNameList).stream().collect(Collectors.toMap(BdCustomer::getFName,BdCustomer::getFNumber));
        for (List<Object> row : data) {
            String customerName = HandsontableUtils.getValue(row,0);
            String departmentName = HandsontableUtils.getValue(row,1);
            String accountName = HandsontableUtils.getValue(row,2);
            String amountStr = HandsontableUtils.getValue(row,3);
            BigDecimal amount = StringUtils.isEmpty(amountStr) ? BigDecimal.ZERO : new BigDecimal(amountStr);
            String remarks = HandsontableUtils.getValue(row,4);
            String empInfoName = HandsontableUtils.getValue(row,5);
            String otherTypeName = HandsontableUtils.getValue(row,6);
            String expenseTypeName = HandsontableUtils.getValue(row,7);
            String customerForName = HandsontableUtils.getValue(row,8);

            ArOtherRecAbleFEntityDto entityDto = new ArOtherRecAbleFEntityDto();
            entityDto.setEmpInfoNumber(empInfoNameMap.get(empInfoName));
            entityDto.setOtherTypeNumber(otherTypeNameMap.get(otherTypeName));
            entityDto.setExpenseTypeNumber(expenseTypeNameMap.get(expenseTypeName));
            if(StringUtils.isNotBlank(customerForName)){
                entityDto.setCustomerForNumber(customerNameMap.get(customerForName));
            }
            entityDto.setDepartmentNumber(departmentNameMap.get(departmentName));
            entityDto.setAmount(amount);
            entityDto.setAccountNumber(accountNameMap.get(accountName));
            entityDto.setComment(remarks);
            String billKey = customerName + CharConstant.COMMA+ departmentName +CharConstant.COMMA+accountName+CharConstant.COMMA+amount+CharConstant.COMMA+remarks;
            if (!arOtherRecAbleDtoMap.containsKey(billKey)) {
                ArOtherRecAbleDto arOtherRecAbleDto = new ArOtherRecAbleDto();
                arOtherRecAbleDto.setExtendType(BillTypeEnum.其他应收单_K3.name());
                arOtherRecAbleDto.setCreatorK3(accountKingdeeBook.getUsername());
                arOtherRecAbleDto.setKingdeeName(kingdeeBook.getName());
                arOtherRecAbleDto.setDate(billDate);
                arOtherRecAbleDto.setCustomerNumber(customerNameMap.get(customerName));
                arOtherRecAbleDto.setAmount(amount);
                arOtherRecAbleDto.setDepartmentNumber(departmentNameMap.get(departmentName));
                arOtherRecAbleDtoMap.put(billKey, arOtherRecAbleDto);
            } else {
                BigDecimal amountTotal = arOtherRecAbleDtoMap.get(billKey).getAmount();
                arOtherRecAbleDtoMap.get(billKey).setAmount(amountTotal.add(amount));
            }
            arOtherRecAbleDtoMap.get(billKey).getArOtherRecAbleFEntityDtoList().add(entityDto);
        }
        List<ArOtherRecAbleDto> billList = Lists.newArrayList(arOtherRecAbleDtoMap.values());
        List<KingdeeSynDto> kingdeeSynDtoList = save(billList,kingdeeBook,accountKingdeeBook);
        return kingdeeSynDtoList;
    }

    public KingdeeSynDto save(ArOtherRecAbleDto arOtherRecAbleDto, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        KingdeeSynDto kingdeeSynDto;
        Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
        if(isLogin) {
            kingdeeSynDto = save(arOtherRecAbleDto,kingdeeBook);
        }else{
            kingdeeSynDto = new KingdeeSynDto(false,"未登入金蝶系统");
        }
        return kingdeeSynDto;
    }

    public List<KingdeeSynDto> save(List<ArOtherRecAbleDto> arOtherRecAbleDtoList, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        List<KingdeeSynDto> kingdeeSynDtoList = Lists.newArrayList();
        Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
        if(isLogin) {
            if (CollectionUtil.isNotEmpty(arOtherRecAbleDtoList)) {
                for (ArOtherRecAbleDto arOtherRecAbleDto : arOtherRecAbleDtoList) {
                    KingdeeSynDto kingdeeSynDto = save(arOtherRecAbleDto,kingdeeBook);
                    kingdeeSynDtoList.add(kingdeeSynDto);
                }
            }
        }else{
            kingdeeSynDtoList.add(new KingdeeSynDto(false,"未登入金蝶系统"));
        }
        return kingdeeSynDtoList;
    }

    public ArOtherRecAbleForm getForm(){
        ArOtherRecAbleForm arOtherRecAbleForm = new ArOtherRecAbleForm();
        Map<String,Object> map = Maps.newHashMap();
        map.put("accountNameList",bdAccountRepository.findAll().stream().map(BdAccount::getFName).collect(Collectors.toList()));
        map.put("staffNameList",hrEmpInfoRepository.findAll().stream().map(HrEmpInfo::getFName).collect(Collectors.toList()));
        map.put("departmentNameList",bdDepartmentRepository.findAll().stream().map(BdDepartment::getFFullName).collect(Collectors.toList()));
        map.put("otherTypeNameList",basAssistantRepository.findByType("其他类").stream().map(BasAssistant::getFDataValue).collect(Collectors.toList()));
        map.put("expenseTypeNameList",basAssistantRepository.findByType("费用类").stream().map(BasAssistant::getFDataValue).collect(Collectors.toList()));
        map.put("customerNameList",bdCustomerRepository.findAll().stream().map(BdCustomer::getFName).collect(Collectors.toList()));
        arOtherRecAbleForm.setExtra(map);
        return arOtherRecAbleForm;
    }
}
