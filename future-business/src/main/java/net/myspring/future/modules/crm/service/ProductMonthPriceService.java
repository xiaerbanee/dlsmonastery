package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.crm.domain.ProductMonthPrice;
import net.myspring.future.modules.crm.domain.ProductMonthPriceDetail;
import net.myspring.future.modules.crm.domain.ProductType;
import net.myspring.future.modules.crm.mapper.ProductMonthPriceDetailMapper;
import net.myspring.future.modules.crm.mapper.ProductMonthPriceMapper;
import net.myspring.future.modules.crm.mapper.ProductTypeMapper;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ProductMonthPriceService {

    @Autowired
    private ProductMonthPriceMapper productMonthPriceMapper;
    @Autowired
    private ProductMonthPriceDetailMapper productMonthPriceDetailMapper;
    @Autowired
    private ProductTypeMapper productTypeMapper;

    public ProductMonthPrice findOne(String id) {
        ProductMonthPrice productMonthPrice=productMonthPriceMapper.findOne(id);
        List<ProductType> productTypeList = productTypeMapper.findAllScoreType();
        List<ProductMonthPriceDetail> productMonthPriceDetailList = productMonthPrice.getProductMonthPriceDetailList();
        List<String> productMonthPriceDetailProductTypeIdList = CollectionUtil.extractToList(CollectionUtil.extractToList(productMonthPriceDetailList,"productType"),"id");
        for(ProductType productType:productTypeList){
            if(!productMonthPriceDetailProductTypeIdList.contains(productType.getId())){
                ProductMonthPriceDetail productMonthPriceDetail = new ProductMonthPriceDetail();
                productMonthPriceDetail.setProductType(productType);
                productMonthPriceDetail.setProductTypeId(productType.getId());
                productMonthPriceDetail.setPrice3(productType.getPrice3());
                productMonthPriceDetail.setPrice2(productType.getPrice2());
                productMonthPriceDetail.setBaokaPrice(productType.getBaokaPrice());
                productMonthPriceDetailList.add(productMonthPriceDetail);
            }
        }
        productMonthPrice.setProductMonthPriceDetailList(productMonthPriceDetailList);
        return productMonthPrice;
    }


    public Page<ProductMonthPrice> findPage(Pageable pageable, Map<String, Object> map) {
        Page<ProductMonthPrice> page = productMonthPriceMapper.findPage(pageable, map);
        return page;
    }

    public List<ProductMonthPriceDetail> getProductTypeList(){
        List<ProductType> productTypeList = productTypeMapper.findAllScoreType();
        List<ProductMonthPriceDetail> list = Lists.newArrayList();
        for(ProductType productType:productTypeList){
            ProductMonthPriceDetail productMonthPriceDetail = new ProductMonthPriceDetail();
            productMonthPriceDetail.setProductType(productType);
            productMonthPriceDetail.setProductTypeId(productType.getId());
            productMonthPriceDetail.setPrice3(productType.getPrice3());
            productMonthPriceDetail.setPrice2(productType.getPrice2());
            productMonthPriceDetail.setBaokaPrice(productType.getBaokaPrice());
            list.add(productMonthPriceDetail);
        }
        return list;
    }

    public RestResponse checkMonth(String month){
        return null;
    }

    @Transactional
    public void save(ProductMonthPrice productMonthPrice){
    }

}
