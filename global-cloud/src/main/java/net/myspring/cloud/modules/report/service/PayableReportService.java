package net.myspring.cloud.modules.report.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.PayableBillTypeEnum;
import net.myspring.cloud.modules.input.domain.BdDepartment;
import net.myspring.cloud.modules.input.domain.BdMaterial;
import net.myspring.cloud.modules.input.domain.BdSupplier;
import net.myspring.cloud.modules.input.mapper.BdDepartmentMapper;
import net.myspring.cloud.modules.input.mapper.BdMaterialMapper;
import net.myspring.cloud.modules.input.mapper.BdSupplierMapper;
import net.myspring.cloud.modules.report.dto.PayableForDetailDto;
import net.myspring.cloud.modules.report.dto.PayableForSummaryDto;
import net.myspring.cloud.modules.report.mapper.PayableReportMapper;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@KingdeeDataSource
public class PayableReportService {
    @Autowired
    private PayableReportMapper payableReportMapper;
    @Autowired
    private BdSupplierMapper bdSupplierMapper;
    @Autowired
    private BdDepartmentMapper bdDepartmentMapper;
    @Autowired
    private BdMaterialMapper bdMaterialMapper;

    public List<PayableForDetailDto> getDetailList(LocalDate dateStart, LocalDate dateEnd, String supplierId, String departmentId ) {
        List<PayableForDetailDto> dataList = Lists.newArrayList();
        List<PayableForSummaryDto> payableForSummaryDtoList = payableReportMapper.findByEndDateAndIn(dateStart,Lists.newArrayList(supplierId),Lists.newArrayList(departmentId));
        PayableForSummaryDto summary = new PayableForSummaryDto();
        summary.setSupplierId(supplierId);
        summary.setSupplierName(bdSupplierMapper.findNameBySupplierId(supplierId));
        summary.setDepartmentId(departmentId);
        summary.setDepartmentName(bdDepartmentMapper.findNameByDepartmentId(departmentId));
        summary.setBeginAmount(BigDecimal.ZERO);
        for(PayableForSummaryDto payableForSummaryDto : payableForSummaryDtoList){
            if (payableForSummaryDto.getSupplierId() != null){
                summary.setBeginAmount(payableForSummaryDto.getBeginAmount());
            }
        }
        List<PayableForDetailDto> detailForBillList = Lists.newArrayList();
        List<PayableForDetailDto> cgrkdListForSumList = payableReportMapper.findCGRKDByPeriodForEntryFSum(dateStart, dateEnd,supplierId,departmentId);
        List<PayableForDetailDto> cgtldListForSum = payableReportMapper.findCGTLDByPeriodForEntryFSum(dateStart, dateEnd,supplierId,departmentId);
        List<PayableForDetailDto> yfdListForSum = payableReportMapper.findYFDByPeriodForEntrySum(dateStart, dateEnd,supplierId,departmentId);
        List<PayableForDetailDto> fkdList = payableReportMapper.findFKDByPeriodForEntry(dateStart, dateEnd,supplierId,departmentId);
        List<PayableForDetailDto> fktkdList = payableReportMapper.findFKTKDByPeriodForEntry(dateStart, dateEnd,supplierId,departmentId);
        List<PayableForDetailDto> qtyfdList = payableReportMapper.findQTYFDByPeriodForEntry(dateStart, dateEnd,supplierId,departmentId);
        detailForBillList.addAll(cgrkdListForSumList);
        detailForBillList.addAll(cgtldListForSum);
        detailForBillList.addAll(yfdListForSum);
        detailForBillList.addAll(fkdList);
        detailForBillList.addAll(fktkdList);
        detailForBillList.addAll(qtyfdList);
        //按日期排序
        List<PayableForDetailDto> detailForBillByDateList = Lists.newArrayList();
        LocalDate tempDate = dateStart;
        while(tempDate.isBefore(dateEnd) || tempDate.equals(dateEnd)){
            for(PayableForDetailDto payableReconciliationForDetail : detailForBillList) {
                if (payableReconciliationForDetail.getDate().equals(tempDate)) {
                    detailForBillByDateList.add(payableReconciliationForDetail);
                }
            }
            tempDate = tempDate.plusDays(1);
        }
        //有物料的
        List<PayableForDetailDto> detailForMaterialList = Lists.newArrayList();
        List<PayableForDetailDto> cgrkdList = payableReportMapper.findCGRKDByPeriodForEntryF(dateStart, dateEnd,supplierId,departmentId);
        List<PayableForDetailDto> cgtldList = payableReportMapper.findCGTLDByPeriodForEntryF(dateStart, dateEnd,supplierId,departmentId);
        List<PayableForDetailDto> yfdList = payableReportMapper.findYFDByPeriodForEntry(dateStart, dateEnd,supplierId,departmentId);
        detailForMaterialList.addAll(cgrkdList);
        detailForMaterialList.addAll(cgtldList);
        detailForMaterialList.addAll(yfdList);
        PayableForDetailDto head = new PayableForDetailDto();
        head.setBillType(summary.getSupplierName());
        head.setBillNo(summary.getDepartmentName());
        head.setCss("");
        dataList.add(head);
        
        BigDecimal beginShouldGive = summary.getBeginAmount();

        PayableForDetailDto beginAmount = new PayableForDetailDto();
        beginAmount.setBillType("期初应付");
        beginAmount.setEndAmount(beginShouldGive);
        beginAmount.setCss("warning");
        dataList.add(beginAmount);

        Map<String, List<PayableForDetailDto>> detailForMaterial = Maps.newHashMap();
        if(CollectionUtil.isNotEmpty(detailForBillList)){
            for(PayableForDetailDto detailMaterial : detailForMaterialList){
                if(!detailForMaterial.containsKey(detailMaterial.getBillNo())){
                    detailForMaterial.put(detailMaterial.getBillNo(),new ArrayList<PayableForDetailDto>());
                }
                detailForMaterial.get(detailMaterial.getBillNo()).add(detailMaterial);
            }
        }
        for(int i = 0 ;i<detailForBillByDateList.size();i++){
            String css = "";
            if(i % 2 != 0){
                css = "info";
            }
            PayableForDetailDto detailForBill = detailForBillByDateList.get(i);
            if(!"C".equals(detailForBill.getDocumentStatus())){
                css = "danger";
            }
            PayableForDetailDto payableDetail = new PayableForDetailDto();
            payableDetail.setCss(css);
            payableDetail.setBillType(detailForBill.getBillType());
            payableDetail.setDate(detailForBill.getDate());
            payableDetail.setBillNo(detailForBill.getBillNo());
            payableDetail.setNote(detailForBill.getNote());
            //应付
            if(detailForBill.getBillType().equals(PayableBillTypeEnum.标准采购入库.name())){
                payableDetail.setPayableAmount(detailForBill.getAmount());
                beginShouldGive = beginShouldGive.add(payableDetail.getPayableAmount());
                payableDetail.setEndAmount(beginShouldGive);
                dataList.add(payableDetail);
            }else if(detailForBill.getBillType().equals(PayableBillTypeEnum.标准退料单.name())){
                payableDetail.setPayableAmount(BigDecimal.ZERO.subtract(detailForBill.getAmount()));
                beginShouldGive = beginShouldGive.add(payableDetail.getPayableAmount());
                payableDetail.setEndAmount(beginShouldGive);
                dataList.add(payableDetail);
            }else if(detailForBill.getBillType().equals(PayableBillTypeEnum.标准应付单.name())) {
                payableDetail.setPayableAmount(detailForBill.getAmount());
                beginShouldGive = beginShouldGive.add(payableDetail.getPayableAmount());
                payableDetail.setEndAmount(beginShouldGive);
                dataList.add(payableDetail);
            }else if(detailForBill.getBillType().equals(PayableBillTypeEnum.其他应付单.name())) {
                payableDetail.setMaterialName(detailForBill.getMaterialId());//费用名称
                payableDetail.setQuantity(detailForBill.getQuantity());//费用编码
                payableDetail.setPayableAmount(detailForBill.getAmount());
                beginShouldGive = beginShouldGive.add(payableDetail.getPayableAmount());
                payableDetail.setEndAmount(beginShouldGive);
                dataList.add(payableDetail);
            //实付
            }else if(detailForBill.getBillType().equals(PayableBillTypeEnum.采购业务付款单.name())) {
                payableDetail.setActualPayAmount(detailForBill.getAmount());
                beginShouldGive = beginShouldGive.subtract(payableDetail.getActualPayAmount());
                payableDetail.setEndAmount(beginShouldGive);
                dataList.add(payableDetail);
            }else if(detailForBill.getBillType().equals(PayableBillTypeEnum.采购业务退款单.name())) {
                payableDetail.setActualPayAmount(BigDecimal.ZERO.subtract(detailForBill.getAmount()));
                beginShouldGive = beginShouldGive.subtract(payableDetail.getActualPayAmount());
                payableDetail.setEndAmount(beginShouldGive);
                dataList.add(payableDetail);
            }
            List<BdMaterial> materialList = bdMaterialMapper.findAll();
            Map<String,String> materialMap = Maps.newHashMap();
            for(BdMaterial material : materialList){
                materialMap.put(material.getfMasterId(),material.getfName());
            }
            if(detailForMaterial.containsKey(detailForBill.getBillNo())){
                for(PayableForDetailDto detailMaterial : detailForMaterial.get(detailForBill.getBillNo())){
                    PayableForDetailDto payableDetailForMaterial = new PayableForDetailDto();
                    payableDetailForMaterial.setBillNo(detailMaterial.getBillNo());
                    payableDetailForMaterial.setMaterialName(materialMap.get(detailMaterial.getMaterialId()));
                    if(detailMaterial.getQuantity() != null && detailMaterial.getQuantity().intValue() != 0){
                        payableDetailForMaterial.setPrice(detailMaterial.getAmount().divide(detailMaterial.getQuantity(), 2, BigDecimal.ROUND_HALF_UP));
                        payableDetailForMaterial.setQuantity(detailMaterial.getQuantity());
                    }
                    payableDetailForMaterial.setAmount(detailMaterial.getAmount());
                    payableDetailForMaterial.setNote(detailMaterial.getNote());
                    payableDetailForMaterial.setCss(css);
                    dataList.add(payableDetailForMaterial);
                }
            }
        }
        PayableForDetailDto endAmount = new PayableForDetailDto();
        endAmount.setBillType("期末应付");
        endAmount.setEndAmount(beginShouldGive);
        endAmount.setCss("warning");
        dataList.add(endAmount);
        return dataList;
    }

