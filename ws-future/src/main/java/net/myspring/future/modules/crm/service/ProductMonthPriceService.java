package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.repository.ProductTypeRepository;
import net.myspring.future.modules.crm.domain.ProductMonthPrice;
import net.myspring.future.modules.crm.domain.ProductMonthPriceDetail;
import net.myspring.future.modules.crm.dto.ProductMonthPriceDetailDto;
import net.myspring.future.modules.crm.dto.ProductMonthPriceDto;
import net.myspring.future.modules.crm.dto.StoreAllotDto;
import net.myspring.future.modules.crm.repository.ProductMonthPriceDetailRepository;
import net.myspring.future.modules.crm.repository.ProductMonthPriceRepository;
import net.myspring.future.modules.crm.web.form.ProductMonthPriceDetailForm;
import net.myspring.future.modules.crm.web.form.ProductMonthPriceForm;
import net.myspring.future.modules.crm.web.query.ProductMonthPriceQuery;
import net.myspring.future.modules.layout.domain.ShopAllot;
import net.myspring.future.modules.layout.domain.ShopAllotDetail;
import net.myspring.future.modules.layout.dto.ShopAllotDetailDto;
import net.myspring.future.modules.layout.web.form.ShopAllotDetailForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;
import org.elasticsearch.xpack.notification.email.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ProductMonthPriceService {

    @Autowired
    private ProductMonthPriceRepository productMonthPriceRepository;
    @Autowired
    private ProductMonthPriceDetailRepository productMonthPriceDetailRepository;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private CacheUtils cacheUtils;

    public Page<ProductMonthPriceDto> findPage(Pageable pageable, ProductMonthPriceQuery productMonthPriceQuery){
        Page<ProductMonthPriceDto> page = productMonthPriceRepository.findPage(pageable,productMonthPriceQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public String checkMonth(String id, String month) {

        ProductMonthPrice productMonthPrice = productMonthPriceRepository.findByCompanyIdAndMonthAndEnabledIsTrue(RequestUtils.getCompanyId(), month);
        if(productMonthPrice == null ){
            return null;
        }else if(StringUtils.isBlank(id)){
            return "月份已经存在";
        }else if(!productMonthPrice.getId().equals(id)){
            return "月份已经存在";
        }
        return null;
    }

    public ProductMonthPriceDto findDto(String id) {
        ProductMonthPriceDto productMonthPriceDto = productMonthPriceRepository.findDto(id);
        cacheUtils.initCacheInput(productMonthPriceDto);
        return productMonthPriceDto;
    }

    public List<ProductMonthPriceDetailDto> findDetailListForNew() {

        List<ProductMonthPriceDetailDto> result = productMonthPriceDetailRepository.findDetailListForNew(RequestUtils.getCompanyId());

        cacheUtils.initCacheInput(result);

        return result;

    }

    public List<ProductMonthPriceDetailDto> findDetailListForEdit(String productMonthPriceId) {
        List<ProductMonthPriceDetailDto> result = productMonthPriceDetailRepository.findDetailListForEdit(RequestUtils.getCompanyId(), productMonthPriceId);

        cacheUtils.initCacheInput(result);

        return result;
    }

    @Transactional
    public void save(ProductMonthPriceForm productMonthPriceForm) {
        ProductMonthPrice productMonthPrice = null;
        if(productMonthPriceForm.isCreate()){
            productMonthPrice = new ProductMonthPrice();
            productMonthPrice.setMonth(productMonthPriceForm.getMonth());
            productMonthPrice.setRemarks(productMonthPriceForm.getRemarks());
            productMonthPriceRepository.save(productMonthPrice);

            batchSaveProductMonthPriceDetails(productMonthPriceForm.getProductMonthPriceDetailList(), productMonthPrice);
        }else{
            productMonthPrice = productMonthPriceRepository.findOne(productMonthPriceForm.getId());
            productMonthPrice.setRemarks( productMonthPriceForm.getRemarks());
            productMonthPriceRepository.save(productMonthPrice);


            batchSaveProductMonthPriceDetails(productMonthPriceForm.getProductMonthPriceDetailList(), productMonthPrice);
        }

    }

    @Transactional
    private void batchSaveProductMonthPriceDetails(List<ProductMonthPriceDetailForm> productMonthPriceDetailList, ProductMonthPrice productMonthPrice) {

        if(productMonthPriceDetailList == null || productMonthPriceDetailList.isEmpty()){
            return ;
        }

        Map<String, ProductMonthPriceDetail> productMonthPriceDetailMap = productMonthPriceDetailRepository.findMap(CollectionUtil.extractToList(productMonthPriceDetailList,"id"));

        List<ProductMonthPriceDetail> productMonthPriceDetailsToBeSaved = new ArrayList<>();
        for(ProductMonthPriceDetailForm productMonthPriceDetailForm : productMonthPriceDetailList){
            ProductMonthPriceDetail productMonthPriceDetail ;
            if(productMonthPriceDetailForm.isCreate()){
                productMonthPriceDetail = new ProductMonthPriceDetail();
                productMonthPriceDetail.setBaokaPrice(productMonthPriceDetailForm.getBaokaPrice() == null ? BigDecimal.ZERO : productMonthPriceDetailForm.getBaokaPrice());
                productMonthPriceDetail.setPrice2(productMonthPriceDetailForm.getPrice2() == null ? BigDecimal.ZERO : productMonthPriceDetailForm.getPrice2());
                productMonthPriceDetail.setPrice3(productMonthPriceDetailForm.getPrice3() == null ? BigDecimal.ZERO : productMonthPriceDetailForm.getPrice3());
                productMonthPriceDetail.setProductMonthPriceId(productMonthPrice.getId());
                productMonthPriceDetail.setProductTypeId(productMonthPriceDetailForm.getProductTypeId());
            }else{
                productMonthPriceDetail = productMonthPriceDetailMap.get(productMonthPriceDetailForm.getId());
                productMonthPriceDetail.setBaokaPrice(productMonthPriceDetailForm.getBaokaPrice() == null ? BigDecimal.ZERO : productMonthPriceDetailForm.getBaokaPrice());
                productMonthPriceDetail.setPrice2(productMonthPriceDetailForm.getPrice2() == null ? BigDecimal.ZERO : productMonthPriceDetailForm.getPrice2());
                productMonthPriceDetail.setPrice3(productMonthPriceDetailForm.getPrice3() == null ? BigDecimal.ZERO : productMonthPriceDetailForm.getPrice3());
            }

            productMonthPriceDetailsToBeSaved.add(productMonthPriceDetail);

        }

        productMonthPriceDetailRepository.save(productMonthPriceDetailsToBeSaved);

    }
}
