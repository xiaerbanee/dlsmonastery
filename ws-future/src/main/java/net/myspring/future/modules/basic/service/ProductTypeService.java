package net.myspring.future.modules.basic.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.dto.ProductDto;
import net.myspring.future.modules.basic.dto.ProductTypeDto;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.basic.repository.ProductTypeRepository;
import net.myspring.future.modules.basic.web.form.ProductTypeForm;
import net.myspring.future.modules.basic.web.query.ProductTypeQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductTypeService {

    @Autowired
    private ProductTypeRepository productTypeRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CacheUtils cacheUtils;


    public ProductType findOne(String id) {
        ProductType productType = productTypeRepository.findOne(id);
        return productType;
    }

    public ProductTypeForm getForm(ProductTypeForm productTypeForm){
        if(!productTypeForm.isCreate()){
            ProductType productType = productTypeRepository.findOne(productTypeForm.getId());
            productTypeForm = BeanUtil.map(productType,productTypeForm.getClass());
            List<Product> productList = productRepository.findByProductTypeId(productTypeForm.getId());
            productTypeForm.setProductList(BeanUtil.map(productList,ProductDto.class));
        }
        return productTypeForm;
    }

    public List<ProductType> findAllScoreType(){
        List<ProductType> productTypeList = productTypeRepository.findAllScoreType();
        return productTypeList;
    }

    public ProductType save(ProductTypeForm productTypeForm) {
        ProductType productType;
        if (productTypeForm.isCreate()) {
            productType= BeanUtil.map(productTypeForm,ProductType.class);
            productTypeRepository.save(productType);
        } else {

            //TODO 不这样写update
//            productRepository.updateProductTypeToNull(productTypeForm.getId());
            productType= productTypeRepository.findOne(productTypeForm.getId());
            ReflectionUtil.copyProperties(productTypeForm,productType);
            productTypeRepository.save(productType);
        }
        if (CollectionUtil.isNotEmpty(productTypeForm.getProductIdList())) {
            //TODO 需要重写，不能这样写update
//            productRepository.updateProductTypeId(productType.getId(), productTypeForm.getProductIdList());
        }
        return productType;
    }

    public void logicDeleteOne(ProductTypeForm productTypeForm) {
        productTypeRepository.logicDeleteOne(productTypeForm.getId());
    }

    public Page<ProductTypeDto> findPage(Pageable pageable, ProductTypeQuery productTypeQuery) {
        Page<ProductTypeDto> page = productTypeRepository.findPage(pageable, productTypeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<ProductType> findAll(){
        List<ProductType> productTypeList=productTypeRepository.findAll();
        return productTypeList;
    }

//    public List<SimpleExcelSheet> findSimpleExcelSheets(Workbook workbook, Map<String, Object> map) {
//        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "name", "产品型号"));
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "code", "型号编码"));
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "created.fullName", "创建人"));
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "scoreType", "是否打分"));
//        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
//        List<ProductType> productTypeList = productTypeRepository.findList(map);
//        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("货品类型", productTypeList, simpleExcelColumnList);
//        simpleExcelSheetList.add(simpleExcelSheet);
//        return simpleExcelSheetList;
//    }

    public List<ProductType> findByNameLike(String name){
        List<ProductType> productTypeList = productTypeRepository.findByNameLike(name);
        return productTypeList;
    }

}
