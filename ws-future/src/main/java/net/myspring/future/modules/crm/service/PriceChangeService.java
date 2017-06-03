package net.myspring.future.modules.crm.service;

import net.myspring.future.common.enums.PriceChangeStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.crm.repository.PriceChangeRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.PriceChange;
import net.myspring.future.modules.crm.domain.PriceChangeProduct;
import net.myspring.future.modules.crm.dto.PriceChangeDto;
import net.myspring.future.modules.crm.repository.PriceChangeProductRepository;
import net.myspring.future.modules.crm.web.form.PriceChangeForm;
import net.myspring.future.modules.crm.web.query.PriceChangeQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class PriceChangeService {

    @Autowired
    private PriceChangeRepository priceChangeRepository;
    @Autowired
    private PriceChangeProductRepository priceChangeProductRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public PriceChange findNearPriceChange(){
        return priceChangeRepository.findNearPriceChange();
    }

    public List<PriceChange> findAllByEnabledAndDate(LocalDateTime uploadEndDate) {
        return priceChangeRepository.findByEnabledIsTrueAndUploadEndDateGreaterThanEqualOrderByIdDesc(uploadEndDate);
    }

    public PriceChangeDto findOne(String id){
        PriceChangeDto priceChangeDto = new PriceChangeDto();
        if(id!=null){
            PriceChange priceChange = priceChangeRepository.findOne(id);
            priceChangeDto = BeanUtil.map(priceChange,PriceChangeDto.class);
            List<String> productTypeList = CollectionUtil.extractToList(priceChangeProductRepository.findByPriceChangeId(id),"productTypeId");
            HashSet hashSet = new HashSet(productTypeList);
            productTypeList.clear();
            productTypeList.addAll(hashSet);
            priceChangeDto.setProductTypeIdList(productTypeList);
        }
        return priceChangeDto;
    }

    public PriceChangeForm getForm(PriceChangeForm priceChangeForm){

        return priceChangeForm;
    }


    public Page<PriceChangeDto> findPage(Pageable pageable, PriceChangeQuery priceChangeQuery) {

        Page<PriceChangeDto> page = priceChangeRepository.findPage(pageable, priceChangeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }


    public void save(PriceChangeForm priceChangeForm){
        PriceChange priceChange;

        if(priceChangeForm.isCreate()){
            priceChange = BeanUtil.map(priceChangeForm,PriceChange.class);
            priceChange.setStatus(PriceChangeStatusEnum.上报中.name());
            priceChangeRepository.save(priceChange);

            List<String> productTypeIdList = priceChangeForm.getProductTypeIdList();
            for(String productTypeId:productTypeIdList){
                List<Product> productList = productRepository.findByProductTypeId(productTypeId);
                List<String> productIdList = CollectionUtil.extractToList(productList,"id");
                for(String productId:productIdList){
                    PriceChangeProduct priceChangeProduct = new PriceChangeProduct();
                    priceChangeProduct.setPriceChangeId(priceChange.getId());
                    priceChangeProduct.setProductId(productId);
                    priceChangeProduct.setProductTypeId(productTypeId);
                    priceChangeProductRepository.save(priceChangeProduct);
                }
            }
        }else{
            priceChange = priceChangeRepository.findOne(priceChangeForm.getId());
            priceChange.setPriceChangeDate(priceChangeForm.getPriceChangeDate());
            priceChange.setUploadEndDate(priceChangeForm.getUploadEndDate());
            priceChange.setRemarks(priceChangeForm.getRemarks());
            priceChangeRepository.save(priceChange);
        }
    }


    public void delete(PriceChangeForm priceChangeForm){
        PriceChange priceChange = priceChangeRepository.findOne(priceChangeForm.getId());
        priceChange.setName(priceChange.getName()+LocalDate.now()+"废除");
        priceChange.setEnabled(false);
        priceChangeRepository.save(priceChange);
    }


    public void check(PriceChangeForm priceChangeForm){
        PriceChange priceChange = priceChangeRepository.findOne(priceChangeForm.getId());
        priceChange.setCheckPercent(priceChangeForm.getCheckPercent());
        priceChange.setStatus(PriceChangeStatusEnum.抽检中.name());
        priceChangeRepository.save(priceChange);

        //TODO 抽检该调价项目下上传的串码


    }


}