    public List<PayableForSummaryDto> getSummaryList(LocalDate dateStart, LocalDate dateEnd){
        List<PayableForSummaryDto> dataList = Lists.newArrayList();
        //期初结余：其他应收单+入库单-退料单-付款单+付款退款单
        List<PayableForSummaryDto> dateStartList = payableReportMapper.findByEndDate(dateStart);
        Map<String,BigDecimal> dateStartMap = Maps.newHashMap();
        for(PayableForSummaryDto Summary : dateStartList){
            String key = Summary.getSupplierId()+Summary.getDepartmentId();
            dateStartMap.put(key,Summary.getBeginAmount());
        }
        //期末结余
        List<PayableForSummaryDto> dateEndList = payableReportMapper.findByEndDate(dateEnd.plusDays(1));
        for(PayableForSummaryDto Summary : dateEndList){
            PayableForSummaryDto summary = new PayableForSummaryDto();
            summary.setSupplierId(Summary.getSupplierId());
            summary.setDepartmentId(Summary.getDepartmentId());
            summary.setEndAmount(Summary.getBeginAmount());
            String key = Summary.getSupplierId()+Summary.getDepartmentId();
            if(dateStartMap.containsKey(key)){
                summary.setBeginAmount(dateStartMap.get(key));
            }else{
                summary.setBeginAmount(BigDecimal.ZERO);
            }
            dataList.add(summary);
        }
        List<PayableForSummaryDto> payableForSummaryList = Lists.newArrayList();
        List<PayableForSummaryDto> CGRKDByPeriodForSumList = payableReportMapper.findCGRKDByPeriodForSum(dateStart, dateEnd);
        List<PayableForSummaryDto> CGTLDByPeriodForSumList = payableReportMapper.findCGTLDByPeriodForSum(dateStart, dateEnd);
        List<PayableForSummaryDto> QTYFDByPeriodForSumList = payableReportMapper.findQTYFDByPeriodForSum(dateStart, dateEnd);
        List<PayableForSummaryDto> YFDByPeriodForSumList = payableReportMapper.findYFDByPeriodForSum(dateStart, dateEnd);
        List<PayableForSummaryDto> FKDByPeriodForSumList = payableReportMapper.findFKDByPeriodForSum(dateStart, dateEnd);
        List<PayableForSummaryDto> FKTKDByPeriodForSumList = payableReportMapper.findFKTKDByPeriodForSum(dateStart, dateEnd);
        payableForSummaryList.addAll(CGRKDByPeriodForSumList);
        payableForSummaryList.addAll(CGTLDByPeriodForSumList);
        payableForSummaryList.addAll(QTYFDByPeriodForSumList);
        payableForSummaryList.addAll(YFDByPeriodForSumList);
        payableForSummaryList.addAll(FKDByPeriodForSumList);
        payableForSummaryList.addAll(FKTKDByPeriodForSumList);
        for(PayableForSummaryDto summary : dataList){
            String key = summary.getSupplierId()+summary.getDepartmentId();
            BigDecimal CGRKDAmount = BigDecimal.ZERO;
            BigDecimal CGTLDAmount = BigDecimal.ZERO;
            BigDecimal QTYFDAmount = BigDecimal.ZERO;
            BigDecimal YFDAmount = BigDecimal.ZERO;
            BigDecimal FKDAmount = BigDecimal.ZERO;
            BigDecimal FKTKDAmount = BigDecimal.ZERO;
            for(PayableForSummaryDto itemFor : CGRKDByPeriodForSumList){
                String keyFor = itemFor.getSupplierId()+itemFor.getDepartmentId();
                if (key.equals(keyFor)) {
                    CGRKDAmount = itemFor.getBeginAmount();
                }
            }
            for(PayableForSummaryDto itemFor : CGTLDByPeriodForSumList){
                String keyFor = itemFor.getSupplierId()+itemFor.getDepartmentId();
                if (key.equals(keyFor)) {
                    CGTLDAmount = itemFor.getBeginAmount();
                }
            }
            for(PayableForSummaryDto itemFor : QTYFDByPeriodForSumList){
                String keyFor = itemFor.getSupplierId()+itemFor.getDepartmentId();
                if (key.equals(keyFor)) {
                    QTYFDAmount = itemFor.getBeginAmount();
                }
            }
            for(PayableForSummaryDto itemFor : YFDByPeriodForSumList){
                String keyFor = itemFor.getSupplierId()+itemFor.getDepartmentId();
                if (key.equals(keyFor)) {
                    YFDAmount = itemFor.getBeginAmount();
                }
            }
            for(PayableForSummaryDto itemFor : FKDByPeriodForSumList){
                String keyFor = itemFor.getSupplierId()+itemFor.getDepartmentId();
                if (key.equals(keyFor)) {
                    FKDAmount = itemFor.getBeginAmount();
                }
            }
            for(PayableForSummaryDto itemFor : FKTKDByPeriodForSumList){
                String keyFor = itemFor.getSupplierId()+itemFor.getDepartmentId();
                if (key.equals(keyFor)) {
                    FKTKDAmount = itemFor.getBeginAmount();
                }
            }
            //应付金额：采购入库单-采购退料单+其他应付单+应付单
            summary.setPayableAmount(CGRKDAmount.subtract(CGTLDAmount).add(QTYFDAmount).add(YFDAmount));
            //实付金额：付款单-付款退款单
            summary.setActualPayAmount(FKDAmount.subtract(FKTKDAmount));
        }
        //根据id设置name
        List<BdSupplier> bdSupplierList = bdSupplierMapper.findAll();
        List<BdDepartment> bdDepartmentList = bdDepartmentMapper.findAll();
        for(PayableForSummaryDto summary : dataList){
            for(BdSupplier bdSupplier : bdSupplierList){
                if(bdSupplier.getfSupplierId().equals(summary.getSupplierId())) {
                    summary.setSupplierName(bdSupplier.getfName());
                    break;
                }
            }
            for(BdDepartment department : bdDepartmentList) {
                if (department.getfDeptId().equals(summary.getDepartmentId())) {
                    summary.setDepartmentName(department.getfName());
                    break;
                }
            }
        }
        return dataList;
    }
}
