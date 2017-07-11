package net.myspring.cloud.modules.report.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.BillTypeEnum;
import net.myspring.cloud.modules.kingdee.domain.BdDepartment;
import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import net.myspring.cloud.modules.kingdee.domain.BdSupplier;
import net.myspring.cloud.modules.kingdee.repository.BdDepartmentRepository;
import net.myspring.cloud.modules.kingdee.repository.BdMaterialRepository;
import net.myspring.cloud.modules.kingdee.repository.BdSupplierRepository;
import net.myspring.cloud.modules.report.dto.SupplierPayableDetailDto;
import net.myspring.cloud.modules.report.dto.SupplierPayableDto;
import net.myspring.cloud.modules.report.repository.SupplierPayableZMDRepository;
import net.myspring.cloud.modules.report.web.query.SupplierPayableDetailQuery;
import net.myspring.cloud.modules.report.web.query.SupplierPayableQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 供应商-应付(ZMD)
 * Created by lihx on 2017/6/29.
 */
@Service
@KingdeeDataSource
@Transactional(readOnly = true)
public class SupplierPayableZMDService {
    @Autowired
    private SupplierPayableZMDRepository supplierPayableZMDRepository;
    @Autowired
    private BdSupplierRepository bdSupplierRepository;
    @Autowired
    private BdDepartmentRepository bdDepartmentRepository;
    @Autowired
    private BdMaterialRepository bdMaterialRepository;

