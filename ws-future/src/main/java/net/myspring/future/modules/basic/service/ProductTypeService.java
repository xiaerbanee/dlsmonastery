package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.dto.ProductTypeDto;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.basic.repository.ProductTypeRepository;
import net.myspring.future.modules.basic.web.form.ProductTypeForm;
import net.myspring.future.modules.basic.web.query.ProductTypeQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ProductTypeService {

    @Autowired
    private ProductTypeRepository productTypeRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private GridFsTemplate tempGridFsTemplate;

    public ProductType save(ProductTypeForm productTypeForm) {

        ProductType productType;
        if (productTypeForm.isCreate()) {
            ProductType sameNameRecord = productTypeRepository.findByNameAndCompanyId(productTypeForm.getName(), RequestUtils.getCompanyId());
            if(sameNameRecord != null){
                throw new ServiceException("名称已经存在，不能新增");
            }
            productType = new ProductType();
            ReflectionUtil.copyProperties(productTypeForm, productType);
            productTypeRepository.save(productType);

        } else {
            ProductType sameNameRecord = productTypeRepository.findByNameAndCompanyId(productTypeForm.getName(), RequestUtils.getCompanyId());
            if(sameNameRecord != null && !sameNameRecord.getId().equals(productTypeForm.getId())){
                throw new ServiceException("名称已经存在，不能修改");
            }

            productType= productTypeRepository.findOne(productTypeForm.getId());
            ReflectionUtil.copyProperties(productTypeForm, productType);
            productTypeRepository.save(productType);
        }

        //开始更新product表
        if(!productTypeForm.isCreate()){
            List<Product> oldProductList = productRepository.findByEnabledIsTrueAndProductTypeId(productTypeForm.getId());
            for(Product product : oldProductList){
                product.setProductTypeId(null);
            }
            productRepository.save(oldProductList);
        }

        if (CollectionUtil.isNotEmpty(productTypeForm.getProductIdList())) {
            List<Product> newProductList = productRepository.findAll(productTypeForm.getProductIdList());
            for(Product product : newProductList){
                product.setProductTypeId(productType.getId());
            }
            productRepository.save(newProductList);
        }

        return productType;
    }

    public void logicDelete(String id) {
        productTypeRepository.logicDelete(id);
    }

    public Page<ProductTypeDto> findPage(Pageable pageable, ProductTypeQuery productTypeQuery) {
        Page<ProductTypeDto> page = productTypeRepository.findPage(pageable, productTypeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<ProductTypeDto> findByNameLike(String name){
        List<ProductTypeDto> productTypeDtos = Lists.newArrayList();
        if(StringUtils.isNotBlank(name)){
           productTypeDtos = BeanUtil.map(productTypeRepository.findByNameLike(name),ProductTypeDto.class);
        }

        return productTypeDtos;
    }

    public List<ProductTypeDto> findByIds(List<String> ids){
        if(CollectionUtil.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        List<ProductType> productTypes = productTypeRepository.findAll(ids);
        List<ProductTypeDto> productTypeDtos = BeanUtil.map(productTypes,ProductTypeDto.class);
        return productTypeDtos;
    }

    public ProductTypeDto findDto(String id) {
        ProductTypeDto result = productTypeRepository.findDto(id);
        cacheUtils.initCacheInput(result);
        return result;
    }

    public String export(ProductTypeQuery productTypeQuery) {


        Workbook workbook = new SXSSFWorkbook(10000);

        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<SimpleExcelColumn> productTypeColumnList = Lists.newArrayList();
        productTypeColumnList.add(new SimpleExcelColumn(workbook, "name", "产品型号"));
        productTypeColumnList.add(new SimpleExcelColumn(workbook, "code", "型号编码"));
        productTypeColumnList.add(new SimpleExcelColumn(workbook, "createdByName", "创建人"));
        productTypeColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));
        productTypeColumnList.add(new SimpleExcelColumn(workbook, "scoreTypeName", "是否打分"));

        List<ProductTypeDto> productTypeDtoList = findPage(new PageRequest(0,10000), productTypeQuery).getContent();
        simpleExcelSheetList.add(new SimpleExcelSheet("统计型号列表", productTypeDtoList, productTypeColumnList));

        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"统计型号列表"+ LocalDate.now()+".xlsx", simpleExcelSheetList);
        ByteArrayInputStream byteArrayInputStream= ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        GridFSFile gridFSFile = tempGridFsTemplate.store(byteArrayInputStream,simpleExcelBook.getName(),"application/octet-stream; charset=utf-8", RequestUtils.getDbObject());
        return StringUtils.toString(gridFSFile.getId());

    }
}
