package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.mapper.ProductTypeMapper;
import net.myspring.future.modules.basic.repository.ProductMonthPriceRepository;
import net.myspring.future.modules.crm.domain.ProductMonthPrice;
import net.myspring.future.modules.crm.domain.ProductMonthPriceDetail;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductMonthPriceService {

    @Autowired
    private ProductMonthPriceRepository productMonthPriceRepository;

    @Autowired
    private ProductTypeMapper productTypeMapper;

    public ProductMonthPrice findOne(String id) {
        ProductMonthPrice productMonthPrice=productMonthPriceRepository.findOne(id);
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


    public void save(ProductMonthPrice productMonthPrice){
    }

}
