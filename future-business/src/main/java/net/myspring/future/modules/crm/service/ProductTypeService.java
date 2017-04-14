package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.modules.crm.domain.ProductType;
import net.myspring.future.modules.crm.mapper.ProductMapper;
import net.myspring.future.modules.crm.mapper.ProductTypeMapper;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductTypeService {

    @Autowired
    private ProductTypeMapper productTypeMapper;
    @Autowired
    private ProductMapper productMapper;


    public ProductType findOne(String id) {
        ProductType productType = productTypeMapper.findOne(id);
        return productType;
    }

    public List<ProductType> findAllScoreType(){
        List<ProductType> productTypeList = productTypeMapper.findAllScoreType();
        return productTypeList;
    }

    public ProductType save(ProductType productType) {
        return productType;
    }

    public void logicDeleteOne(String id) {
        productTypeMapper.logicDeleteOne(id);
    }

    public Page<ProductType> findPage(Pageable pageable, Map<String, Object> map) {
        Page<ProductType> page = productTypeMapper.findPage(pageable, map);
        return page;
    }

    public List<ProductType> findAll(){
        List<ProductType> productTypeList=productTypeMapper.findAll();
        return productTypeList;
    }

    public List<SimpleExcelSheet> findSimpleExcelSheets(Workbook workbook, Map<String, Object> map) {
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "name", "产品型号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "code", "型号编码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "created.fullName", "创建人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "scoreType", "是否打分"));
        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<ProductType> productTypeList = productTypeMapper.findByFilter(map);
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("货品类型", productTypeList, simpleExcelColumnList);
        simpleExcelSheetList.add(simpleExcelSheet);
        return simpleExcelSheetList;
    }

    public List<ProductType> findByNameLike(String name){
        List<ProductType> productTypeList = productTypeMapper.findByNameLike(name);
        return productTypeList;
    }
}
