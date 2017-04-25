package net.myspring.cloud.modules.kingdee.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.CharEnum;
import net.myspring.cloud.common.enums.K3CloudBillTypeEnum;
import net.myspring.cloud.common.enums.K3CloudFormIdEnum;
import net.myspring.cloud.common.utils.*;
import net.myspring.cloud.modules.kingdee.client.AccountClient;
import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.kingdee.domain.BdDepartment;
import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import net.myspring.cloud.modules.kingdee.dto.BatchBillDetailDto;
import net.myspring.cloud.modules.kingdee.dto.BatchBillDto;
import net.myspring.cloud.modules.kingdee.mapper.ArReceivableMapper;
import net.myspring.cloud.modules.kingdee.mapper.BdCustomerMapper;
import net.myspring.cloud.modules.kingdee.mapper.BdDepartmentMapper;
import net.myspring.cloud.modules.kingdee.mapper.BdMaterialMapper;
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
 * Created by lihx on 2017/4/25.
 */
@Service
@KingdeeDataSource
public class BatchBillService {
    @Autowired
    private BdCustomerMapper bdCustomerMapper;
    @Autowired
    private BdMaterialMapper bdMaterialMapper;
    @Autowired
    private BdDepartmentMapper bdDepartmentMapper;
    @Autowired
    private ArReceivableMapper arReceivableMapper;
    @Autowired
    private AccountClient accountClient;
    @Autowired
    private SecurityUtils securityUtils;

    //批量开单
    public List<String> save(List<List<Object>> datas, String storeFNumber, LocalDate billDate) {
        Map<String, String> customerMap = Maps.newHashMap();
        Map<String, String> customerIdMap = Maps.newHashMap();
        Map<String, String> materialMap = Maps.newHashMap();
        List<BdCustomer> bdCustomerList = bdCustomerMapper.findAll(null);
        for (BdCustomer bdCustomer : bdCustomerList) {
            customerMap.put(bdCustomer.getfName(), bdCustomer.getfNumber());
            customerIdMap.put(bdCustomer.getfName(), bdCustomer.getfCustId());
        }
        List<BdMaterial> bdMaterialList = bdMaterialMapper.findAll(null);
        for (BdMaterial bdMaterial : bdMaterialList) {
            materialMap.put(bdMaterial.getfName(), bdMaterial.getfNumber());
        }
        Map<String, BatchBillDto> billMap = Maps.newLinkedHashMap();
        for (List<Object> row : datas) {
            BatchBillDetailDto batchBillDetail = new BatchBillDetailDto();
            String productCode = HandSonTableUtils.getValue(row,0);
            String customerName = HandSonTableUtils.getValue(row,1);
            String priceStr = HandSonTableUtils.getValue(row, 3);
            BigDecimal price = StringUtils.isEmpty(priceStr) ? BigDecimal.ZERO : new BigDecimal(priceStr);
            Integer qty = Integer.valueOf(HandSonTableUtils.getValue(row, 4));
            String type = HandSonTableUtils.getValue(row, 5);
            String remarks = HandSonTableUtils.getValue(row, 6);

            batchBillDetail.setProductFNumber(productCode);
            batchBillDetail.setPrice(price);
            batchBillDetail.setQty(qty);
            batchBillDetail.setRemarks(remarks);
            String billKey = customerMap.get(customerName) + CharEnum.COMMA + type;
            if (!billMap.containsKey(billKey)) {
                BatchBillDto batchBill = new BatchBillDto();
                batchBill.setCustomerFNumber(customerMap.get(customerName));
                batchBill.setStoreFNumber(storeFNumber);
                batchBill.setDate(billDate);
                batchBill.setType(type);
                batchBill.setNote(remarks);
                BdDepartment bdDepartment = bdDepartmentMapper.findByCustomerId(customerIdMap.get(customerName));
                batchBill.setDepartmentFNumber(bdDepartment.getfNumber());
                billMap.put(billKey, batchBill);
            }
            billMap.get(billKey).getBatchBillDetailDtoList().add(batchBillDetail);
        }
        List<BatchBillDto> batchBillDtoList = Lists.newArrayList(billMap.values());
        List<String> codeList = Lists.newArrayList();
        //财务出库开单
        if (CollectionUtil.isNotEmpty(batchBillDtoList)) {
            for (BatchBillDto batchBillDto : batchBillDtoList) {
                K3CloudSaveExtend k3CloudSaveExtend;
                if (K3CloudBillTypeEnum.销售出库单.name().equals(batchBillDto.getType()) || K3CloudBillTypeEnum.现销出库单.name().equals(batchBillDto.getType())) {
                    k3CloudSaveExtend = new K3CloudSaveExtend(K3CloudFormIdEnum.SAL_OUTSTOCK.name(), getSaleOutStock(batchBillDto), K3CloudFormIdEnum.AR_receivable.name()) {
                        @Override
                        public String getNextBillNo() {
                            return arReceivableMapper.findFBillNoByfSourceBillNo(getBillNo());
                        }
                    };
                } else {
                    k3CloudSaveExtend = new K3CloudSaveExtend(K3CloudFormIdEnum.SAL_RETURNSTOCK.name(), getReturnStock(batchBillDto), K3CloudFormIdEnum.AR_receivable.name()) {
                        @Override
                        public String getNextBillNo() {
                            return arReceivableMapper.findFBillNoByfSourceBillNo(getBillNo());
                        }
                    };
                }
                String billNo = K3cloudUtils.save(k3CloudSaveExtend).getBillNo();
                codeList.add(billNo);
            }
        }
        return codeList;
    }

