package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.dto.ProductImeDto;
import net.myspring.future.modules.crm.mapper.ProductImeMapper;
import net.myspring.future.modules.crm.mapper.ProductImeSaleMapper;
import net.myspring.future.modules.crm.mapper.ProductImeUploadMapper;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ProductImeService {
    @Autowired
    private ProductImeMapper productImeMapper;
    @Autowired
    private ProductImeSaleMapper productImeSaleMapper;
    @Autowired
    private ProductImeUploadMapper productImeUploadMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<ProductIme> findPage(Pageable pageable, Map<String,Object> map){
        Page<ProductIme> page=productImeMapper.findPage(pageable,map);
        return page;
    }

    public List<ProductIme> findByDepotAndImeLike(String depotId, String ime) {
        List<ProductIme> productImeList=productImeMapper.findByStoreAndImeLike(depotId,ime);
        return productImeList;
    }

    public List<ProductImeDto> findByImeList(List<String> imeList){
        List<ProductIme> productImeList=productImeMapper.findByImeList(imeList);
        List<ProductImeDto> productImeDtoList= BeanUtil.map(productImeList,ProductImeDto.class);
        cacheUtils.initCacheInput(productImeDtoList);
        return productImeDtoList;
    }

    public Map<String,Integer> findQtyMap(List<String> imeList){
        Map<String,Integer> map= Maps.newHashMap();
        List<ProductIme> productImeList=productImeMapper.findByImeList(imeList);
        if(CollectionUtil.isNotEmpty(productImeList)){
            List<Product> productList=productMapper.findByIds(CollectionUtil.extractToList(productImeList,"productId"));
            Map<String,Product> productMap=CollectionUtil.extractToMap(productList,"id");
            Map<String,List<ProductIme>> productImeMap=CollectionUtil.extractToMapList(productImeList,"productId");
            for(Map.Entry<String,List<ProductIme>> entry:productImeMap.entrySet()){
                map.put(productMap.get(entry.getKey()).getName(),entry.getValue().size());
            }
        }
        return map;
    }

    public List<ProductIme> findByImeLike(String ime){
        List<ProductIme> productImeList=productImeMapper.findByImeLike(ime);
        return productImeList;
    }

    public ProductIme findOne(String id){
        ProductIme productIme = productImeMapper.findOne(id);
        return productIme;
    }

    public List<ProductIme> findByStoreAndBoxAndIme(String storeId, List<String> boxImeList, List<String> imeList){
        List<ProductIme> productImeList = productImeMapper.findByStoreAndBoxImeAndIme(storeId,boxImeList,imeList);
        return productImeList;
    }

    public List<SimpleExcelSheet> findSimpleExcelSheets(Workbook workbook, Map<String, Object> map) {
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "boxIme", "箱号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "ime", "串码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "ime2", "串码2"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "meid", "MEID"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "product.name", "货品型号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "product.productType.name", "统计型号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "inputType", "入库类型"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "billId", "工厂订单编号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdTime", "工厂发货时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "创建时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedDate", "办事处"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedDate", "考核区域"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "depot.areaType", "区域属性"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "depot.name", "所在仓库"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "retailDate", "保卡注册日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productImeSale.createdDate", "串码核销日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productImeSale.created.fullName", "核销人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productImeUpload.createdDate", "保卡上报日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productImeUpload.created.fullName", "保卡上报人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "lastModified.fullName", "更新人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedDate", "更新时间"));

        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<ProductIme> productImeList = productImeMapper.findByFilter(map);
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("串码列表"+ LocalDate.now(), productImeList, simpleExcelColumnList);
        simpleExcelSheetList.add(simpleExcelSheet);
        return simpleExcelSheetList;
    }

}
