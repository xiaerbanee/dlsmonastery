package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.KingdeeNameEnum;
import net.myspring.cloud.common.utils.CacheUtils;
import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.BatchPurchaseDto;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import net.myspring.cloud.modules.input.mapper.BdMaterialMapper;
import net.myspring.cloud.modules.sys.mapper.KingdeeBookMapper;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 采购入库
 * Created by lihx on 2016/11/2.
 */
@Service
@KingdeeDataSource
public class BatchPurchaseService {
    @Autowired
    private BdMaterialMapper bdMaterialMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private KingdeeBookMapper kingdeeBookMapper;
    
    //采购入库
    public List<String> saveInStock(LocalDate billDate, List<List<String>> dataList, String store, String supplier, String department)  {
        Map<String, BatchPurchaseDto> purchaseDtoMap = Maps.newLinkedHashMap();
        Map<String, String> productMap = Maps.newHashMap();
        for (NameNumberDto basicData : bdMaterialMapper.findNameAndNumber()) {
            productMap.put(basicData.getName(), basicData.getNumber());
        }
        List<String> codeList = Lists.newArrayList();
        String purMrbResult = "";
        Map<String, Object> purMrbModel = Maps.newLinkedHashMap();
        List<Object> purMrbEntry = Lists.newArrayList();
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", "");
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "RKD01_SYS"));
        model.put("FDate",  LocalDateUtils.format(billDate));
        model.put("FBusinessType", "CG");
        model.put("FStockOrgId", CollectionUtil.getMap("FNumber", "100"));
        model.put("FDemandOrgId", CollectionUtil.getMap("FNumber", "100"));
        model.put("FPurchaseOrgId", CollectionUtil.getMap("FNumber", "100"));
        model.put("FPurchaseDeptId", CollectionUtil.getMap("FNumber", department));
        model.put("FStockDeptId", CollectionUtil.getMap("FNumber", department));
        model.put("FSupplierId", CollectionUtil.getMap("FNumber", supplier));
        List<Object> entity = Lists.newArrayList();
        for (List<String> row : dataList) {
            String productCode = StringUtils.toString(row.get(0)).trim();
            String productName=StringUtils.toString(row.get(1)).trim();
            String priceStr = StringUtils.toString(row.get(2)).trim();
            BigDecimal price = StringUtils.isEmpty(priceStr) ? BigDecimal.ZERO : new BigDecimal(priceStr);
            Integer qty = Integer.valueOf(StringUtils.toString(row.get(3)).trim());
            String remarks = StringUtils.toString(row.get(4)).trim();
            String productType = StringUtils.toString(row.get(6)).trim();
            String afterSaleType = StringUtils.toString(row.get(8)).trim();
            BigDecimal amount = new BigDecimal(qty).multiply(price);

            String ggrl = StringUtils.toString(row.get(5)).trim();
            BigDecimal rl = StringUtils.isEmpty(ggrl) ? BigDecimal.ZERO : new BigDecimal(ggrl);
            if (rl.compareTo(BigDecimal.ZERO) == 1) {
                BigDecimal returnPrice = price.multiply(rl).multiply(new BigDecimal(0.01));
                BigDecimal returnAmount = new BigDecimal(qty).multiply(returnPrice);
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                returnAmount = new BigDecimal(decimalFormat.format(returnAmount));
                purMrbEntry.add(getPurMrbDetail(productName,store,  productMap.get(productType), returnAmount,remarks,qty));
            }
            String shrlStr = StringUtils.toString(row.get(7)).trim();
            BigDecimal shrl = StringUtils.isEmpty(shrlStr) ? BigDecimal.ZERO : new BigDecimal(shrlStr);
            if (shrl.compareTo(BigDecimal.ZERO) == 1 && KingdeeNameEnum.INAVIVO.name().equals(kingdeeBookMapper.findNameByCompanyId(RequestUtils.getCompanyId()))) {
                BigDecimal returnPrice = price.multiply(shrl).multiply(new BigDecimal(0.01));
                BigDecimal returnAmount = new BigDecimal(qty).multiply(returnPrice);
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                returnAmount = new BigDecimal(decimalFormat.format(returnAmount));
                purMrbEntry.add(getPurMrbDetail(productName,store,  productMap.get(afterSaleType), returnAmount,remarks,qty));
            }

            Map<String, Object> detail = Maps.newLinkedHashMap();
            detail.put("FMaterialId", CollectionUtil.getMap("FNumber", productCode));
            detail.put("FRealQty", qty);
            detail.put("FPriceUnitQty", qty);
            detail.put("FBaseUnitQty", qty);
            detail.put("FRemainInStockBaseQty", qty);
            detail.put("FPriceBaseQty", qty);
            detail.put("FRemainInStockQty", qty);
            detail.put("FTaxNetPrice", price);
            detail.put("FTaxPrice", price);
            detail.put("FPrice", price);
            detail.put("FAmount", amount);
            detail.put("FStockId", CollectionUtil.getMap("FNumber", store));
            detail.put("FStockStatusId", CollectionUtil.getMap("FNumber", "KCZT01_SYS"));
            // 是否赠品
            detail.put("FGiveAway", price.compareTo(BigDecimal.ZERO) == 0 ? 1 : 0);
            detail.put("FExchangeTypeId", CollectionUtil.getMap("FNumber", "HLTX01_SYS"));
            detail.put("FLocalCurrId", CollectionUtil.getMap("FNumber", "PRE001"));
            detail.put("FNote", remarks);
            entity.add(detail);
        }
        model.put("STK_InStock__FInStockEntry", entity);
        root.put("Model", model);
        String inStockResult =  ObjectMapperUtils.writeValueAsString(root);