    //批量销售出库单json
    private String getSaleOutStock(BatchBillDto batchBillDto) {
        Map<String, Object> root = Maps.newLinkedHashMap();
//        root.put("Creator", accountClient.getName());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FDate", batchBillDto.getDate());
        if (K3CloudBillTypeEnum.销售出库单.name().equals(batchBillDto.getType())) {
            model.put("FBillTypeID", K3cloudUtils.getMap("FNumber", "XSCKD01_SYS"));
        } else {
            model.put("FBillTypeID", K3cloudUtils.getMap("FNumber", "XSCKD06_SYS"));
        }
        model.put("FDeliveryDeptID", K3cloudUtils.getMap("FNumber", batchBillDto.getDepartmentFNumber()));
        model.put("FSaleOrgId", K3cloudUtils.getMap("FNumber", 100));
        model.put("FStockOrgId", K3cloudUtils.getMap("FNumber", 100));
        model.put("FOwnerIdHead", K3cloudUtils.getMap("FNumber", 100));
        model.put("FSettleCurrID", K3cloudUtils.getMap("FNumber", "PRE001"));
        model.put("FCustomerID", K3cloudUtils.getMap("FNumber", batchBillDto.getCustomerFNumber()));
        model.put("FNote", batchBillDto.getNote()+ "批量开单");
        List<Object> entity = Lists.newArrayList();
        for (BatchBillDetailDto batchBillDetail : batchBillDto.getBatchBillDetailDtoList()) {
            if (batchBillDetail.getQty() != null && batchBillDetail.getQty() > 0) {
                Map<String, Object> detail = Maps.newLinkedHashMap();
                detail.put("FMaterialId", K3cloudUtils.getMap("FNumber", batchBillDetail.getProductFNumber()));
                detail.put("FStockID", K3cloudUtils.getMap("FNumber", batchBillDto.getStoreFNumber()));
                detail.put("FStockStatusID", K3cloudUtils.getMap("FNumber", "KCZT01_SYS"));
                detail.put("FRealQty", batchBillDetail.getQty());
                detail.put("FBaseUnitQty", batchBillDetail.getQty());
                detail.put("FPriceUnitQty", batchBillDetail.getQty());
                detail.put("FTaxNetPrice", batchBillDetail.getPrice());
                detail.put("FSALBASEQTY", batchBillDetail.getQty());
                detail.put("FSALUNITQTY", batchBillDetail.getQty());
                detail.put("FPRICEBASEQTY", batchBillDetail.getQty());
                // 是否赠品
                detail.put("FIsFree", batchBillDetail.getPrice().compareTo(BigDecimal.ZERO) == 0 ? 1 : 0);
                detail.put("FPrice", batchBillDetail.getPrice());
                detail.put("FTaxPrice", batchBillDetail.getPrice());
                detail.put("FAmount", new BigDecimal(batchBillDetail.getQty()).multiply(batchBillDetail.getPrice()));
                detail.put("FBefDisAllAmt", new BigDecimal(batchBillDetail.getQty()).multiply(batchBillDetail.getPrice()));
                detail.put("FEntrynote", batchBillDetail.getRemarks());
                entity.add(detail);
            }
        }
        model.put("SAL_OUTSTOCK__FEntity", entity);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        return result;
    }

