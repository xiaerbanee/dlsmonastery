package net.myspring.cloud.modules.report.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.enums.CustomerReceiveEnum;
import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.kingdee.mapper.*;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDetailDto;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import net.myspring.cloud.modules.report.mapper.CustomerReceiveMapper;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveDetailQuery;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/5/11.
 */
@Service
public class CustomerReceiveService {
    @Autowired
    private CustomerReceiveMapper customerReceiveMapper;
    @Autowired
    private ArOtherRecableMapper arOtherRecableMapper;
    @Autowired
    private ArReceiveBillMapper arReceiveBillMapper;
    @Autowired
    private ArRefundBillMapper arRefundBillMapper;
    @Autowired
    private SalOutStockMapper salOutStockMapper;
    @Autowired
    private SalReturnStockMapper salReturnStockMapper;
    @Autowired
    private BdCustomerMapper bdCustomerMapper;

    public List<CustomerReceiveDto>  findCustomerReceiveDtoList(CustomerReceiveQuery customerReceiveQuery) {
        LocalDate dateStart = customerReceiveQuery.getDateStart();
        LocalDate dateEnd = customerReceiveQuery.getDateEnd();
        HashSet<String> customerIdSet = new HashSet<String>();
        if (customerReceiveQuery.getCustomerIdList() != null){
            customerIdSet.addAll(customerReceiveQuery.getCustomerIdList());
        }
        if (StringUtils.isNotBlank(customerReceiveQuery.getCustomerGroup())){
           List<BdCustomer> customerList =  bdCustomerMapper.findByPrimaryGroup(customerReceiveQuery.getCustomerGroup());
           customerIdSet.addAll(CollectionUtil.extractToList(customerList,"FCustId"));
        }
        if (customerIdSet.isEmpty()) {
            BdCustomer bdCustomer = bdCustomerMapper.findTopOne();
            List<BdCustomer> customerList =  bdCustomerMapper.findByPrimaryGroup(bdCustomer.getFPrimaryGroup());
            customerIdSet.addAll(CollectionUtil.extractToList(customerList,"FCustId"));
        }
        List<CustomerReceiveDto> tempList = Lists.newLinkedList();
        List<CustomerReceiveDto> dataForStartDate = customerReceiveMapper.findByEndDateAndCustomerIdList(dateStart,customerIdSet);
        List<CustomerReceiveDto> dataForEndDate = customerReceiveMapper.findByEndDateAndCustomerIdList(dateEnd.plusDays(1),customerIdSet);
        //期初结余
        Map<String,BigDecimal> dateStartMap = Maps.newHashMap();
        for(CustomerReceiveDto startItem : dataForStartDate){
            String key1 = startItem.getCustomerId();
            dateStartMap.put(key1,startItem.getBeginShouldGet());
        }
        //期末结余
        for(CustomerReceiveDto endItem : dataForEndDate){
            CustomerReceiveDto summaryModel = new CustomerReceiveDto();
            summaryModel.setCustomerId(endItem.getCustomerId());
            summaryModel.setEndShouldGet(endItem.getBeginShouldGet());
            String key2 = endItem.getCustomerId();
            if(dateStartMap.containsKey(key2)){
                summaryModel.setBeginShouldGet(dateStartMap.get(key2));
            }else{
                summaryModel.setBeginShouldGet(BigDecimal.ZERO);
            }
            tempList.add(summaryModel);
        }
        List<CustomerReceiveDto> QTYSDListForPeriodList = arOtherRecableMapper.findByPeriodForSum(dateStart, dateEnd,customerIdSet);
        List<CustomerReceiveDto> XSTHDListForPeriodList = salReturnStockMapper.findXSTHDByPeriodForSum(dateStart, dateEnd,customerIdSet);
        List<CustomerReceiveDto> XSCKDListForPeriodList = salOutStockMapper.findXSCKDByPeriodForSum(dateStart, dateEnd,customerIdSet);
        List<CustomerReceiveDto> SKDForPeriodList = arReceiveBillMapper.findByPeriodForSum(dateStart, dateEnd,customerIdSet);
        List<CustomerReceiveDto> SKTKDForPeriodList = arRefundBillMapper.findByPeriodForSum(dateStart, dateEnd,customerIdSet);
        for(CustomerReceiveDto item : tempList) {
            String key = item.getCustomerId();
            BigDecimal QTYSDAmount = BigDecimal.ZERO;
            BigDecimal XSTHDAmount = BigDecimal.ZERO;
            BigDecimal XSCKDAmount = BigDecimal.ZERO;
            BigDecimal SKDAmount = BigDecimal.ZERO;
            BigDecimal SKTKDAmount = BigDecimal.ZERO;
            for (CustomerReceiveDto itemFor : QTYSDListForPeriodList) {
                String keyFor = itemFor.getCustomerId();
                if (key.equals(keyFor)) {
                    QTYSDAmount = itemFor.getBeginShouldGet();
                }
            }
            for (CustomerReceiveDto itemFor : XSTHDListForPeriodList) {
                String keyFor = itemFor.getCustomerId();
                if (key.equals(keyFor)) {
                    XSTHDAmount = itemFor.getBeginShouldGet();
                }
            }
            for (CustomerReceiveDto itemFor : XSCKDListForPeriodList) {
                String keyFor = itemFor.getCustomerId();
                if (key.equals(keyFor)) {
                    XSCKDAmount = itemFor.getBeginShouldGet();
                }
            }
            for (CustomerReceiveDto itemFor : SKDForPeriodList) {
                String keyFor = itemFor.getCustomerId();
                if (key.equals(keyFor)) {
                    SKDAmount = itemFor.getBeginShouldGet();
                }
            }
            for (CustomerReceiveDto itemFor : SKTKDForPeriodList) {
                String keyFor = itemFor.getCustomerId();
                if (key.equals(keyFor)) {
                    SKTKDAmount = itemFor.getBeginShouldGet();
                }
            }
            //应收
            item.setRealGet(QTYSDAmount.add(XSCKDAmount.subtract(XSTHDAmount)));
            //实收
            item.setShouldGet(SKDAmount.subtract(SKTKDAmount));
        }
        List<String> customerIdList = CollectionUtil.extractToList(tempList,"customerId");
        List<BdCustomer> customerList =  bdCustomerMapper.findByIdList(customerIdList);
        for (CustomerReceiveDto summary : tempList){
            for(BdCustomer customer : customerList){
                if(summary.getCustomerId().equals(customer.getFCustId())) {
                    summary.setCustomerName(customer.getFName());
                    summary.setCustomerGroupName(customer.getFPrimaryGroupName());
                }
            }
            if (customerReceiveQuery.getQueryDetail()){
                CustomerReceiveDetailQuery query = new CustomerReceiveDetailQuery();
                query.setDateStart(dateStart);
                query.setDateEnd(dateEnd);
                query.setCustomerId(summary.getCustomerId());
                summary.setCustomerReceiveDetailDtoList(findCustomerReceiveDetailDtoList(query));
            }
        }
        return tempList;
    }