    public List<SupplierPayableDto> findSupplierPayableDtoList(SupplierPayableQuery supplierPayableQuery) {
        LocalDate dateStart = supplierPayableQuery.getDateStart();
        LocalDate dateEnd = supplierPayableQuery.getDateEnd();
        List<String> supplierIdList = supplierPayableQuery.getSupplierIdList();
        List<String> departmentIdList = supplierPayableQuery.getDepartmentIdList();
        List<SupplierPayableDto> supplierPayableDtoList = Lists.newArrayList();
        if (supplierIdList.size() > 0 || departmentIdList.size() > 0 && dateStart != null && dateEnd != null) {
            //期初结余：采购入库单-采购退料单+应付单+其他应付单-付款单+付款退款单
            List<SupplierPayableDto> startPayList = supplierPayableZMDRepository.findEndPayable(dateStart, supplierIdList, departmentIdList);
            Map<String, SupplierPayableDto> startPayKeyMap = Maps.newHashMap();
            for (SupplierPayableDto startPay : startPayList) {
                String key = startPay.getSupplierId()+CharConstant.COMMA + startPay.getDepartmentId();
                startPayKeyMap.put(key, startPay);
            }
            //期末结余
            List<SupplierPayableDto> endPayList = supplierPayableZMDRepository.findEndPayable(dateEnd.plusDays(1), supplierIdList, departmentIdList);
            Map<String, SupplierPayableDto> endPayKeyMap = Maps.newHashMap();
            for (SupplierPayableDto endPay : endPayList) {
                String key = endPay.getSupplierId()+CharConstant.COMMA + endPay.getDepartmentId();
                endPayKeyMap.put(key, endPay);
                SupplierPayableDto supplierPayable = new SupplierPayableDto();
                supplierPayable.setSupplierId(endPay.getSupplierId());
                supplierPayable.setDepartmentId(endPay.getDepartmentId());
                supplierPayable.setEndAmount(endPay.getBeginAmount());
                if (startPayKeyMap.containsKey(key)) {
                    supplierPayable.setBeginAmount(startPayKeyMap.get(key).getBeginAmount());
                } else {
                    supplierPayable.setBeginAmount(BigDecimal.ZERO);
                }
                supplierPayableDtoList.add(supplierPayable);
            }
            for (Map.Entry<String, SupplierPayableDto> startPayMap : startPayKeyMap.entrySet()) {
                if (!endPayKeyMap.containsKey(startPayMap.getKey())) {
                    SupplierPayableDto supplierPayable = new SupplierPayableDto();
                    supplierPayable.setSupplierId(startPayMap.getValue().getSupplierId());
                    supplierPayable.setDepartmentId(startPayMap.getValue().getDepartmentId());
                    supplierPayable.setBeginAmount(startPayMap.getValue().getBeginAmount());
                    supplierPayable.setEndAmount(BigDecimal.ZERO);
                    supplierPayableDtoList.add(supplierPayable);
                }
            }
            //应付金额：采购入库单-采购退料单+其他应付单+应付单
            List<SupplierPayableDto> payableList = supplierPayableZMDRepository.findShouldPay(supplierPayableQuery);
            Map<String, BigDecimal> payableKeyMap = Maps.newHashMap();
            for (SupplierPayableDto supplierPayableDto : payableList) {
                String key = supplierPayableDto.getSupplierId()+CharConstant.COMMA + supplierPayableDto.getDepartmentId();
                payableKeyMap.put(key, supplierPayableDto.getBeginAmount());
            }
            //实付金额：付款单-付款退款单
            List<SupplierPayableDto> actualPayList = supplierPayableZMDRepository.findActualPay(supplierPayableQuery);
            Map<String, BigDecimal> actualPayKeyMap = Maps.newHashMap();
            for (SupplierPayableDto supplierPayableDto : actualPayList) {
                String key = supplierPayableDto.getSupplierId()+CharConstant.COMMA + supplierPayableDto.getDepartmentId();
                actualPayKeyMap.put(key, supplierPayableDto.getBeginAmount());
            }
            Map<String, BdSupplier> bdSupplierIdMap;
            if (supplierIdList.size()>0){
                bdSupplierIdMap = bdSupplierRepository.findBySupplierIdList(supplierIdList).stream().collect(Collectors.toMap(BdSupplier::getFSupplierId, BdSupplier -> BdSupplier));
            }else {
                bdSupplierIdMap = bdSupplierRepository.findAll().stream().collect(Collectors.toMap(BdSupplier::getFSupplierId, BdSupplier -> BdSupplier));
            }
            Map<String, BdDepartment> bdDepartmentIdMap = Maps.newHashMap();
            if (departmentIdList.size()>0){
                 bdDepartmentIdMap = bdDepartmentRepository.findAll().stream().collect(Collectors.toMap(BdDepartment::getFDeptId, BdDepartment -> BdDepartment));
            }
            for (SupplierPayableDto supplierPayable : supplierPayableDtoList) {
                String key = supplierPayable.getSupplierId()+CharConstant.COMMA + supplierPayable.getDepartmentId();
                supplierPayable.setSupplierName(bdSupplierIdMap.get(supplierPayable.getSupplierId()).getFName());
                if (bdDepartmentIdMap.size()>0){
                    supplierPayable.setDepartmentName(bdDepartmentIdMap.get(supplierPayable.getDepartmentId()).getFFullName());
                }
                supplierPayable.setPayableAmount(payableKeyMap.get(key));
                supplierPayable.setActualPayAmount(actualPayKeyMap.get(key));
                if (supplierPayableQuery.getQueryDetail()) {
                    SupplierPayableDetailQuery supplierPayableDetailQuery = new SupplierPayableDetailQuery();
                    supplierPayableDetailQuery.setSupplierIdList(supplierPayableQuery.getSupplierIdList());
                    supplierPayableDetailQuery.setDepartmentIdList(supplierPayableQuery.getDepartmentIdList());
                    supplierPayableDetailQuery.setDateStart(supplierPayableQuery.getDateStart());
                    supplierPayableDetailQuery.setDateEnd(supplierPayableQuery.getDateEnd());
                    Map<String,List<SupplierPayableDetailDto>> supplierPayableDetailDtoMap = findSupplierPayableDetailDtoMap(supplierPayableDetailQuery);
                    supplierPayable.setSupplierPayableDetailDtoList(supplierPayableDetailDtoMap.get(key));
                }
            }
            return supplierPayableDtoList;
        }
        return null;
    }

    public List<SupplierPayableDetailDto> findSupplierPayableDetailDtoList(SupplierPayableDetailQuery supplierPayableDetailQuery) {
        List<SupplierPayableDetailDto> detailDtoList = Lists.newArrayList();
        List<String> supplierIdList = supplierPayableDetailQuery.getSupplierIdList();
        List<String> departmentIdList = supplierPayableDetailQuery.getDepartmentIdList();
        Map<String,List<SupplierPayableDetailDto>> map = findSupplierPayableDetailDtoMap(supplierPayableDetailQuery);
        if (map.size() > 0){
            for (String departmentId : departmentIdList){
                for (String supplierId : supplierIdList){
                    String key = supplierId+CharConstant.COMMA+departmentId;
                    detailDtoList.addAll(map.get(key));
                }
            }
        }
        return detailDtoList;
    }

