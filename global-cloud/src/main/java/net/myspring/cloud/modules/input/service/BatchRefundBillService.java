package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.K3CloudFormIdEnum;
import net.myspring.cloud.common.enums.KingdeeNameEnum;
import net.myspring.cloud.common.handsontable.HandSonTableUtils;
import net.myspring.cloud.common.utils.CacheUtils;
import net.myspring.cloud.common.utils.SecurityUtils;
import net.myspring.cloud.modules.input.domain.BdCustomer;
import net.myspring.cloud.modules.input.domain.CnBank;
import net.myspring.cloud.modules.input.dto.BatchRefundBillDto;
import net.myspring.cloud.modules.input.dto.K3CloudSaveDto;
import net.myspring.cloud.modules.input.mapper.BdCustomerMapper;
import net.myspring.cloud.modules.input.mapper.BdDepartmentMapper;
import net.myspring.cloud.modules.input.mapper.CnBankMapper;
import net.myspring.cloud.modules.remote.dto.AccountDto;
import net.myspring.cloud.modules.sys.mapper.KingdeeBookMapper;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.BoolEnum;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 收款退款单
 * Created by liuj on 2016-06-27.
 */
@Service
@KingdeeDataSource
public class BatchRefundBillService {
    @Autowired
    private CnBankMapper cnBankMapper;
    @Autowired
    private BdCustomerMapper bdCustomerMapper;
    @Autowired
    private BdDepartmentMapper bdDepartmentMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private KingdeeBookMapper kingdeeBookMapper;

    //收款退款单（银行）
    public List<String> save(List<List<Object>> datas) {
        Map<String, String> bankMap = Maps.newHashMap();
        Map<String, String> customerMap = Maps.newHashMap();
        Map<String, String> customerIdMap = Maps.newHashMap();
        for (CnBank cnBank : cnBankMapper.findAll()) {
            bankMap.put(cnBank.getfName(), cnBank.getfNumber());
        }
        for (BdCustomer bdCustomer : bdCustomerMapper.findAll()) {
            customerMap.put(bdCustomer.getfName(), bdCustomer.getfNumber());
            customerIdMap.put(bdCustomer.getfName(), bdCustomer.getfCustId());
        }
        Map<String, BatchRefundBillDto> ARRefundBillMap = Maps.newLinkedHashMap();
        Map<String, BatchRefundBillDto> cashARRefundBillMap = Maps.newLinkedHashMap();
        for (List<Object> row : datas) {
            String customerName = StringUtils.toString(row.get(0));
            String bankName = HandSonTableUtils.getValue(row, 1);
            LocalDate billDate = LocalDateUtils.parse(HandSonTableUtils.getValue(row, 2));
            String priceStr = HandSonTableUtils.getValue(row, 3);
            BigDecimal amount = StringUtils.isEmpty(priceStr) ? BigDecimal.ZERO : new BigDecimal(priceStr);
            String settleType = HandSonTableUtils.getValue(row, 4);
            String remarks = HandSonTableUtils.getValue(row, 5);
            String billKey = "";
            if ("电汇".equals(settleType)) {
                billKey = customerName + CharConstant.COMMA + bankName + CharConstant.COMMA + billDate + CharConstant.COMMA + amount + CharConstant.COMMA + remarks;
                if (!ARRefundBillMap.containsKey(billKey)) {
                    BatchRefundBillDto arRefundBill = new BatchRefundBillDto();
                    arRefundBill.setCustomerName(customerMap.get(customerName));
                    arRefundBill.setBankName(BoolEnum.TRUE.getValue().toString().equals(StringUtils.isBlank(bankName)) ? "" : bankMap.get(bankName));
                    arRefundBill.setBillDate(billDate);
                    arRefundBill.setAmount(amount);
                    arRefundBill.setDepartment(bdDepartmentMapper.findByCustomerId(customerIdMap.get(customerName)).getfNumber());
                    if (KingdeeNameEnum.WZOPPO.name().equals(kingdeeBookMapper.findNameByCompanyId(SecurityUtils.getCompanyId()))) {
                        arRefundBill.setSubject(bdCustomerMapper.findNumberByName("银行存款"));
                    }
                    arRefundBill.setNote(remarks);
                    ARRefundBillMap.put(billKey, arRefundBill);
                } else {
                    ARRefundBillMap.get(billKey).setAmount(amount.add(amount));
                }
            } else {
                billKey = customerName + CharConstant.COMMA + billDate + CharConstant.COMMA + amount + CharConstant.COMMA + remarks;
                if (!cashARRefundBillMap.containsKey(billKey)) {
                    BatchRefundBillDto arRefundBill = new BatchRefundBillDto();
                    arRefundBill.setCustomerName(customerMap.get(customerName));
                    arRefundBill.setBillDate(billDate);
                    arRefundBill.setAmount(amount);
                    arRefundBill.setDepartment(bdDepartmentMapper.findByCustomerId(customerIdMap.get(customerName)).getfNumber());
                    arRefundBill.setSubject(bdCustomerMapper.findNumberByName("库存现金"));
                    arRefundBill.setNote(remarks);
                    cashARRefundBillMap.put(billKey, arRefundBill);
                } else {
                    cashARRefundBillMap.get(billKey).setAmount(amount.add(amount));
                }
            }

        }
        AccountDto accountDto = new AccountDto();
        cacheUtils.initCacheInput(accountDto);
        List<String> billNos = Lists.newArrayList();
        List<BatchRefundBillDto> billList = Lists.newArrayList(ARRefundBillMap.values());
        if (CollectionUtil.isNotEmpty(billList)) {
            for (BatchRefundBillDto arRefundBill : billList) {
                K3CloudSaveDto k3CloudSaveDto = new K3CloudSaveDto(K3CloudFormIdEnum.AR_REFUNDBILL.name(), getARRefundBill(arRefundBill,accountDto));
                String billNo = null;
                billNos.add(billNo);
            }
        }
        List<BatchRefundBillDto> cashBillList = Lists.newArrayList(cashARRefundBillMap.values());
        if (CollectionUtil.isNotEmpty(cashBillList)) {
            for (BatchRefundBillDto arRefundBill : cashBillList) {
                K3CloudSaveDto k3CloudSaveDto = new K3CloudSaveDto(K3CloudFormIdEnum.AR_REFUNDBILL.name(), getCashARRefundBill(arRefundBill,accountDto));
                String billNo = null;
                billNos.add(billNo);
            }
        }
        return billNos;
    }

