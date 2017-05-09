package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.K3CloudBillTypeEnum;
import net.myspring.cloud.common.handsontable.HandSonTableUtils;
import net.myspring.cloud.common.utils.CacheUtils;
import net.myspring.cloud.modules.input.domain.BdCustomer;
import net.myspring.cloud.modules.input.domain.BdDepartment;
import net.myspring.cloud.modules.input.domain.BdMaterial;
import net.myspring.cloud.modules.input.dto.BatchBillDetailDto;
import net.myspring.cloud.modules.input.dto.BatchBillDto;
import net.myspring.cloud.modules.input.mapper.ArReceivableMapper;
import net.myspring.cloud.modules.input.mapper.BdCustomerMapper;
import net.myspring.cloud.modules.input.mapper.BdDepartmentMapper;
import net.myspring.cloud.modules.input.mapper.BdMaterialMapper;
import net.myspring.cloud.modules.input.web.query.BatchBillQuery;
import net.myspring.cloud.modules.sys.dto.AccountDto;
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
    private CacheUtils cacheUtils;

    //批量开单
    public List<String> save(List<List<Object>> datas, String storeFNumber, LocalDate billDate) {
        Map<String, String> customerMap = Maps.newHashMap();
        Map<String, String> customerIdMap = Maps.newHashMap();
        Map<String, String> materialMap = Maps.newHashMap();
        List<BdCustomer> bdCustomerList = bdCustomerMapper.findAll();
        for (BdCustomer bdCustomer : bdCustomerList) {
            customerMap.put(bdCustomer.getfName(), bdCustomer.getfNumber());
            customerIdMap.put(bdCustomer.getfName(), bdCustomer.getfCustId());
        }
        List<BdMaterial> bdMaterialList = bdMaterialMapper.findAll();
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
            String billKey = customerMap.get(customerName) + CharConstant.COMMA + type;
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
        AccountDto accountDto = new AccountDto();
        cacheUtils.initCacheInput(accountDto);
        if (CollectionUtil.isNotEmpty(batchBillDtoList)) {
            for (BatchBillDto batchBillDto : batchBillDtoList) {

            }
        }
        return codeList;
    }

    //批量销售出库单json
    private String getSaleOutStock(BatchBillDto batchBillDto,AccountDto accountDto) {
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", accountDto.getName());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FDate", batchBillDto.getDate());
        if (K3CloudBillTypeEnum.销售出库单.name().equals(batchBillDto.getType())) {
            model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "XSCKD01_SYS"));
        } else {
            model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "XSCKD06_SYS"));
        }
        model.put("FDeliveryDeptID", CollectionUtil.getMap("FNumber", batchBillDto.getDepartmentFNumber()));
        model.put("FSaleOrgId", CollectionUtil.getMap("FNumber", 100));
        model.put("FStockOrgId", CollectionUtil.getMap("FNumber", 100));
        model.put("FOwnerIdHead", CollectionUtil.getMap("FNumber", 100));
        model.put("FSettleCurrID", CollectionUtil.getMap("FNumber", "PRE001"));
        model.put("FCustomerID", CollectionUtil.getMap("FNumber", batchBillDto.getCustomerFNumber()));
        model.put("FNote", batchBillDto.getNote()+ "批量开单");
        List<Object> entity = Lists.newArrayList();
        for (BatchBillDetailDto batchBillDetail : batchBillDto.getBatchBillDetailDtoList()) {
            if (batchBillDetail.getQty() != null && batchBillDetail.getQty() > 0) {
                Map<String, Object> detail = Maps.newLinkedHashMap();
                detail.put("FMaterialId", CollectionUtil.getMap("FNumber", batchBillDetail.getProductFNumber()));
                detail.put("FStockID", CollectionUtil.getMap("FNumber", batchBillDto.getStoreFNumber()));
                detail.put("FStockStatusID", CollectionUtil.getMap("FNumber", "KCZT01_SYS"));
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
    private String getReturnStock(BatchBillDto batchBillDto,AccountDto accountDto) {
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", accountDto.getName());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FDate", batchBillDto.getDate());
        if (K3CloudBillTypeEnum.销售退货单.name().equals(batchBillDto.getType())) {
            model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "XSTHD01_SYS"));
        } else {
            model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "XSTHD06_SYS"));
        }
        model.put("FStockDeptId", CollectionUtil.getMap("FNumber", batchBillDto.getDepartmentFNumber()));
        model.put("FSaledeptid", CollectionUtil.getMap("FNumber", batchBillDto.getDepartmentFNumber()));
        model.put("FSaleOrgId", CollectionUtil.getMap("FNumber", 100));
        model.put("FStockOrgId", CollectionUtil.getMap("FNumber", 100));
        model.put("FOwnerIdHead", CollectionUtil.getMap("FNumber", 100));
        model.put("FRetcustId", CollectionUtil.getMap("FNumber", batchBillDto.getCustomerFNumber()));
        model.put("FHeadNote", batchBillDto.getNote()+ "批量开单");
        List<Object> entity = Lists.newArrayList();
        for (BatchBillDetailDto batchBillDetail : batchBillDto.getBatchBillDetailDtoList()) {
            if (batchBillDetail.getQty() != null && batchBillDetail.getQty() > 0) {
                Map<String, Object> detail = Maps.newLinkedHashMap();
                detail.put("FMaterialId", CollectionUtil.getMap("FNumber", batchBillDetail.getProductFNumber()));
                detail.put("FStockId", CollectionUtil.getMap("FNumber", batchBillDto.getStoreFNumber()));
                detail.put("FStockStatusID", CollectionUtil.getMap("FNumber", "KCZT01_SYS"));
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
        subHeadEntity.put("FLocalCurrId", CollectionUtil.getMap("FNumber", "PRE001"));
        subHeadEntity.put("FExchangeTypeId", CollectionUtil.getMap("FNumber", "HLTX01_SYS"));
        model.put("SAL_RETURNSTOCK__SubHeadEntity", subHeadEntity);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        return result;
    }

    public BatchBillQuery getFormProperty(BatchBillQuery batchBillQuery){
        batchBillQuery.setTypeList(K3CloudBillTypeEnum.values());
        return batchBillQuery;
    }
}