    //批量销售退货单json
    private String getReturnStock(BatchBillDto batchBillDto) {
        Map<String, Object> root = Maps.newLinkedHashMap();
//        root.put("Creator", ThreadLocalContext.get().getAccount().getLoginName());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FDate", batchBillDto.getDate());
        if (K3CloudBillTypeEnum.销售退货单.name().equals(batchBillDto.getType())) {
            model.put("FBillTypeID", K3cloudUtils.getMap("FNumber", "XSTHD01_SYS"));
        } else {
            model.put("FBillTypeID", K3cloudUtils.getMap("FNumber", "XSTHD06_SYS"));
        }
        model.put("FStockDeptId", K3cloudUtils.getMap("FNumber", batchBillDto.getDepartmentFNumber()));
        model.put("FSaledeptid", K3cloudUtils.getMap("FNumber", batchBillDto.getDepartmentFNumber()));
        model.put("FSaleOrgId", K3cloudUtils.getMap("FNumber", 100));
        model.put("FStockOrgId", K3cloudUtils.getMap("FNumber", 100));
        model.put("FOwnerIdHead", K3cloudUtils.getMap("FNumber", 100));
        model.put("FRetcustId", K3cloudUtils.getMap("FNumber", batchBillDto.getCustomerFNumber()));
        model.put("FHeadNote", batchBillDto.getNote()+ "批量开单");
        List<Object> entity = Lists.newArrayList();
        for (BatchBillDetailDto batchBillDetail : batchBillDto.getBatchBillDetailDtoList()) {
            if (batchBillDetail.getQty() != null && batchBillDetail.getQty() > 0) {
                Map<String, Object> detail = Maps.newLinkedHashMap();
                detail.put("FMaterialId", K3cloudUtils.getMap("FNumber", batchBillDetail.getProductFNumber()));
                detail.put("FStockId", K3cloudUtils.getMap("FNumber", batchBillDto.getStoreFNumber()));
                detail.put("FStockStatusID", K3cloudUtils.getMap("FNumber", "KCZT01_SYS"));
                detail.put("FRealQty", batchBillDetail.getQty());
                detail.put("FBaseunitQty", batchBillDetail.getQty());
                detail.put("FTaxPrice", batchBillDetail.getPrice());
                detail.put("FPrice", batchBillDetail.getPrice());
                detail.put("FPriceUnitQty", batchBillDetail.getQty());
                detail.put("FTaxNetPrice", batchBillDetail.getPrice());
                detail.put("FAmount", new BigDecimal(batchBillDetail.getQty()).multiply(batchBillDetail.getPrice()));
                detail.put("FAmount_LC", new BigDecimal(batchBillDetail.getQty()).multiply(batchBillDetail.getPrice()));
                detail.put("FAllAmount", new BigDecimal(batchBillDetail.getQty()).multiply(batchBillDetail.getPrice()));
                detail.put("FAllAmount_LC", new BigDecimal(batchBillDetail.getQty()).multiply(batchBillDetail.getPrice()));
                detail.put("FSalBaseQty", batchBillDetail.getQty());
                detail.put("FSalUnitQty", batchBillDetail.getQty());
                detail.put("FPriceBaseQty", batchBillDetail.getQty());
                detail.put("Fnote", batchBillDetail.getRemarks());
                entity.add(detail);
            }
        }
        model.put("SAL_RETURNSTOCK__FEntity", entity);

        Map<String, Object> subHeadEntity = Maps.newLinkedHashMap();
        subHeadEntity.put("FExchangeRate", 1);
        subHeadEntity.put("FLocalCurrId", K3cloudUtils.getMap("FNumber", "PRE001"));
        subHeadEntity.put("FExchangeTypeId", K3cloudUtils.getMap("FNumber", "HLTX01_SYS"));
        model.put("SAL_RETURNSTOCK__SubHeadEntity", subHeadEntity);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        return result;
    }
}