    public List<CustomerReceiveDetailDto>  findCustomerReceiveDetailDtoList(CustomerReceiveDetailQuery customerReceiveDetailQuery) {
        LocalDate dateStart = customerReceiveDetailQuery.getDateStart();
        LocalDate dateEnd = customerReceiveDetailQuery.getDateEnd();
        String customerId = customerReceiveDetailQuery.getCustomerId();
        List<CustomerReceiveDetailDto> dataList = Lists.newArrayList();
        HashSet<String> customerSet = new HashSet<String>();
        customerSet.add(customerId);
        List<CustomerReceiveDto> summaryItemList = customerReceiveMapper.findByEndDateAndCustomerIdList(dateStart,customerSet);
        CustomerReceiveDto summaryItem = new CustomerReceiveDto();
        summaryItem.setCustomerId(customerId);
        summaryItem.setCustomerName(bdCustomerMapper.findById(customerId).getFName());
        summaryItem.setBeginShouldGet(BigDecimal.ZERO);
        for(CustomerReceiveDto item : summaryItemList){
            if(item.getCustomerId() != null){
                summaryItem.setBeginShouldGet(item.getBeginShouldGet());
            }
        }
        List<CustomerReceiveDetailDto> detailForBillList = customerReceiveMapper.findByPeriodForBillSum(dateStart, dateEnd,customerId);
        //物料详细
        List<CustomerReceiveDetailDto> detailForMaterialList = customerReceiveMapper.findByPeriodForMaterial(dateStart, dateEnd,customerId);
        Map<String,List<CustomerReceiveDetailDto>> detailForMaterialMap = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(detailForMaterialList)) {
            for (CustomerReceiveDetailDto customerAccount : detailForMaterialList) {
                if (!detailForMaterialMap.containsKey(customerAccount.getBillNo())) {
                    detailForMaterialMap.put(customerAccount.getBillNo(), new ArrayList<CustomerReceiveDetailDto>());
                }
                detailForMaterialMap.get(customerAccount.getBillNo()).add(customerAccount);
            }
        }
        CustomerReceiveDetailDto head = new CustomerReceiveDetailDto();
        head.setBillType(summaryItem.getCustomerName());
        head.setIndex(-2);
        dataList.add(head);

