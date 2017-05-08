package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.K3CloudFormIdEnum;
import net.myspring.cloud.common.enums.KingdeeNameEnum;
import net.myspring.cloud.common.utils.CacheUtils;
import net.myspring.cloud.common.utils.SecurityUtils;
import net.myspring.cloud.modules.input.domain.BasAssistant;
import net.myspring.cloud.modules.input.domain.BdAccount;
import net.myspring.cloud.modules.input.domain.HrEmpInfo;
import net.myspring.cloud.modules.input.dto.BatchOtherRecAbleDetailDto;
import net.myspring.cloud.modules.input.dto.BatchOtherRecAbleDto;
import net.myspring.cloud.modules.input.dto.K3CloudSave;
import net.myspring.cloud.modules.input.mapper.*;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import net.myspring.cloud.modules.input.utils.K3cloudUtils;
import net.myspring.cloud.modules.remote.dto.AccountDto;
import net.myspring.cloud.modules.sys.mapper.KingdeeBookMapper;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 其他应收单
 * Created by admin on 2016/10/21.
 */
@Service
@KingdeeDataSource
public class BatchOtherRecAbleService {
    @Autowired
    private BdCustomerMapper bdCustomerMapper;
    @Autowired
    private BdDepartmentMapper bdDepartmentMapper;
    @Autowired
    private BasAssistantMapper basAssistantMapper;
    @Autowired
    private HrEmpInfoMapper hrEmpInfoMapper;
    @Autowired
    private BdAccountMapper bdAccountMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private KingdeeBookMapper kingdeeBookMapper;

