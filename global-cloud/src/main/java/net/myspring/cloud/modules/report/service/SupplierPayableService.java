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
import net.myspring.cloud.modules.report.dto.CustomerReceiveDetailDto;
import net.myspring.cloud.modules.report.dto.SupplierPayableDetailDto;
import net.myspring.cloud.modules.report.dto.SupplierPayableDto;
import net.myspring.cloud.modules.report.repository.SupplierPayableRepository;
import net.myspring.cloud.modules.report.web.query.SupplierPayableDetailQuery;
import net.myspring.cloud.modules.report.web.query.SupplierPayableQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 供应商-应付
 * Created by lihx on 2017/6/29.
 */
@Service
@KingdeeDataSource
@Transactional
public class SupplierPayableService {
    @Autowired
    private SupplierPayableRepository supplierPayableRepository;
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
        if (supplierIdList.size() > 0 || departmentIdList.size() > 0) {
            //期初结余：采购入库单-采购退料单+应付单+其他应付单-付款单+付款退款单
            List<SupplierPayableDto> startPayList = supplierPayableRepository.findEndPayable(dateStart, supplierIdList, departmentIdList);
            Map<String, SupplierPayableDto> startPayKeyMap = Maps.newHashMap();
            for (SupplierPayableDto startPay : startPayList) {
                String key = startPay.getSupplierId()+CharConstant.COMMA + startPay.getDepartmentId();
                startPayKeyMap.put(key, startPay);
            }
            //期末结余
            List<SupplierPayableDto> endPayList = supplierPayableRepository.findEndPayable(dateEnd.plusDays(1), supplierIdList, departmentIdList);
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
            List<SupplierPayableDto> payableList = supplierPayableRepository.findShouldPay(supplierPayableQuery);
            Map<String, BigDecimal> payableKeyMap = Maps.newHashMap();
            for (SupplierPayableDto supplierPayableDto : payableList) {
                String key = supplierPayableDto.getSupplierId()+CharConstant.COMMA + supplierPayableDto.getDepartmentId();
                payableKeyMap.put(key, supplierPayableDto.getBeginAmount());
            }
            //实付金额：付款单-付款退款单
            List<SupplierPayableDto> actualPayList = supplierPayableRepository.findActualPay(supplierPayableQuery);
            Map<String, BigDecimal> actualPayKeyMap = Maps.newHashMap();
            for (SupplierPayableDto supplierPayableDto : actualPayList) {
                String key = supplierPayableDto.getSupplierId()+CharConstant.COMMA + supplierPayableDto.getDepartmentId();
                actualPayKeyMap.put(key, supplierPayableDto.getBeginAmount());
            }
            Map<String, BdSupplier> bdSupplierIdMap = bdSupplierRepository.findAll().stream().collect(Collectors.toMap(BdSupplier::getFSupplierId, BdSupplier -> BdSupplier));
            Map<String, BdDepartment> bdDepartmentIdMap = bdDepartmentRepository.findAll().stream().collect(Collectors.toMap(BdDepartment::getFDeptId, BdDepartment -> BdDepartment));
            for (SupplierPayableDto supplierPayable : supplierPayableDtoList) {
                String key = supplierPayable.getSupplierId()+CharConstant.COMMA + supplierPayable.getDepartmentId();
                supplierPayable.setSupplierName(bdSupplierIdMap.get(supplierPayable.getSupplierId()).getFName());
                supplierPayable.setDepartmentName(bdDepartmentIdMap.get(supplierPayable.getDepartmentId()).getFFullName());
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
            for (String supplierId : supplierIdList){
                for (String departmentId : departmentIdList){
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
        List<SupplierPayableDto> beginList = supplierPayableRepository.findEndPayable(dateStart,supplierIdList,departmentIdList);
        //根据key组织成map
        Map<String,BigDecimal> keyToBeginAmountMap = Maps.newHashMap();
        for (SupplierPayableDto supplierPayableDto : beginList){
            String key = supplierPayableDto.getSupplierId()+CharConstant.COMMA+supplierPayableDto.getDepartmentId();
            keyToBeginAmountMap.put(key,supplierPayableDto.getBeginAmount());
        }
        //主单--采购入库单sum+采购退料单sum+应付单sum+付款单+付款退款单+其他应付单
        List<SupplierPayableDetailDto> detailForBillList = supplierPayableRepository.findMainList(supplierPayableDetailQuery);
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
        List<SupplierPayableDetailDto> detailForMaterialList = supplierPayableRepository.findDetailList(supplierPayableDetailQuery);
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
        Map<String,BdSupplier> bdSupplierIdMap = bdSupplierRepository.findBySupplierIdList(supplierIdList).stream().collect(Collectors.toMap(BdSupplier::getFSupplierId,BdSupplier->BdSupplier));
        Map<String,BdDepartment> bdDepartmentIdMap = bdDepartmentRepository.findByIdList(departmentIdList).stream().collect(Collectors.toMap(BdDepartment::getFDeptId,BdDepartment->BdDepartment));
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
                supplierPayableDetailDto.setBillNo(bdDepartmentIdMap.get(key.split(CharConstant.COMMA)[1]).getFFullName());
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
}
