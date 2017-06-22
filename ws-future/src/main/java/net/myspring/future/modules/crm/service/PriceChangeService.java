package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.common.enums.PriceChangeStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RandomUtils;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.crm.domain.PriceChangeIme;
import net.myspring.future.modules.crm.repository.PriceChangeImeRepository;
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
import java.util.Map;
import java.util.Random;

@Service
@Transactional
public class PriceChangeService {

    @Autowired
    private PriceChangeRepository priceChangeRepository;
    @Autowired
    private PriceChangeProductRepository priceChangeProductRepository;
    @Autowired
    private PriceChangeImeRepository priceChangeImeRepository;
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
            if(productTypeList!=null){
                String productTypeIds = "";
                for (String productTypeId:productTypeList){
                    productTypeIds += productTypeId+",";
                }
                priceChangeDto.setProductTypeIds(productTypeIds);
            }
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
            priceChange.setStatus(PriceChangeStatusEnum.抽检中.name());
            priceChangeRepository.save(priceChange);

            List<PriceChangeProduct> priceChangeProducts = Lists.newArrayList();
            for(String productTypeId:priceChangeForm.getProductTypeIdList()){
                PriceChangeProduct priceChangeProduct = new PriceChangeProduct();
                priceChangeProduct.setPriceChangeId(priceChange.getId());
                priceChangeProduct.setProductTypeId(productTypeId);
                priceChangeProducts.add(priceChangeProduct);
            }
            priceChangeProductRepository.save(priceChangeProducts);
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

        Integer imeQty = priceChangeImeRepository.countByPriceChangeId(priceChangeForm.getId());
        Integer checkQty = (imeQty * priceChangeForm.getCheckPercent()) / 100;
        Integer leftQty = checkQty;
        //根据抽检比例抽检
        List<PriceChangeIme> result = Lists.newArrayList();
        //保卡已经上报的全抽
        List<PriceChangeIme> priceChangeImes = priceChangeImeRepository.findByPriceChangeIdAndUploadDateIsNotNullAndEnabledIsTrue(priceChange.getId());
        if (CollectionUtil.isNotEmpty(priceChangeImes)) {
            for (PriceChangeIme priceChangeIme : priceChangeImes) {
                priceChangeIme.setStatus(AuditStatusEnum.申请中.name());
                priceChangeIme.setIsCheck(true);
                result.add(priceChangeIme);
            }
        }
        leftQty = leftQty - result.size();
        //如果不够，在已销售的串码中抽检
        if (leftQty > 0) {
            priceChangeImes = priceChangeImeRepository.findCheckList(priceChangeForm.getId(), priceChange.getPriceChangeDate());
            //如果剩余需要抽检的数量比实际要多
            if (leftQty >= priceChangeImes.size()) {
                result.addAll(priceChangeImes);
                leftQty = leftQty - priceChangeImes.size();
            } else {
                List<Integer> randomList = RandomUtils.getRandomList(0, priceChangeImes.size() - 1, leftQty);
                for (Integer index : randomList) {
                    PriceChangeIme priceChangeIme = priceChangeImes.get(index);
                    priceChangeIme.setStatus(AuditStatusEnum.申请中.name());
                    priceChangeIme.setIsCheck(true);
                    result.add(priceChangeIme);
                }
                leftQty = 0;
            }
        }
        //还需抽取，则从所剩下中抽取
        if (leftQty > 0) {
            priceChangeImes = priceChangeImeRepository.findByPriceChangeId(priceChange.getId());
            List<Integer> randomList = RandomUtils.getRandomList(0, priceChangeImes.size() - 1, leftQty);
            for (Integer index : randomList) {
                PriceChangeIme priceChangeIme = priceChangeImes.get(index);
                priceChangeIme.setStatus(AuditStatusEnum.申请中.name());
                priceChangeIme.setIsCheck(true);
                result.add(priceChangeIme);
            }
        }
        priceChangeImeRepository.save(result);


    }


}
