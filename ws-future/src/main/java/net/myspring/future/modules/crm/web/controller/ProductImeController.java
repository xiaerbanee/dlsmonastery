package net.myspring.future.modules.crm.web.controller;


import com.sun.org.apache.xpath.internal.operations.Bool;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.common.enums.BoolEnum;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.*;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.basic.service.ProductService;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.dto.ProductImeDto;
import net.myspring.future.modules.crm.dto.ProductImeHistoryDto;
import net.myspring.future.modules.crm.dto.ProductImeReportDto;
import net.myspring.future.modules.crm.service.ProductImeService;
import net.myspring.future.modules.crm.web.form.ProductImeBatchChangeForm;
import net.myspring.future.modules.crm.web.form.ProductImeBatchCreateForm;
import net.myspring.future.modules.crm.web.form.ProductImeCreateForm;
import net.myspring.future.modules.crm.web.query.ProductImeQuery;
import net.myspring.future.modules.crm.web.query.ProductImeReportQuery;
import net.myspring.future.modules.crm.web.query.ProductImeStockReportQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "crm/productIme")
public class ProductImeController {

    @Autowired
    private ProductImeService productImeService;
    @Autowired
    private ProductService productService;
    @Autowired
    private DepotService depotService;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public Page list(Pageable pageable,ProductImeQuery productImeQuery){
        Page<ProductImeDto> page = productImeService.findPage(pageable,productImeQuery);
        return page;
    }

    @RequestMapping(value="getQuery")
    public ProductImeQuery getQuery(ProductImeQuery productImeQuery) {
        productImeQuery.setInputTypeList(InputTypeEnum.getList());
        return productImeQuery;
    }


    @RequestMapping(value="getProductImeDetail")
    public ProductImeDto getProductImeDetail(String id) {
        return productImeService.getProductImeDetail(id);
    }

    @RequestMapping(value="getProductImeHistoryList")
    public List<ProductImeHistoryDto> getProductImeHistoryList(String id) {
        return productImeService.getProductImeHistoryList(id);
    }

    @RequestMapping(value="findDtoListByImes")
    public List<ProductImeDto> findDtoListByImes(String imeStr) {
        return productImeService.findDtoListByImes(imeStr);
    }

    @RequestMapping(value="export")
    public String export(ProductImeQuery productImeQuery) {

        return productImeService.export(productImeQuery);
    }

    @RequestMapping(value = "search")
    public List<ProductImeDto> search(String productIme,String shopId){
        String imeReverse = StringUtils.reverse(productIme);
        List<ProductIme> productImeList =productImeService.findByImeLike(imeReverse,shopId);
        List<ProductImeDto> productImeDtoList = BeanUtil.map(productImeList,ProductImeDto.class);
        return productImeDtoList;
    }

    @RequestMapping(value = "getStockReportQuery")
    public ProductImeStockReportQuery getStockReportQuery(ProductImeStockReportQuery productImeStockReportQuery){

        productImeStockReportQuery.setSumTypeList(ProductImeStockReportSumTypeEnum.getList());
        productImeStockReportQuery.setOutTypeList(ProductImeStockReportOutTypeEnum.getList());
        productImeStockReportQuery.setTownTypeList(TownTypeEnum.getList());

        //TODO 需要获取地区类型列表
        productImeStockReportQuery.setAreaTypeList(new ArrayList<>());

        productImeStockReportQuery.setSumType(ProductImeStockReportSumTypeEnum.区域.name());
        CompanyConfigCacheDto  companyConfigCacheDto = CompanyConfigUtil.findByCode( redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.PRODUCT_NAME.name());
        if(companyConfigCacheDto != null && "WZOPPO".equals(companyConfigCacheDto.getValue())) {
            productImeStockReportQuery.setOutType(ProductImeStockReportOutTypeEnum.核销.name());
         }else{
            productImeStockReportQuery.setOutType(ProductImeStockReportOutTypeEnum.电子保卡.name());

         }
        productImeStockReportQuery.setScoreType(true);

        return productImeStockReportQuery;
    }

    @RequestMapping(value = "productImeReport")
    public List<ProductImeReportDto> productImeReport(ProductImeReportQuery productImeSaleReportQuery){
        return productImeService.productImeReport(productImeSaleReportQuery);
    }

    @RequestMapping(value = "getReportQuery")
    public ProductImeReportQuery getReportQuery(ProductImeReportQuery productImeSaleReportQuery){
        productImeSaleReportQuery.setSumTypeList(SumTypeEnum.getList());
        productImeSaleReportQuery.setAreaTypeList(AreaTypeEnum.getList());
        productImeSaleReportQuery.setTownTypeList(TownTypeEnum.getList());
        productImeSaleReportQuery.setOutTypeList(OutTypeEnum.getList());
        productImeSaleReportQuery.setBoolMap(BoolEnum.getMap());
        productImeSaleReportQuery.setSumType(ProductImeStockReportSumTypeEnum.区域.name());
        CompanyConfigCacheDto  companyConfigCacheDto = CompanyConfigUtil.findByCode( redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.PRODUCT_NAME.name());
        if(companyConfigCacheDto != null && "WZOPPO".equals(companyConfigCacheDto.getValue())) {
            productImeSaleReportQuery.setOutType(ProductImeStockReportOutTypeEnum.核销.name());
        }else{
            productImeSaleReportQuery.setOutType(ProductImeStockReportOutTypeEnum.电子保卡.name());

        }
        return productImeSaleReportQuery;
    }

    @RequestMapping(value = "getBatchCreateForm")
    public ProductImeBatchCreateForm getBatchCreateForm(ProductImeBatchCreateForm productImeBatchCreateForm){

        productImeBatchCreateForm.getExtra().put("productNames" , productService.findNameList(RequestUtils.getCompanyId()));
        productImeBatchCreateForm.getExtra().put("storeNames" , CollectionUtil.extractToList(depotService.findStoreList(new DepotQuery()),"name"));
        return productImeBatchCreateForm;

    }

    @RequestMapping(value = "batchCreate")
    public RestResponse batchCreate(ProductImeBatchCreateForm productImeBatchCreateForm){

        productImeService.batchCreate(productImeBatchCreateForm);

        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getBatchChangeForm")
    public ProductImeBatchChangeForm getBatchChangeForm(ProductImeBatchChangeForm  productImeBatchChangeForm){

        productImeBatchChangeForm.getExtra().put("productNames" , productService.findNameList(RequestUtils.getCompanyId()));
        return productImeBatchChangeForm;

    }

    @RequestMapping(value = "batchChange")
    public RestResponse batchChange(ProductImeBatchChangeForm  productImeBatchChangeForm){

        productImeService.batchChange(productImeBatchChangeForm);

        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());


    }

}
