package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.CharEnum;
import net.myspring.cloud.common.enums.K3CloudFormIdEnum;
import net.myspring.cloud.common.handsontable.HandSonTableUtils;
import net.myspring.cloud.common.utils.SecurityUtils;
import net.myspring.cloud.modules.input.domain.BdMaterial;
import net.myspring.cloud.modules.input.dto.BdMaterialDto;
import net.myspring.cloud.modules.input.dto.K3CloudSave;
import net.myspring.cloud.modules.input.mapper.BdMaterialMapper;
import net.myspring.cloud.modules.input.utils.K3cloudUtils;
import net.myspring.cloud.modules.input.web.form.BatchMaterialForm;
import net.myspring.cloud.modules.report.dto.NameValueDto;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@KingdeeDataSource
public class BatchMaterialService {
    @Autowired
    private BdMaterialMapper bdMaterialMapper;

    public List<String> save(List<List<Object>> datas) {
        Map<String, BdMaterialDto> materialMap = Maps.newLinkedHashMap();
        Map<String, String> materialCategoryMap = Maps.newHashMap();
        Map<String, String> materialGroupMap = Maps.newHashMap();
        for (NameValueDto bdMaterial : bdMaterialMapper.findCategory()) {
            materialCategoryMap.put(bdMaterial.getName(), bdMaterial.getValue());
        }
        for (NameValueDto bdMaterial : bdMaterialMapper.findGroup()) {
            materialGroupMap.put(bdMaterial.getName(), bdMaterial.getValue());
        }
        for (List<Object> row : datas) {
            String productNumber = HandSonTableUtils.getValue(row, 0);
            String productName = HandSonTableUtils.getValue(row, 1);
            String priceStr = HandSonTableUtils.getValue(row, 2);
            BigDecimal price = StringUtils.isEmpty(priceStr) ? BigDecimal.ZERO : new BigDecimal(priceStr);
            String RLPriceStr = HandSonTableUtils.getValue(row,3);
            BigDecimal rlprice = StringUtils.isEmpty(RLPriceStr) ? BigDecimal.ZERO : new BigDecimal(RLPriceStr);
            String productGroup = HandSonTableUtils.getValue(row, 4);
            String productCategory = HandSonTableUtils.getValue(row, 5);
            String billKey = productNumber + CharEnum.COMMA + productName;
            if (!materialMap.containsKey(billKey)) {
                BdMaterialDto bdMaterial = new BdMaterialDto();
                bdMaterial.setfName(productName);
                bdMaterial.setfNumber(productNumber);
                bdMaterial.setPrice1(price);
                bdMaterial.setRlPrice(rlprice);
                bdMaterial.setfCateGoryId(materialCategoryMap.get(productCategory));
                bdMaterial.setfMaterialGroupName(materialGroupMap.get(productGroup));
                materialMap.put(billKey, bdMaterial);
            }
        }
        List<BdMaterialDto> materials = Lists.newArrayList(materialMap.values());
        List<String> codeList = Lists.newArrayList();
        //财务出库开单
        if (CollectionUtil.isNotEmpty(materials)) {
            for (BdMaterialDto bdMaterial : materials) {
                K3CloudSave k3CloudSave = new K3CloudSave(K3CloudFormIdEnum.BD_MATERIAL.name(), getBdMaterial(bdMaterial));
                String billNo = K3cloudUtils.save(k3CloudSave).getBillNo();
                codeList.add(billNo);
            }
        }
        return codeList;
    }

    private String getBdMaterial(BdMaterialDto bdMaterial) {
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", SecurityUtils.getAccountId());
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FName", bdMaterial.getfName());
        model.put("FNumber", bdMaterial.getfNumber());
        model.put("F_PAEZ_Decimal", bdMaterial.getPrice1());
        model.put("F_PAEZ_Decimal1", bdMaterial.getRlPrice());
        Map<String, Object> detail = Maps.newLinkedHashMap();
        detail.put("FCategoryID", K3cloudUtils.getMap("FNumber", bdMaterial.getfCateGoryId()));
        detail.put("FMaterialGroup", K3cloudUtils.getMap("FNumber", bdMaterial.getfMaterialGroupName()));
        detail.put("FTaxRateId", K3cloudUtils.getMap("FNumber", "SL04_SYS"));
        model.put("BD_MATERIAL__SubHeadEntity", detail);
        root.put("Model", model);
        return ObjectMapperUtils.writeValueAsString(root);
    }

    public BatchMaterialForm getFormProperty(BatchMaterialForm batchMaterialForm){
        List<NameValueDto> materialCategories = bdMaterialMapper.findCategory();
        List<NameValueDto> materialGroups = bdMaterialMapper.findGroup();
        batchMaterialForm.setMaterialCategoryList(CollectionUtil.extractToList(materialCategories, "name"));
        batchMaterialForm.setMaterialGroupList(CollectionUtil.extractToList(materialGroups, "name"));
        return batchMaterialForm;
    }

}
