package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.K3CloudFormIdEnum;
import net.myspring.cloud.common.utils.CacheUtils;
import net.myspring.cloud.modules.input.dto.K3CloudSave;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import net.myspring.cloud.modules.input.mapper.BdMaterialMapper;
import net.myspring.cloud.modules.input.utils.K3cloudUtils;
import net.myspring.cloud.modules.remote.dto.AccountDto;
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
 * 采购退料
 * Created by lihx on 2016/11/2.
 */
@Service
@KingdeeDataSource
public class BatchPurchaseReturnService {
    @Autowired
    private BdMaterialMapper bdMaterialMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public List<String> save(LocalDate billDate, List<List<String>> dataList, String store, String supplier, String department) {
        Map<String, String> productMap = Maps.newHashMap();
        for (NameNumberDto material : bdMaterialMapper.findNameAndNumber()) {
            productMap.put(material.getName(), material.getNumber());
        }
        AccountDto accountDto = new AccountDto();
        cacheUtils.initCacheInput(accountDto);
        List<String> codeList = Lists.newArrayList();
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", accountDto.getName());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FBillTypeID", K3cloudUtils.getMap("FNumber", "TLD01_SYS"));
        model.put("FDate", LocalDateUtils.format(billDate));
        model.put("FBusinessType", "CG");
        model.put("FMRTYPE", "B");
        model.put("FMRMODE", "A");
        model.put("FREPLENISHMODE", "B");
        model.put("FStockOrgId", K3cloudUtils.getMap("FNumber", "100"));
        model.put("FRequireOrgId", K3cloudUtils.getMap("FNumber", "100"));
        model.put("FPurchaseOrgId", K3cloudUtils.getMap("FNumber", "100"));
        model.put("FMRDeptId", K3cloudUtils.getMap("FNumber", department));
        model.put("FPURCHASEDEPTID", K3cloudUtils.getMap("FNumber", department));

        model.put("FSupplierID", K3cloudUtils.getMap("FNumber", supplier));
        model.put("FDESCRIPTION", "接口");
        List<Object> entity = Lists.newArrayList();
        for (List<String> row : dataList) {
            String productName = StringUtils.toString(row.get(0));
            String priceStr = StringUtils.toString(row.get(1)).trim();
            BigDecimal price = StringUtils.isEmpty(priceStr) ? BigDecimal.ZERO : new BigDecimal(priceStr);
            Integer qty = Integer.valueOf(StringUtils.toString(row.get(2)).trim());
            String remarks = StringUtils.toString(row.get(3)).trim();
            Map<String, Object> detail = Maps.newLinkedHashMap();
            detail.put("FMaterialId", K3cloudUtils.getMap("FNumber", productMap.get(productName)));
            detail.put("FRMREALQTY", qty);
            detail.put("FREPLENISHQTY", qty);
            detail.put("FKEAPAMTQTY", qty);
            detail.put("FPRICEUNITQTY", qty);
            detail.put("FStockId", K3cloudUtils.getMap("FNumber", store));
            detail.put("FStockStatusId", K3cloudUtils.getMap("FNumber", "KCZT01_SYS"));
            detail.put("FNOTE", remarks);
            detail.put("FPrice", price);
            detail.put("FAmount_LC", new BigDecimal(qty).multiply(price));
            detail.put("FCarryQty", qty);
            // 基本补料数量
            detail.put("FBaseReplayQty", qty);
            // 库存基本数量
            detail.put("FBASEUNITQTY", qty);
            // 扣款数量（基本单位）
            detail.put("FBaseKeapamtQty", qty);
            // 计价基本数量
            detail.put("FPriceBaseQty", qty);
            // 采购基本数量
            detail.put("FCarryBaseQty", qty);
            // 含税单价
            detail.put("FTAXPRICE", price);
            // 净价
            detail.put("FTAXNETPRICE", price);
            detail.put("FAmount", new BigDecimal(qty).multiply(price));
            detail.put("FALLAMOUNT", new BigDecimal(qty).multiply(price));
            detail.put("FAmount", new BigDecimal(qty).multiply(price));
            // 是否赠品
            detail.put("FGiveAway", price.compareTo(BigDecimal.ZERO) == 0 ? 1 : 0);
            detail.put("FExchangeTypeId", K3cloudUtils.getMap("FNumber", "HLTX01_SYS"));
            detail.put("FLocalCurrId", K3cloudUtils.getMap("FNumber", "PRE001"));
            detail.put("FNOTE", remarks);
            detail.put("FPoRequireOrgId", K3cloudUtils.getMap("FNumber", "100"));
            detail.put("FSetPriceUnitID", K3cloudUtils.getMap("FNumber", "Pcs"));
            entity.add(detail);
        }
        model.put("PUR_MRB__FPURMRBENTRY", entity);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        K3CloudSave k3CloudSave = new K3CloudSave(K3CloudFormIdEnum.PUR_MRB.name(), result);
        String billNo = K3cloudUtils.save(k3CloudSave,accountDto).getBillNo();
        codeList.add(billNo);
        return codeList;
    }
}
