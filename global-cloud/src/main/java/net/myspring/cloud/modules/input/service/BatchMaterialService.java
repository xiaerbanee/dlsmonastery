package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.common.handsontable.HandSonTableUtils;
import net.myspring.cloud.modules.input.dto.BdMaterialDto;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.input.mapper.BdMaterialMapper;
import net.myspring.cloud.modules.input.web.query.BatchMaterialQuery;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 * 物料添加
 * Created by lihx on 2016/10/10.
 */
@Service
@KingdeeDataSource
public class BatchMaterialService {
    @Autowired
    private BdMaterialMapper bdMaterialMapper;
    @Autowired
    private KingdeeManager kingdeeManager;

    public List<String> save(List<List<Object>> datas, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook) {
        List<String> resultList = Lists.newArrayList();
        String userName = accountKingdeeBook.getUsername();
        if(kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),userName,accountKingdeeBook.getPassword())) {
            Map<String, BdMaterialDto> materialMap = Maps.newLinkedHashMap();
            Map<String, String> materialCategoryMap = Maps.newHashMap();
            Map<String, String> materialGroupMap = Maps.newHashMap();
            for (NameNumberDto bdMaterial : bdMaterialMapper.findCategory()) {
                materialCategoryMap.put(bdMaterial.getName(), bdMaterial.getNumber());
            }
            for (NameNumberDto bdMaterial : bdMaterialMapper.findGroup()) {
                materialGroupMap.put(bdMaterial.getName(), bdMaterial.getNumber());
            }
            for (List<Object> row : datas) {
                String productNumber = HandSonTableUtils.getValue(row, 0);
                String productName = HandSonTableUtils.getValue(row, 1);
                String priceStr = HandSonTableUtils.getValue(row, 2);
                BigDecimal price = StringUtils.isEmpty(priceStr) ? BigDecimal.ZERO : new BigDecimal(priceStr);
                String RLPriceStr = HandSonTableUtils.getValue(row, 3);
                BigDecimal rlprice = StringUtils.isEmpty(RLPriceStr) ? BigDecimal.ZERO : new BigDecimal(RLPriceStr);
                String productGroup = HandSonTableUtils.getValue(row, 4);
                String productCategory = HandSonTableUtils.getValue(row, 5);
                String billKey = productNumber + CharConstant.COMMA + productName;
                if (!materialMap.containsKey(billKey)) {
                    BdMaterialDto bdMaterial = new BdMaterialDto();
                    bdMaterial.setfName(productName);
                    bdMaterial.setfNumber(productNumber);
                    bdMaterial.setPrice1(price);
                    bdMaterial.setRlPrice(rlprice);
                    bdMaterial.setfCategoryId(materialCategoryMap.get(productCategory));
                    bdMaterial.setfMaterialGroupName(materialGroupMap.get(productGroup));
                    materialMap.put(billKey, bdMaterial);
                }
            }
            List<BdMaterialDto> materials = Lists.newArrayList(materialMap.values());
            //财务出库开单
            if (CollectionUtil.isNotEmpty(materials)) {
                for (BdMaterialDto bdMaterial : materials) {
                    KingdeeSynDto kingdeeSynDto = new KingdeeSynDto(KingdeeFormIdEnum.BD_MATERIAL.name(), getBdMaterial(bdMaterial,userName),kingdeeBook);
                    kingdeeSynDto = kingdeeManager.save(kingdeeSynDto);
                    if(kingdeeSynDto.getSuccess()){
                        resultList.add("物料添加成功: "+kingdeeSynDto.getBillNo());
                    }else {
                        resultList.add("物料添加失败: "+kingdeeSynDto.getResult());
                        System.err.println("物料添加失败: "+kingdeeSynDto.getResult());
                    }
                }
            }
        }else {
            resultList.add("登入失败");
        }
        return resultList;
    }

    private String getBdMaterial(BdMaterialDto bdMaterial,String userName) {
        Map<String, Object> root = Maps.newLinkedHashMap();
        root.put("Creator", userName);
        root.put("NeedUpDateFields", Lists.newArrayList());
        Map<String, Object> model = Maps.newLinkedHashMap();
        model.put("FName", bdMaterial.getfName());
        model.put("FNumber", bdMaterial.getfNumber());
        model.put("F_PAEZ_Decimal", bdMaterial.getPrice1());
        model.put("F_PAEZ_Decimal1", bdMaterial.getRlPrice());
        Map<String, Object> detail = Maps.newLinkedHashMap();
        detail.put("FCategoryID", CollectionUtil.getMap("FNumber", bdMaterial.getfCategoryId()));
        detail.put("FMaterialGroup", CollectionUtil.getMap("FNumber", bdMaterial.getfMaterialGroupName()));
        detail.put("FTaxRateId", CollectionUtil.getMap("FNumber", "SL04_SYS"));
        detail.put("FBaseUnitId",CollectionUtil.getMap("FNumber","Pcs"));
        detail.put("FErpClsID","外购");
        model.put("BD_MATERIAL__SubHeadEntity", detail);
        root.put("Model", model);
        System.out.println(root);
        return ObjectMapperUtils.writeValueAsString(root);
    }

    public BatchMaterialQuery getFormProperty(BatchMaterialQuery batchMaterialQuery){
        List<NameNumberDto> materialCategories = bdMaterialMapper.findCategory();
        List<NameNumberDto> materialGroups = bdMaterialMapper.findGroup();
        batchMaterialQuery.setMaterialCategoryList(CollectionUtil.extractToList(materialCategories, "name"));
        batchMaterialQuery.setMaterialGroupList(CollectionUtil.extractToList(materialGroups, "name"));
        return batchMaterialQuery;
    }

}
