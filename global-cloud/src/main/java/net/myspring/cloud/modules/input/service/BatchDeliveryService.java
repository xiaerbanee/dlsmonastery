package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.common.enums.MisDeliveryTypeEnum;
import net.myspring.cloud.common.handsontable.HandSonTableUtils;
import net.myspring.cloud.common.utils.CacheUtils;
import net.myspring.cloud.modules.input.dto.BatchDeliveryDto;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import net.myspring.cloud.modules.input.mapper.BdMaterialMapper;
import net.myspring.cloud.modules.input.mapper.BdStockMapper;
import net.myspring.cloud.modules.input.web.query.BatchDeliveryQuery;
import net.myspring.cloud.modules.sys.dto.AccountDto;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 其他出库
 * Created by liuj on 2016-06-27.
 */
@Service
@KingdeeDataSource
public class BatchDeliveryService {
    @Autowired
    private BdStockMapper bdStockMapper;
    @Autowired
    private BdMaterialMapper bdMaterialMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public List<String> save(List<List<Object>> datas, String department, LocalDate billDate) {
        Map<String, BatchDeliveryDto> misDeliveryMap = Maps.newLinkedHashMap();
        Map<String, String> depotMap = Maps.newHashMap();
        Map<String, String> productMap = Maps.newHashMap();
        for (NameNumberDto bdstock : bdStockMapper.findNameAndNumber()) {
            depotMap.put(bdstock.getName(), bdstock.getNumber());
        }
        for (NameNumberDto bdMaterial : bdMaterialMapper.findNameAndNumber()) {
            productMap.put(bdMaterial.getName(), bdMaterial.getNumber());
        }
        for (List<Object> row : datas) {
            String productName = StringUtils.toString(row.get(1));
            String depotName = HandSonTableUtils.getValue(row, 2);
            Integer qty = Integer.valueOf(HandSonTableUtils.getValue(row, 3));
            String type = HandSonTableUtils.getValue(row, 4);
            String remarks = HandSonTableUtils.getValue(row, 5);

            String billKey = productName + CharConstant.COMMA + depotName + CharConstant.COMMA + qty + CharConstant.COMMA + remarks + CharConstant.COMMA + type;
            if (!misDeliveryMap.containsKey(billKey)) {
                BatchDeliveryDto misDelivery = new BatchDeliveryDto();
                misDelivery.setBillDate(billDate);
                misDelivery.setDepartment(department);
                misDelivery.setProduct(productMap.get(productName));
                misDelivery.setDepot(depotMap.get(depotName));
                misDelivery.setQty(qty);
                misDelivery.setRemark(remarks);
                misDelivery.setMisDeliveryType(type);
                misDeliveryMap.put(billKey, misDelivery);
            } else {
                misDeliveryMap.get(billKey).setQty(qty + qty);
            }
        }
        AccountDto accountDto = new AccountDto();
        cacheUtils.initCacheInput(accountDto);
        List<BatchDeliveryDto> billList = Lists.newArrayList(misDeliveryMap.values());
        List<String> billNos = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(billList)) {
            for (BatchDeliveryDto misDelivery : billList) {
                KingdeeSynDto kingdeeSynDto = new KingdeeSynDto(KingdeeFormIdEnum.STK_MisDelivery.name(), getMisDeliveryReturn(misDelivery,accountDto));
                String billNo = null;
                billNos.add(billNo);
            }
        }
        return billNos;
    }

    private String getMisDeliveryReturn(BatchDeliveryDto misDelivery,AccountDto accountDto) {
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", accountDto.getName());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FID", 0);
        model.put("FDate", misDelivery.getBillDate());
        model.put("FBillTypeID", CollectionUtil.getMap("FNumber", "QTCKD01_SYS"));
        model.put("FStockOrgId", CollectionUtil.getMap("FNumber", "100"));
        model.put("FPickOrgId", CollectionUtil.getMap("FNumber", "100"));
        model.put("FOwnerIdHead", CollectionUtil.getMap("FNumber", "100"));

        model.put("FDeptId", CollectionUtil.getMap("FNumber", misDelivery.getDepartment()));
        //库存方向
        if (MisDeliveryTypeEnum.出库.name().equals(misDelivery.getMisDeliveryType())) {
            model.put("FStockDirect", "GENERAL");
        } else {
            model.put("FStockDirect", "RETURN");
        }
        model.put("FOwnerTypeIdHead", "BD_OwnerOrg");
        model.put("FBaseCurrId", CollectionUtil.getMap("FNumber", "PRE001"));
        List<Object> entity = Lists.newArrayList();
        Map<String, Object> detail = Maps.newLinkedHashMap();
        detail.put("FMaterialId", CollectionUtil.getMap("FNumber", misDelivery.getProduct()));
        detail.put("FStockId", CollectionUtil.getMap("FNumber", misDelivery.getDepot()));
        detail.put("FQty", misDelivery.getQty());
        detail.put("FEntryNote", misDelivery.getRemark());
        entity.add(detail);
        model.put("STK_MisDelivery__FEntity", entity);
        root.put("Model", model);
        String result = ObjectMapperUtils.writeValueAsString(root);
        return result;
    }

    public BatchDeliveryQuery getFormProperty(BatchDeliveryQuery batchDeliveryQuery){
        batchDeliveryQuery.setTypes(MisDeliveryTypeEnum.values());
        return batchDeliveryQuery;
    }
}
