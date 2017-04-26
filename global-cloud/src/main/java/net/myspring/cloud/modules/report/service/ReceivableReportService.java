package net.myspring.cloud.modules.report.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.ReceivableBillTypeEnum;
import net.myspring.cloud.modules.input.domain.BdCustomer;
import net.myspring.cloud.modules.input.mapper.BdCustomerMapper;
import net.myspring.cloud.modules.report.dto.ReceivableForDetailDto;
import net.myspring.cloud.modules.report.dto.ReceivableForSummaryDto;
import net.myspring.cloud.modules.report.mapper.ReceivableReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static net.myspring.cloud.common.utils.Const.empty;

/**
 * Created by lihx on 2016/12/19.
 */
@Service
@KingdeeDataSource
public class ReceivableReportService {
    @Autowired
    private ReceivableReportMapper receivableReportMapper;
    @Autowired
    private BdCustomerMapper bdCustomerMapper;

    public List<ReceivableForSummaryDto> getSummaryList(LocalDate dateStart, LocalDate dateEnd){
        List<ReceivableForSummaryDto> tempList = Lists.newLinkedList();
        List<ReceivableForSummaryDto> dataForStartDate = receivableReportMapper.findByEndDate(dateStart);
        List<ReceivableForSummaryDto> dataForEndDate = receivableReportMapper.findByEndDate(dateEnd.plusDays(1));
        //期初结余
        Map<String,BigDecimal> dateStartMap = Maps.newHashMap();
        for(ReceivableForSummaryDto startItem : dataForStartDate){
            String key1 = startItem.getCustomerId();
            dateStartMap.put(key1,startItem.getBeginAmount());
        }
        //期末结余
        for(ReceivableForSummaryDto endItem : dataForEndDate){
            ReceivableForSummaryDto summaryModel = new ReceivableForSummaryDto();
            summaryModel.setCustomerId(endItem.getCustomerId());
            summaryModel.setEndAmount(endItem.getBeginAmount());
            String key2 = endItem.getCustomerId();
            if(dateStartMap.containsKey(key2)){
                summaryModel.setBeginAmount(dateStartMap.get(key2));
            }else{
                summaryModel.setBeginAmount(BigDecimal.ZERO);
            }
            tempList.add(summaryModel);
        }
        List<ReceivableForSummaryDto> QTYSDListForPeriodList = receivableReportMapper.findQTYSDByPeriodForSum(dateStart, dateEnd);
        List<ReceivableForSummaryDto> XSTHDListForPeriodList = receivableReportMapper.findXSTHDByPeriodForSum(dateStart, dateEnd);
        List<ReceivableForSummaryDto> XSCKDListForPeriodList = receivableReportMapper.findXSCKDByPeriodForSum(dateStart, dateEnd);
        List<ReceivableForSummaryDto> SKDForPeriodList = receivableReportMapper.findSKDByPeriodForSum(dateStart, dateEnd);
        List<ReceivableForSummaryDto> SKTKDForPeriodList = receivableReportMapper.findSKTKDByPeriodForSum(dateStart, dateEnd);
        for(ReceivableForSummaryDto item : tempList) {
            String key = item.getCustomerId();
            BigDecimal QTYSDAmount = BigDecimal.ZERO;
            BigDecimal XSTHDAmount = BigDecimal.ZERO;
            BigDecimal XSCKDAmount = BigDecimal.ZERO;
            BigDecimal SKDAmount = BigDecimal.ZERO;
            BigDecimal SKTKDAmount = BigDecimal.ZERO;
            for (ReceivableForSummaryDto itemFor : QTYSDListForPeriodList) {
                String keyFor = itemFor.getCustomerId();
                if (key.equals(keyFor)) {
                    QTYSDAmount = itemFor.getBeginAmount();
                }
            }
            for (ReceivableForSummaryDto itemFor : XSTHDListForPeriodList) {
                String keyFor = itemFor.getCustomerId();
                if (key.equals(keyFor)) {
                    XSTHDAmount = itemFor.getBeginAmount();
                }
            }
            for (ReceivableForSummaryDto itemFor : XSCKDListForPeriodList) {
                String keyFor = itemFor.getCustomerId();
                if (key.equals(keyFor)) {
                    XSCKDAmount = itemFor.getBeginAmount();
                }
            }
            for (ReceivableForSummaryDto itemFor : SKDForPeriodList) {
                String keyFor = itemFor.getCustomerId();
                if (key.equals(keyFor)) {
                    SKDAmount = itemFor.getBeginAmount();
                }
            }
            for (ReceivableForSummaryDto itemFor : SKTKDForPeriodList) {
                String keyFor = itemFor.getCustomerId();
                if (key.equals(keyFor)) {
                    SKTKDAmount = itemFor.getBeginAmount();
                }
            }
            //应收
            item.setPayableAmount(QTYSDAmount.add(XSCKDAmount.subtract(XSTHDAmount)));
            //实收
            item.setActualPayAmount(SKDAmount.subtract(SKTKDAmount));
        }
        List<BdCustomer> customerList =  bdCustomerMapper.findAll();
        for (ReceivableForSummaryDto summary : tempList){
            for(BdCustomer customer : customerList){
                if(summary.getCustomerId().equals(customer.getfCustId())) {
                    summary.setCustomerName(customer.getfName());
                    summary.setPrimaryGroup(customer.getfPrimaryGroup());
                    summary.setPrimaryGroupName(customer.getfPrimaryGroupName());
                }
            }
        }
        return tempList;
    }