    //银行
    private String getARRefundBill(BatchRefundBillDto arRefundBill, AccountDto accountDto) {
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", accountDto.getName());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = getHeader(arRefundBill);
        List<Object> entity = Lists.newArrayList();
        Map<String, Object> detail = Maps.newLinkedHashMap();
        detail.put("FSETTLETYPEID", CollectionUtil.getMap("FNumber", "JSFS04_SYS"));
        detail.put("FPURPOSEID", CollectionUtil.getMap("FNumber", "SFKYT01_SYS"));
        if (KingdeeNameEnum.WZOPPO.name().equals(kingdeeBookMapper.findNameByCompanyId(SecurityUtils.getCompanyId()))) {
            detail.put("F_YLG_Base", CollectionUtil.getMap("FNumber", arRefundBill.getSubject()));
        }
        detail.put("FREFUNDAMOUNTFOR", arRefundBill.getAmount());
        detail.put("FREFUNDAMOUNTFOR_E", arRefundBill.getAmount());
        detail.put("FREALREFUNDAMOUNTFOR_D", arRefundBill.getAmount());
        detail.put("FACCOUNTID", CollectionUtil.getMap("FNumber", arRefundBill.getBankName()));
        detail.put("FNOTE", arRefundBill.getNote());
        entity.add(detail);
        model.put("AR_REFUNDBILL__FREFUNDBILLENTRY", entity);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        System.err.println(result);
        return result;
    }

    //现金
    private String getCashARRefundBill(BatchRefundBillDto arRefundBill, AccountDto accountDto) {
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", accountDto.getName());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = getHeader(arRefundBill);
        List<Object> entity = Lists.newArrayList();
        Map<String, Object> detail = Maps.newLinkedHashMap();
        detail.put("FSETTLETYPEID", CollectionUtil.getMap("FNumber", "JSFS01_SYS"));
        detail.put("FPURPOSEID", CollectionUtil.getMap("FNumber", "SFKYT01_SYS"));
        if (KingdeeNameEnum.WZOPPO.name().equals(kingdeeBookMapper.findNameByCompanyId(SecurityUtils.getCompanyId()))) {
            detail.put("F_YLG_Base", CollectionUtil.getMap("FNumber", arRefundBill.getSubject()));
        }
        detail.put("FREFUNDAMOUNTFOR", arRefundBill.getAmount());
        detail.put("FREFUNDAMOUNTFOR_E", arRefundBill.getAmount());
        detail.put("FREALREFUNDAMOUNTFOR_D", arRefundBill.getAmount());
        detail.put("FNOTE", arRefundBill.getNote());
        entity.add(detail);
        model.put("AR_REFUNDBILL__FREFUNDBILLENTRY", entity);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        System.err.println(result);
        return result;
    }


    private Map<String, Object> getHeader(BatchRefundBillDto arRefundBill) {
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "SKTKDLX01_SYS"));
        model.put("FDATE", arRefundBill.getBillDate());
        model.put("FCONTACTUNITTYPE", "BD_Customer");
        model.put("FCONTACTUNIT", CollectionUtil.getMap("FNumber", arRefundBill.getCustomerName()));
        model.put("FRECTUNITTYPE", "BD_Customer");
        model.put("FRECTUNIT", CollectionUtil.getMap("FNumber", arRefundBill.getCustomerName()));
        model.put("FCURRENCYID", CollectionUtil.getMap("FNumber", "PRE001"));
        model.put("FSETTLECUR", CollectionUtil.getMap("FNumber", "PRE001"));
        model.put("FPAYORGID", CollectionUtil.getMap("FNumber", 100));
        model.put("FSETTLEORGID", CollectionUtil.getMap("FNumber", 100));
        model.put("FSALEORGID", CollectionUtil.getMap("FNumber", 100));
        model.put("FSALEDEPTID", CollectionUtil.getMap("FNumber", arRefundBill.getDepartment()));
        model.put("FREFUNDTOTALAMOUNTFOR", arRefundBill.getAmount());
        model.put("FREALREFUNDAMOUNTFOR", arRefundBill.getAmount());
        model.put("FSETTLERATE", 1);
        model.put("FEXCHANGERATE", 1);
        model.put("FBUSINESSTYPE", 1);
        return model;
    }

}
