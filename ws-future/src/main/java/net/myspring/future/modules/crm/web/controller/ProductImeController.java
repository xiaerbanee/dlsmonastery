package net.myspring.future.modules.crm.web.controller;


import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.enums.InputTypeEnum;
import net.myspring.future.common.enums.ProductImeStockReportOutTypeEnum;
import net.myspring.future.common.enums.ProductImeStockReportSumTypeEnum;
import net.myspring.future.common.enums.TownTypeEnum;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.dto.ProductImeDto;
import net.myspring.future.modules.crm.dto.ProductImeHistoryDto;
import net.myspring.future.modules.crm.dto.ProductImeReportDto;
import net.myspring.future.modules.crm.service.ProductImeService;
import net.myspring.future.modules.crm.web.query.ProductImeQuery;
import net.myspring.future.modules.crm.web.query.ProductImeReportQuery;
import net.myspring.future.modules.crm.web.query.ProductImeStockReportQuery;
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

}