        CustomerReceiveDetailDto beginAmount = new CustomerReceiveDetailDto();
        beginAmount.setBillType("期初应收");
        beginAmount.setEndShouldGet(summaryItem.getBeginShouldGet());
        beginAmount.setIndex(-1);
        dataList.add(beginAmount);
        BigDecimal endShouldGet = summaryItem.getBeginShouldGet();
        for(int i = 0 ;i<detailForBillList.size();i++){
            CustomerReceiveDetailDto billSum = detailForBillList.get(i);
            CustomerReceiveDetailDto receivableDetail = new CustomerReceiveDetailDto();
            receivableDetail.setIndex(i);
            receivableDetail.setBillType(billSum.getBillType());
            receivableDetail.setBillDate(billSum.getBillDate());
            receivableDetail.setBillNo(billSum.getBillNo());
            receivableDetail.setRemarks(billSum.getRemarks());
            if(!"C".equals(billSum.getBillStatus())){
                receivableDetail.setIndex(-3);
            }
            //实收
            if(billSum.getBillType().equals(CustomerReceiveEnum.销售收款单.name())){
                receivableDetail.setShouldGet(billSum.getAmount());
                endShouldGet = endShouldGet.subtract(receivableDetail.getRealGet());
                receivableDetail.setEndShouldGet(endShouldGet);
                dataList.add(receivableDetail);
            }else if(billSum.getBillType().equals(CustomerReceiveEnum.销售业务退款单.name())){
                receivableDetail.setShouldGet(BigDecimal.ZERO.subtract(billSum.getAmount()));
                endShouldGet = endShouldGet.subtract(receivableDetail.getRealGet());
                receivableDetail.setEndShouldGet(endShouldGet);
                dataList.add(receivableDetail);
                //不计入期末期初
            }else if(billSum.getBillType().equals(CustomerReceiveEnum.现销退货单.name())) {
                receivableDetail.setShouldGet(BigDecimal.ZERO.subtract(billSum.getAmount()));
                receivableDetail.setEndShouldGet(endShouldGet.add(receivableDetail.getShouldGet()));
                dataList.add(receivableDetail);
            }else if(billSum.getBillType().equals(CustomerReceiveEnum.现销出库单.name())){
                receivableDetail.setShouldGet(billSum.getAmount());
                receivableDetail.setEndShouldGet(endShouldGet.add(receivableDetail.getShouldGet()));
                dataList.add(receivableDetail);
                //应收
            }else if(billSum.getBillType().equals(CustomerReceiveEnum.标准销售退货单.name())) {
                receivableDetail.setShouldGet(BigDecimal.ZERO.subtract(billSum.getAmount()));
                endShouldGet = endShouldGet.add(receivableDetail.getShouldGet());
                receivableDetail.setEndShouldGet(endShouldGet);
                dataList.add(receivableDetail);
            }else{
                receivableDetail.setShouldGet(billSum.getAmount());
                endShouldGet = endShouldGet.add(receivableDetail.getShouldGet());
                receivableDetail.setEndShouldGet(endShouldGet);
                dataList.add(receivableDetail);
            }
            if (detailForMaterialMap.containsKey(billSum.getBillNo())) {
                for (CustomerReceiveDetailDto billMaterial : detailForMaterialMap.get(billSum.getBillNo())) {
                    if (billMaterial.getBillNo().contains("XSTHD")) {
                        billMaterial.setQty(0L - billMaterial.getQty());
                    } else if (billMaterial.getQty() != null) {
                        billMaterial.setQty(billMaterial.getQty());
                    }
                    billMaterial.setBillType(CharConstant.EMPTY);
                    billMaterial.setIndex(i);
                    dataList.add(billMaterial);
                }
            }
        }
        CustomerReceiveDetailDto endAmount = new CustomerReceiveDetailDto();
        endAmount.setBillType("期末应收");
        endAmount.setEndShouldGet(endShouldGet);
        endAmount.setIndex(-1);
        dataList.add(endAmount);
        return dataList;
    }

}
