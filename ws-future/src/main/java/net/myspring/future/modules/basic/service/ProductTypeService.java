package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.dto.ProductDto;
import net.myspring.future.modules.basic.dto.ProductTypeDto;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.basic.mapper.ProductTypeMapper;
import net.myspring.future.modules.basic.web.form.ProductForm;
import net.myspring.future.modules.basic.web.query.ProductTypeQuery;
import net.myspring.future.modules.basic.web.form.ProductTypeForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductTypeService {

    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CacheUtils cacheUtils;


    public ProductType findOne(String id) {
        ProductType productType = productTypeMapper.findOne(id);
        return productType;
    }

    public ProductTypeForm findForm(ProductTypeForm productTypeForm){
        if(!productTypeForm.isCreate()){
            ProductType productType = productTypeMapper.findOne(productTypeForm.getId());
            productTypeForm = BeanUtil.map(productType,productTypeForm.getClass());
            List<Product> productList = productMapper.findByProductTypeId(productTypeForm.getId());
            productTypeForm.setProductList(BeanUtil.map(productList,ProductDto.class));
        }
        return productTypeForm;
    }

    public List<ProductType> findAllScoreType(){
        List<ProductType> productTypeList = productTypeMapper.findAllScoreType();
        return productTypeList;
    }

    public ProductType save(ProductTypeForm productTypeForm) {
        ProductType productType;
        if (productTypeForm.isCreate()) {
            productType= BeanUtil.map(productTypeForm,ProductType.class);
            productTypeMapper.save(productType);
        } else {
            productMapper.updateProductTypeToNull(productTypeForm.getId());
            productType= productTypeMapper.findOne(productTypeForm.getId());
            ReflectionUtil.copyProperties(productTypeForm,productType);
            productTypeMapper.update(productType);
        }
        if (CollectionUtil.isNotEmpty(productTypeForm.getProductIdList())) {
            productMapper.updateProductTypeId(productType.getId(), productTypeForm.getProductIdList());
        }
        return productType;
    }

    public void logicDeleteOne(ProductTypeForm productTypeForm) {
        productTypeMapper.logicDeleteOne(productTypeForm.getId());
    }

    public Page<ProductTypeDto> findPage(Pageable pageable, ProductTypeQuery productTypeQuery) {
        Page<ProductTypeDto> page = productTypeMapper.findPage(pageable, productTypeQuery);
        cacheUtils.initCacheInput(page.getContent());
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