    //其他应收单
    public List<String> save(List<List<String>> datas, LocalDate billDate) {
        Map<String, BatchOtherRecAbleDto> aROtherRecAbleMap = Maps.newLinkedHashMap();
        Map<String, String> subjectMap = Maps.newHashMap();
        Map<String, String> departMap = Maps.newHashMap();
        Map<String, String> otherTypeMap = Maps.newHashMap();
        Map<String, String> expenseMap = Maps.newHashMap();
        Map<String, String> secUserMap = Maps.newHashMap();
        Map<String, String> customerMap = Maps.newHashMap();
        for (NameNumberDto bdCustomer : bdCustomerMapper.findNameAndNumber()) {
            customerMap.put(bdCustomer.getName(), bdCustomer.getName());
        }
        for (BdAccount bdSettleTyp : bdAccountMapper.findAllSubject()) {
            subjectMap.put(bdSettleTyp.getfName(), bdSettleTyp.getfNumber());
        }
        for (NameNumberDto item : bdDepartmentMapper.findNameAndNumber()) {
            departMap.put(item.getName(), item.getNumber());
        }
        for (BasAssistant item : basAssistantMapper.findByType("其他类")) {
            otherTypeMap.put(item.getfDataValue(), item.getfNumber());
        }
        for (BasAssistant item : basAssistantMapper.findByType("费用类")) {
            expenseMap.put(item.getfDataValue(), item.getfNumber());
        }
        for (HrEmpInfo serUser1 : hrEmpInfoMapper.findAllUser()) {
            secUserMap.put(serUser1.getfName(), serUser1.getfNumber());
        }
        for (List<String> row : datas) {
            BatchOtherRecAbleDetailDto aROtherRecAbleDetail = new BatchOtherRecAbleDetailDto();
            String customerName = StringUtils.toString(row.get(0));
            String departName = StringUtils.toString(row.get(1)).trim();
            String subjectName = StringUtils.toString(row.get(2)).trim();
            String priceStr = StringUtils.toString(row.get(3)).trim();
            BigDecimal amount = StringUtils.isEmpty(priceStr) ? BigDecimal.ZERO : new BigDecimal(priceStr);
            String remarks = StringUtils.toString(row.get(4)).trim();
            String secUser = "";
            String otherType = "";
            String expenseType = "";
            String F_PAEC_Base = "";
            secUser = StringUtils.toString(row.get(5)).trim();
            otherType = StringUtils.toString(row.get(6)).trim();
            expenseType = StringUtils.toString(row.get(7)).trim();
            aROtherRecAbleDetail.setSecUser(secUserMap.get(secUser));
            aROtherRecAbleDetail.setOtherType(otherTypeMap.get(otherType));
            aROtherRecAbleDetail.setExpenseType(expenseMap.get(expenseType));
            F_PAEC_Base = StringUtils.toString(row.get(8)).trim();
            if(StringUtils.isNotBlank(F_PAEC_Base)){
                aROtherRecAbleDetail.setF_PAEC_Base(customerMap.get(F_PAEC_Base));
            }
            aROtherRecAbleDetail.setDepartment(departMap.get(departName));
            aROtherRecAbleDetail.setAmount(amount);
            aROtherRecAbleDetail.setSubject(subjectMap.get(subjectName));
            aROtherRecAbleDetail.setRemarks(remarks);
            String billKey = customerName + CharConstant.COMMA+ departName + CharConstant.COMMA+subjectName+ CharConstant.COMMA+amount+ CharConstant.COMMA+remarks;
            if (!aROtherRecAbleMap.containsKey(billKey)) {
                BatchOtherRecAbleDto aROtherRecAble = new BatchOtherRecAbleDto();
                aROtherRecAble.setBillDate(billDate);
                aROtherRecAble.setCustomer(customerMap.get(customerName));
                aROtherRecAble.setAmount(amount);
                aROtherRecAble.setDepartment(departMap.get(departName));
                aROtherRecAble.setNote(remarks);
                aROtherRecAbleMap.put(billKey, aROtherRecAble);
            } else {
                BigDecimal amountTotal = aROtherRecAbleMap.get(billKey).getAmount();
                aROtherRecAbleMap.get(billKey).setAmount(amountTotal.add(amount));
            }
            aROtherRecAbleMap.get(billKey).getBatchOtherRecAbleDetailDtoList().add(aROtherRecAbleDetail);
        }
        AccountDto accountDto = new AccountDto();
        cacheUtils.initCacheInput(accountDto);
        List<BatchOtherRecAbleDto> billList = Lists.newArrayList(aROtherRecAbleMap.values());
        List<String> billNos = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(billList)) {
            for (BatchOtherRecAbleDto aROtherRecAble : billList) {
                K3CloudSave k3CloudSave = new K3CloudSave(K3CloudFormIdEnum.AR_OtherRecAble.name(), getAROtherRecAble(aROtherRecAble,accountDto));
                String billNo = K3cloudUtils.save(k3CloudSave,accountDto).getBillNo();
                billNos.add(billNo);
            }
        }
        return billNos;
    }

    // 其他应收单
    private String getAROtherRecAble(BatchOtherRecAbleDto aROtherRecAble, AccountDto accountDto) {
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", accountDto.getName());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FBillTypeID", K3cloudUtils.getMap("FNumber", "QTYSD01_SYS"));
        model.put("FDATE", aROtherRecAble.getBillDate());
        model.put("FCONTACTUNITTYPE", "BD_Customer");
        model.put("FCONTACTUNIT", K3cloudUtils.getMap("FNumber", aROtherRecAble.getCustomer()));
        model.put("FCURRENCYID", K3cloudUtils.getMap("FNumber", "PRE001"));
        model.put("FAMOUNTFOR", aROtherRecAble.getAmount());
        model.put("FDEPARTMENTID", K3cloudUtils.getMap("FNumber", aROtherRecAble.getDepartment()));
        model.put("FSALEORGID", K3cloudUtils.getMap("FNumber", 100));
        model.put("FSETTLEORGID", K3cloudUtils.getMap("FNumber", 100));
        model.put("FPAYORGID", K3cloudUtils.getMap("FNumber", 100));
        model.put("FEXCHANGETYPE", K3cloudUtils.getMap("FNumber", "HLTX01_SYS"));
        model.put("FEXCHANGERATE", 1);
        model.put("FMAINBOOKSTDCURRID", K3cloudUtils.getMap("FNumber", "PRE001"));
        List<Object> entity = Lists.newArrayList();
        for (BatchOtherRecAbleDetailDto aROtherRecAbleDetail : aROtherRecAble.getBatchOtherRecAbleDetailDtoList()) {
            Map<String, Object> detail = Maps.newLinkedHashMap();
            detail.put("FCOSTDEPARTMENTID", K3cloudUtils.getMap("FNumber", aROtherRecAbleDetail.getDepartment()));
            detail.put("F_YLG_Base", K3cloudUtils.getMap("FNumber", aROtherRecAbleDetail.getSubject()));
            detail.put("F_PAEC_Assistant", K3cloudUtils.getMap("FNumber", aROtherRecAbleDetail.getOtherType()));
            detail.put("F_PAEC_Assistant1", K3cloudUtils.getMap("FNumber", aROtherRecAbleDetail.getExpenseType()));
            if (StringUtils.isNotBlank(aROtherRecAbleDetail.getF_PAEC_Base())) {
                if (KingdeeNameEnum.WZOPPO.name().equals(kingdeeBookMapper.findNameByCompanyId(SecurityUtils.getCompanyId()))) {
                    detail.put("F_PAEC_Base", K3cloudUtils.getMap("FNumber", aROtherRecAbleDetail.getF_PAEC_Base()));
                } else {
                    detail.put("F_YLG_Base2", K3cloudUtils.getMap("FNumber", aROtherRecAbleDetail.getF_PAEC_Base()));
                }
            }
            detail.put("F_YLG_Base1", K3cloudUtils.getMap("FStaffNumber", aROtherRecAbleDetail.getSecUser()));
            detail.put("FNOTAXAMOUNTFOR", aROtherRecAbleDetail.getAmount());
            detail.put("FAMOUNTFOR_D", aROtherRecAbleDetail.getAmount());
            detail.put("FAMOUNT_D", aROtherRecAbleDetail.getAmount());
            detail.put("FCOMMENT", aROtherRecAbleDetail.getRemarks());
            entity.add(detail);
        }
        model.put("AR_OtherRecAble__FEntity", entity);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        System.out.println(result);
        return result;
    }
}