    //一个supplierId+departmentId对应List<CustomerReceiveDetailDto>
    public Map<String, List<SupplierPayableDetailDto>> findSupplierPayableDetailDtoMap(SupplierPayableDetailQuery supplierPayableDetailQuery) {
        LocalDate dateStart = supplierPayableDetailQuery.getDateStart();
        List<String> supplierIdList = supplierPayableDetailQuery.getSupplierIdList();
        List<String> departmentIdList = supplierPayableDetailQuery.getDepartmentIdList();
        //期初应收
        List<SupplierPayableDto> beginList = supplierPayableZMDRepository.findEndPayable(dateStart,supplierIdList,departmentIdList);
        //根据key组织成map
        Map<String,BigDecimal> keyToBeginAmountMap = Maps.newHashMap();
        for (SupplierPayableDto supplierPayableDto : beginList){
            String key = supplierPayableDto.getSupplierId()+CharConstant.COMMA+supplierPayableDto.getDepartmentId();
            keyToBeginAmountMap.put(key,supplierPayableDto.getBeginAmount());
        }
        //主单--采购入库单sum+采购退料单sum+应付单sum+付款单+付款退款单+其他应付单
        List<SupplierPayableDetailDto> detailForBillList = supplierPayableZMDRepository.findMainList(supplierPayableDetailQuery);
        //根据key组织成map
        Map<String, List<SupplierPayableDetailDto>> keyToMainBillMap = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(detailForBillList)) {
            for (SupplierPayableDetailDto supplierPayableDetailDto: detailForBillList) {
                String key = supplierPayableDetailDto.getSupplierId()+CharConstant.COMMA+supplierPayableDetailDto.getDepartmentId();
                if (!keyToMainBillMap.containsKey(key)) {
                    keyToMainBillMap.put(key, new ArrayList<>());
                }
                keyToMainBillMap.get(key).add(supplierPayableDetailDto);
            }
        }
        //有物料的
        List<SupplierPayableDetailDto> detailForMaterialList = supplierPayableZMDRepository.findDetailList(supplierPayableDetailQuery);
        //根据BillNo组织成map
        Map<String, List<SupplierPayableDetailDto>> billNoToDetailBillMap = Maps.newHashMap();
        List<String> materialIdList = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(detailForMaterialList)) {
            for (SupplierPayableDetailDto supplierPayableDetailDto: detailForMaterialList) {
                String billNo = supplierPayableDetailDto.getBillNo();
                if (!billNoToDetailBillMap.containsKey(billNo)) {
                    billNoToDetailBillMap.put(billNo, new ArrayList<>());
                }
                billNoToDetailBillMap.get(billNo).add(supplierPayableDetailDto);
                materialIdList.add(supplierPayableDetailDto.getMaterialId());
            }
        }
        Map<String,BdSupplier> bdSupplierIdMap;
        if (CollectionUtil.isNotEmpty(supplierIdList)){
            bdSupplierIdMap = bdSupplierRepository.findBySupplierIdList(supplierIdList).stream().collect(Collectors.toMap(BdSupplier::getFSupplierId,BdSupplier->BdSupplier));
        }else {
            bdSupplierIdMap = bdSupplierRepository.findAll().stream().collect(Collectors.toMap(BdSupplier::getFSupplierId,BdSupplier->BdSupplier));
        }
        Map<String,BdDepartment> bdDepartmentIdMap = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(departmentIdList)) {
            bdDepartmentIdMap =  bdDepartmentRepository.findByIdList(departmentIdList).stream().collect(Collectors.toMap(BdDepartment::getFDeptId,BdDepartment->BdDepartment));
        }
        Map<String,BdMaterial> materialIdMap = Maps.newHashMap();
        if (materialIdList.size()>0){
            materialIdMap = bdMaterialRepository.findByMasterIdList(materialIdList).stream().collect(Collectors.toMap(BdMaterial::getFMasterId,BdMaterial->BdMaterial));
        }
        Map<String,List<SupplierPayableDetailDto>> result = Maps.newHashMap();
        if (keyToMainBillMap.size()>0) {
            for (String key : keyToMainBillMap.keySet()) {
                if(!result.containsKey(key)) {
                    result.put(key,Lists.newArrayList());
                }
                List<SupplierPayableDetailDto> list = result.get(key);
                BigDecimal beginAmount = keyToBeginAmountMap.get(key);
                if (beginAmount == null){
                    beginAmount = BigDecimal.ZERO;
                }
                int index = 0;
                SupplierPayableDetailDto supplierPayableDetailDto = new SupplierPayableDetailDto();
                supplierPayableDetailDto.setBillType(bdSupplierIdMap.get(key.split(CharConstant.COMMA)[0]).getFName());
                if (bdDepartmentIdMap.size()>0){
                    supplierPayableDetailDto.setBillNo(bdDepartmentIdMap.get(key.split(CharConstant.COMMA)[1]).getFFullName());
                }
                supplierPayableDetailDto.setIndex(index++);
                list.add(supplierPayableDetailDto);

                supplierPayableDetailDto = new SupplierPayableDetailDto();
                supplierPayableDetailDto.setBillType("期初应付");
                supplierPayableDetailDto.setEndAmount(beginAmount);
                supplierPayableDetailDto.setIndex(index++);
                list.add(supplierPayableDetailDto);
                for (int i = 0; i < detailForBillList.size(); i++) {
                    SupplierPayableDetailDto main = detailForBillList.get(i);
                    supplierPayableDetailDto = new SupplierPayableDetailDto();
                    supplierPayableDetailDto.setBillType(main.getBillType());
                    supplierPayableDetailDto.setDate(main.getDate());
                    supplierPayableDetailDto.setBillNo(main.getBillNo());
                    supplierPayableDetailDto.setNote(main.getNote());
                    supplierPayableDetailDto.setDocumentStatus(main.getDocumentStatus());
                    supplierPayableDetailDto.setIndex(index++);
                    //应付
                    if (main.getBillType().equals(BillTypeEnum.标准采购入库.name())) {
                        supplierPayableDetailDto.setPayableAmount(main.getAmount());
                        beginAmount = beginAmount.add(supplierPayableDetailDto.getPayableAmount());
                        supplierPayableDetailDto.setEndAmount(beginAmount);
                        list.add(supplierPayableDetailDto);
                    } else if (main.getBillType().equals(BillTypeEnum.标准退料单.name())) {
                        supplierPayableDetailDto.setPayableAmount(BigDecimal.ZERO.subtract(main.getAmount()));
                        beginAmount = beginAmount.add(supplierPayableDetailDto.getPayableAmount());
                        supplierPayableDetailDto.setEndAmount(beginAmount);
                        list.add(supplierPayableDetailDto);
                    } else if (main.getBillType().equals(BillTypeEnum.标准应付单.name())) {
                        supplierPayableDetailDto.setPayableAmount(main.getAmount());
                        beginAmount = beginAmount.add(supplierPayableDetailDto.getPayableAmount());
                        supplierPayableDetailDto.setEndAmount(beginAmount);
                        list.add(supplierPayableDetailDto);
                    } else if (main.getBillType().equals(BillTypeEnum.其他应付单.name())) {
                        supplierPayableDetailDto.setMaterialName(main.getMaterialId());//费用名称
                        supplierPayableDetailDto.setQty(main.getQty());//费用编码
                        supplierPayableDetailDto.setPayableAmount(main.getAmount());
                        beginAmount = beginAmount.add(supplierPayableDetailDto.getPayableAmount());
                        supplierPayableDetailDto.setEndAmount(beginAmount);
                        list.add(supplierPayableDetailDto);
                    //实付
                    } else if (main.getBillType().equals(BillTypeEnum.采购业务付款单.name())) {
                        supplierPayableDetailDto.setActualPayAmount(main.getAmount());
                        beginAmount = beginAmount.subtract(supplierPayableDetailDto.getActualPayAmount());
                        supplierPayableDetailDto.setEndAmount(beginAmount);
                        list.add(supplierPayableDetailDto);
                    } else if (main.getBillType().equals(BillTypeEnum.采购业务退款单.name())) {
                        supplierPayableDetailDto.setActualPayAmount(BigDecimal.ZERO.subtract(main.getAmount()));
                        beginAmount = beginAmount.subtract(supplierPayableDetailDto.getActualPayAmount());
                        supplierPayableDetailDto.setEndAmount(beginAmount);
                        list.add(supplierPayableDetailDto);
                    }
                    if (billNoToDetailBillMap.containsKey(main.getBillNo())) {
                        for (SupplierPayableDetailDto detailMaterial : billNoToDetailBillMap.get(main.getBillNo())) {
                            supplierPayableDetailDto = new SupplierPayableDetailDto();
                            supplierPayableDetailDto.setBillNo(detailMaterial.getBillNo());
                            supplierPayableDetailDto.setMaterialName(materialIdMap.get(detailMaterial.getMaterialId()).getFName());
                            if (detailMaterial.getQty() != null && detailMaterial.getQty().intValue() != 0) {
                                supplierPayableDetailDto.setPrice(detailMaterial.getAmount().divide(detailMaterial.getQty(), 2, BigDecimal.ROUND_HALF_UP));
                                supplierPayableDetailDto.setQty(detailMaterial.getQty());
                            }
                            supplierPayableDetailDto.setAmount(detailMaterial.getAmount());
                            supplierPayableDetailDto.setNote(detailMaterial.getNote());
                            supplierPayableDetailDto.setDocumentStatus(detailMaterial.getDocumentStatus());
                            supplierPayableDetailDto.setIndex(index);
                            list.add(supplierPayableDetailDto);
                        }
                    }
                }
                supplierPayableDetailDto = new SupplierPayableDetailDto();
                supplierPayableDetailDto.setBillType("期末应付");
                supplierPayableDetailDto.setEndAmount(beginAmount);
                supplierPayableDetailDto.setIndex(index++);
                list.add(supplierPayableDetailDto);
            }
        }
        return result;
    }

    public SupplierPayableQuery getQuery(){
        return null;
    }

    //excel报错
    public SimpleExcelBook export(SupplierPayableQuery supplierPayableQuery){
        supplierPayableQuery.setQueryDetail(true);
        List<SupplierPayableDto> supplierPayableDtoList = findSupplierPayableDtoList(supplierPayableQuery);
        Workbook workbook = new SXSSFWorkbook(10000);
        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"supplierName", "供应商名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"departmentName", "门店名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"beginAmount", "期初应付"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"payableAmount", "应付金额"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"actualPayAmount", "实付金额"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"endAmount", "期末应付"));
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("应付汇总", supplierPayableDtoList, simpleExcelColumnList);
        simpleExcelSheetList.add(simpleExcelSheet);
        for(SupplierPayableDto supplierPayableDto : supplierPayableDtoList){
            List<SupplierPayableDetailDto> supplierPayableDetailDtoList = supplierPayableDto.getSupplierPayableDetailDtoList();
            if (CollectionUtil.isNotEmpty(supplierPayableDetailDtoList)){
                List<SimpleExcelColumn> columnList = Lists.newArrayList();
                CellStyle cellStyleRed = ExcelUtils.getCellStyleMap(workbook).get(ExcelCellStyle.RED.name());
                columnList.add(new SimpleExcelColumn(workbook,"billType", "业务类型"));
                columnList.add(new SimpleExcelColumn(workbook,"billNo", "单据编号"));
                columnList.add(new SimpleExcelColumn(workbook,"date", "单据日期"));
                columnList.add(new SimpleExcelColumn(workbook,"quantity", "商品名称"));
                columnList.add(new SimpleExcelColumn(workbook,"price", "数量"));
                columnList.add(new SimpleExcelColumn(workbook,"amount", "单价"));
                columnList.add(new SimpleExcelColumn(workbook,"payableAmount", "金额"));
                columnList.add(new SimpleExcelColumn(workbook,"actualPayAmount", "应付"));
                columnList.add(new SimpleExcelColumn(workbook,"endAmount", "实付"));
                columnList.add(new SimpleExcelColumn(workbook,"note", "期末"));
                SupplierPayableDetailDto supplierPayableDetailDto = supplierPayableDetailDtoList.get(0);
                SimpleExcelSheet excelSheet = new SimpleExcelSheet(supplierPayableDetailDto.getBillType()+"("+supplierPayableDetailDto.getBillNo()+")", supplierPayableDetailDtoList, columnList);
                simpleExcelSheetList.add(excelSheet);
            }
        }
        ExcelUtils.doWrite(workbook, simpleExcelSheetList);
        return new SimpleExcelBook(workbook,"应付款汇总报表"+ supplierPayableQuery.getDateStart()+"-"+supplierPayableQuery.getDateEnd()+".xlsx",simpleExcelSheetList);
    }
}