//        K3CloudSaveDto k3CloudSaveDto = new K3CloudSaveDto(K3CloudFormIdEnum.STK_InStock.name(), inStockResult);
        String billNo = null;
        codeList.add(billNo);
        if (CollectionUtil.isNotEmpty(purMrbEntry)) {
            Map<String, Object> purMrbroot = Maps.newLinkedHashMap();
            purMrbroot.put("Creator", "");
            purMrbroot.put("NeedUpDateFields", Lists.newArrayList());
            purMrbModel = savePurMrbHeader(billDate, supplier,department);
            purMrbModel.put("PUR_MRB__FPURMRBENTRY", purMrbEntry);
            purMrbroot.put("Model", purMrbModel);
            purMrbResult =  ObjectMapperUtils.writeValueAsString(purMrbroot);

//            k3CloudSaveDto = new K3CloudSaveDto(K3CloudFormIdEnum.PUR_MRB.name(), purMrbResult);
            billNo = null;
            codeList.add(billNo);
        }
        return codeList;
    }

    private Map<String, Object> savePurMrbHeader(LocalDate billDate, String supplier,String department) {
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "TLD01_SYS"));
        model.put("FDate", LocalDateUtils.format(billDate));
        model.put("FBusinessType", "CG");
        model.put("FMRTYPE", "B");
        model.put("FMRMODE", "A");
        model.put("FREPLENISHMODE", "B");
        model.put("FStockOrgId", CollectionUtil.getMap("FNumber",  "100"));
        model.put("FRequireOrgId", CollectionUtil.getMap("FNumber", "100"));
        model.put("FPurchaseOrgId", CollectionUtil.getMap("FNumber", "100"));
        model.put("FMRDeptId", CollectionUtil.getMap("FNumber", department));
        model.put("FPURCHASEDEPTID", CollectionUtil.getMap("FNumber", department));

        model.put("FSupplierID", CollectionUtil.getMap("FNumber", supplier));
        model.put("FDESCRIPTION", "接口");
        return model;
    }

    //采购退料
    public Map<String, Object> getPurMrbDetail(String productName,String store, String productType, BigDecimal returnAmount,String remarks,Integer qty) {
        Map<String, Object> detail = Maps.newLinkedHashMap();
        detail.put("FMaterialId", CollectionUtil.getMap("FNumber", productType));
        detail.put("FRMREALQTY", 1);
        detail.put("FREPLENISHQTY", 1);
        detail.put("FKEAPAMTQTY", 1);
        detail.put("FPRICEUNITQTY", 1);
        detail.put("FStockId", CollectionUtil.getMap("FNumber", store));
        detail.put("FStockStatusId", CollectionUtil.getMap("FNumber", "KCZT01_SYS"));
        detail.put("FNOTE", productName);
        detail.put("FPrice", returnAmount);
        detail.put("FAmount_LC", returnAmount);
        detail.put("FCarryQty", 1);
        // 基本补料数量
        detail.put("FBaseReplayQty", 1);
        // 库存基本数量
        detail.put("FBASEUNITQTY", 1);
        // 扣款数量（基本单位）
        detail.put("FBaseKeapamtQty", 1);
        // 计价基本数量
        detail.put("FPriceBaseQty", 1);
        // 采购基本数量
        detail.put("FCarryBaseQty", 1);
        // 含税单价
        detail.put("FTAXPRICE", returnAmount);
        // 净价
        detail.put("FTAXNETPRICE", returnAmount);
        detail.put("FAmount", returnAmount);
        detail.put("FALLAMOUNT", returnAmount);
        detail.put("FPoRequireOrgId", CollectionUtil.getMap("FNumber", "100"));
        detail.put("FAmount", returnAmount);
        // 是否赠品
        detail.put("FGiveAway", returnAmount.compareTo(BigDecimal.ZERO) == 0 ? 1 : 0);
        detail.put("FExchangeTypeId", CollectionUtil.getMap("FNumber", "HLTX01_SYS"));
        detail.put("FLocalCurrId", CollectionUtil.getMap("FNumber", "PRE001"));
        detail.put("FNOTE", productName+", "+qty+", "+remarks);
        //订单需求组织（电教不用）
        detail.put("FSetPriceUnitID", CollectionUtil.getMap("FNumber", "Pcs"));
        return detail;
    }

}