    public List<ReceivableForDetailDto> getDetailList(LocalDate dateStart, LocalDate dateEnd, String customerId){
        List<ReceivableForDetailDto> dataList = Lists.newArrayList();
        List<ReceivableForSummaryDto> summaryItemList = receivableReportMapper.findByEndDateAndIn(dateStart,Lists.newArrayList(customerId));
        ReceivableForSummaryDto summaryItem = new ReceivableForSummaryDto();
        summaryItem.setCustomerId(customerId);
        summaryItem.setCustomerName(bdCustomerMapper.findById(customerId).getfName());
        summaryItem.setBeginAmount(BigDecimal.ZERO);
        for(ReceivableForSummaryDto item : summaryItemList){
            if(item.getCustomerId() != null){
                summaryItem = item;
            }
        }
        List<ReceivableForDetailDto> detailForBillList = Lists.newArrayList();
        List<ReceivableForDetailDto> QTYSDByPeriodForEntrySumList = receivableReportMapper.findQTYSDByPeriodForEntrySum(dateStart, dateEnd,customerId);
        List<ReceivableForDetailDto> XSTHDByPeriodForMaterialSumList = receivableReportMapper.findXSTHDByPeriodForEntryFSum(dateStart, dateEnd,customerId);
        List<ReceivableForDetailDto> XSCKDByPeriodForMaterialSumList = receivableReportMapper.findXSCKDByPeriodForEntryFSum(dateStart, dateEnd,customerId);
        List<ReceivableForDetailDto> XXTHDByPeriodForMaterialSumList = receivableReportMapper.findXXTHDByPeriodForEntryFSum(dateStart, dateEnd,customerId);
        List<ReceivableForDetailDto> XXCKDByPeriodForMaterialSumList = receivableReportMapper.findXXCKDByPeriodForEntryFSum(dateStart, dateEnd,customerId);
        List<ReceivableForDetailDto> SKDForPeriodList = receivableReportMapper.findSKDByPeriodForEntrySum(dateStart, dateEnd,customerId);
        List<ReceivableForDetailDto> SKTKDForPeriodList = receivableReportMapper.findSKTKDByPeriodForEntrySum(dateStart, dateEnd,customerId);
        detailForBillList.addAll(QTYSDByPeriodForEntrySumList);
        detailForBillList.addAll(XSTHDByPeriodForMaterialSumList);
        detailForBillList.addAll(XSCKDByPeriodForMaterialSumList);
        detailForBillList.addAll(XXTHDByPeriodForMaterialSumList);
        detailForBillList.addAll(XXCKDByPeriodForMaterialSumList);
        detailForBillList.addAll(SKDForPeriodList);
        detailForBillList.addAll(SKTKDForPeriodList);
        //按日期排序
        List<ReceivableForDetailDto> detailForBillByDateList = Lists.newArrayList();
        LocalDate tempDate = dateStart;
        while(tempDate.isBefore(dateEnd) || tempDate.equals(dateEnd)){
            for(ReceivableForDetailDto receivableReportForDetail : detailForBillList) {
                if (receivableReportForDetail.getDate().equals(tempDate)) {
                    detailForBillByDateList.add(receivableReportForDetail);
                }
            }
            tempDate = tempDate.plusDays(1);
        }
      //物料详细
        List<ReceivableForDetailDto> detailForMaterialList = Lists.newArrayList();
        List<ReceivableForDetailDto> QTYSDByPeriodForEntryList = receivableReportMapper.findQTYSDByPeriodForEntry(dateStart, dateEnd,customerId);
        List<ReceivableForDetailDto> XSTHDListForPeriodList = receivableReportMapper.findXSTHDByPeriodForEntryF(dateStart, dateEnd,customerId);
        List<ReceivableForDetailDto> XSCKDListForPeriodList = receivableReportMapper.findXSCKDByPeriodForEntryF(dateStart, dateEnd,customerId);
        List<ReceivableForDetailDto> XXTHDListForPeriodList = receivableReportMapper.findXXTHDByPeriodForEntryF(dateStart, dateEnd,customerId);
        List<ReceivableForDetailDto> XXCKDListForPeriodList = receivableReportMapper.findXXCKDByPeriodForEntryF(dateStart, dateEnd,customerId);
        List<ReceivableForDetailDto> SKDByPeriodForEntryList = receivableReportMapper.findSKDByPeriodForEntry(dateStart, dateEnd,customerId);
        List<ReceivableForDetailDto> SKTKDByPeriodForEntryList = receivableReportMapper.findSKTKDByPeriodForEntry(dateStart, dateEnd,customerId);
        detailForMaterialList.addAll(QTYSDByPeriodForEntryList);
        detailForMaterialList.addAll(XSTHDListForPeriodList);
        detailForMaterialList.addAll(XSCKDListForPeriodList);
        detailForMaterialList.addAll(XXTHDListForPeriodList);
        detailForMaterialList.addAll(XXCKDListForPeriodList);
        detailForMaterialList.addAll(SKDByPeriodForEntryList);
        detailForMaterialList.addAll(SKTKDByPeriodForEntryList);
        ReceivableForDetailDto head = new ReceivableForDetailDto(summaryItem.getCustomerName(),summaryItem.getCustomerId());
        head.setCss("");
        dataList.add(head);

        ReceivableForDetailDto beginAmount = new ReceivableForDetailDto();
        beginAmount.setBillType("期初应收");
        beginAmount.setEndAmount(summaryItem.getBeginAmount());
        beginAmount.setCss("warning");
        dataList.add(beginAmount);
        BigDecimal endShouldGet = summaryItem.getBeginAmount();
        for(int i = 0 ;i<detailForBillByDateList.size();i++){
            String css = "";
            if(i % 2 != 0){
                css = "info";
            }
            ReceivableForDetailDto detailForBill = detailForBillByDateList.get(i);
            if(!"C".equals(detailForBill.getDocumentStatus())){
                css = "danger";
            }
            ReceivableForDetailDto receivableDetail = new ReceivableForDetailDto();
            receivableDetail.setCss(css);
            receivableDetail.setBillType(detailForBill.getBillType());
            receivableDetail.setDate(detailForBill.getDate());
            receivableDetail.setBillNo(detailForBill.getBillNo());
            receivableDetail.setNote(detailForBill.getNote());
            //实收
            if(detailForBill.getBillType().equals(ReceivableBillTypeEnum.销售收款单.name())){
                receivableDetail.setActualPayAmount(detailForBill.getAmount());
                endShouldGet = endShouldGet.subtract(receivableDetail.getActualPayAmount());
                receivableDetail.setEndAmount(endShouldGet);
                dataList.add(receivableDetail);
            }else if(detailForBill.getBillType().equals(ReceivableBillTypeEnum.销售业务退款单.name())){
                receivableDetail.setActualPayAmount(BigDecimal.ZERO.subtract(detailForBill.getAmount()));
                endShouldGet = endShouldGet.subtract(receivableDetail.getActualPayAmount());
                receivableDetail.setEndAmount(endShouldGet);
                dataList.add(receivableDetail);
            //不计入期末期初
            }else if(detailForBill.getBillType().equals(ReceivableBillTypeEnum.现销退货单.name())) {
                receivableDetail.setPayableAmount(BigDecimal.ZERO.subtract(detailForBill.getAmount()));
                receivableDetail.setEndAmount(endShouldGet.add(receivableDetail.getPayableAmount()));
                dataList.add(receivableDetail);
            }else if(detailForBill.getBillType().equals(ReceivableBillTypeEnum.现销出库单.name())){
                receivableDetail.setPayableAmount(detailForBill.getAmount());
                receivableDetail.setEndAmount(endShouldGet.add(receivableDetail.getPayableAmount()));
                dataList.add(receivableDetail);
             //应收
            }else if(detailForBill.getBillType().equals(ReceivableBillTypeEnum.标准销售退货单.name())) {
                receivableDetail.setPayableAmount(BigDecimal.ZERO.subtract(detailForBill.getAmount()));
                endShouldGet = endShouldGet.add(receivableDetail.getPayableAmount());
                receivableDetail.setEndAmount(endShouldGet);
                dataList.add(receivableDetail);
            }else{
                receivableDetail.setPayableAmount(detailForBill.getAmount());
                endShouldGet = endShouldGet.add(receivableDetail.getPayableAmount());
                receivableDetail.setEndAmount(endShouldGet);
                dataList.add(receivableDetail);
            }
            for (ReceivableForDetailDto detailMaterial : detailForMaterialList) {
                if (detailMaterial.getBillNo().equals(detailForBill.getBillNo()) && detailMaterial.getBillType().equals(detailForBill.getBillType())) {
                    if(detailMaterial.getBillNo().contains("XSTHD")){
                        detailMaterial.setQuantity(BigDecimal.ZERO.subtract(detailMaterial.getQuantity()));
                    }else if(detailMaterial.getQuantity() != null){
                        detailMaterial.setQuantity(detailMaterial.getQuantity());
                    }
                    detailMaterial.setBillType(empty);
                    detailMaterial.setCss(css);
                    dataList.add(detailMaterial);
                }
            }
        }
        ReceivableForDetailDto endAmount = new ReceivableForDetailDto();
        endAmount.setBillType("期末应收");
        endAmount.setEndAmount(endShouldGet);
        endAmount.setCss("warning");
        dataList.add(endAmount);
        return dataList;
  }

    public List<ReceivableForSummaryDto> findShouldGet(List<String> customerIds, LocalDate dateEnd){
        return receivableReportMapper.findByEndDateAndIn(dateEnd,customerIds);
    }
}
