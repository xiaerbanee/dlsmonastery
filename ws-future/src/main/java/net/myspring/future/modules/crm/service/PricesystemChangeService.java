package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Pricesystem;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.PricesystemDto;
import net.myspring.future.modules.basic.repository.PricesystemDetailRepository;
import net.myspring.future.modules.basic.repository.PricesystemRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.PricesystemChange;
import net.myspring.future.modules.crm.dto.PricesystemChangeDto;
import net.myspring.future.modules.crm.repository.PricesystemChangeRepository;
import net.myspring.future.modules.crm.web.form.PricesystemChangeForm;
import net.myspring.future.modules.crm.web.query.PricesystemChangeQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class PricesystemChangeService {

    @Autowired
    private PricesystemChangeRepository pricesystemChangeRepository;
    @Autowired
    private PricesystemRepository pricesystemRepository;
    @Autowired
    private PricesystemDetailRepository pricesystemDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CacheUtils cacheUtils;


    public PricesystemChange findOne(String id) {
        PricesystemChange pricesystemChange = pricesystemChangeRepository.findOne(id);
        return pricesystemChange;
    }

    public Page<PricesystemChangeDto> findPage(Pageable pageable, PricesystemChangeQuery pricesystemChangeQuery) {
        Page<PricesystemChangeDto> page = pricesystemChangeRepository.findPage(pageable,pricesystemChangeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public PricesystemChangeQuery getQuery(PricesystemChangeQuery pricesystemChangeQuery){
        pricesystemChangeQuery.getExtra().put("statusList",AuditStatusEnum.getList());
        pricesystemChangeQuery.getExtra().put("pricesystems",BeanUtil.map(pricesystemRepository.findAllEnabled(), PricesystemDto.class));
        return pricesystemChangeQuery;
    }

    public PricesystemChangeForm getForm(PricesystemChangeForm pricesystemChangeForm){
        return pricesystemChangeForm;
    }

    public void save(PricesystemChangeForm pricesystemChangeForm){
        if(pricesystemChangeForm.getProductIds().size() == 0){
            return;
        }
        Map<String,Product> productMap = CollectionUtil.extractToMap(productRepository.findAll(pricesystemChangeForm.getProductIds()),"name");
        String json = HtmlUtils.htmlUnescape(pricesystemChangeForm.getData());
        List<List<Object>> data = ObjectMapperUtils.readValue(json, ArrayList.class);
        List<Pricesystem> pricesystems = pricesystemRepository.findAllEnabled();
        List<PricesystemChange> pricesystemChanges = Lists.newArrayList();
        for(List<Object> rows:data){
            if(rows.size()-pricesystems.size() != 1){
                return;
            }
            for(int i = 1;i<rows.size();i++){
                String newPriceStr = StringUtils.toString(rows.get(i)).trim();
                BigDecimal newPrice =  BigDecimal.ZERO;
                if(StringUtils.isNotBlank(newPriceStr)) {
                    newPrice = new BigDecimal(newPriceStr);
                }
                Product product  =productMap.get(rows.get(0));
                if(product == null){
                    return;
                }
                PricesystemDetail pricesystemDetail = pricesystemDetailRepository.findByPricesystemIdAndProductId(pricesystems.get(i-1).getId(),product.getId());
                BigDecimal oldPrice = BigDecimal.ZERO;
                if(pricesystemDetail !=null&&pricesystemDetail.getPrice()!=null){
                    oldPrice = pricesystemDetail.getPrice();
                }
                if(newPrice.compareTo(oldPrice) !=0){
                    PricesystemChange pricesystemChange = new PricesystemChange();
                    pricesystemChange.setProductId(product.getId());
                    pricesystemChange.setPricesystemId(pricesystems.get(i-1).getId());
                    pricesystemChange.setStatus(AuditStatusEnum.申请中.name());
                    pricesystemChange.setNewPrice(newPrice);
                    pricesystemChange.setOldPrice(oldPrice);
                    pricesystemChange.setRemarks(pricesystemChangeForm.getRemarks());
                    pricesystemChanges.add(pricesystemChange);
                }
            }
        }
        pricesystemChangeRepository.save(pricesystemChanges);
    }


    public void batchAudit(String[] ids,Boolean pass){
        List<String> idList = Arrays.asList(ids);
        if(CollectionUtil.isNotEmpty(idList)){
            for(String id:idList){
                audit(id,pass);
            }
        }
    }

    public void audit(String id,Boolean pass){
        if(StringUtils.isNotBlank(id)){
            PricesystemChange pricesystemChange = pricesystemChangeRepository.findOne(id);
            if(pass){
                PricesystemDetail pricesystemDetail = pricesystemDetailRepository.findByPricesystemIdAndProductId(pricesystemChange.getPricesystemId(), pricesystemChange.getProductId());
                if (pricesystemDetail == null) {
                    pricesystemDetail = new PricesystemDetail();
                    pricesystemDetail.setProductId(pricesystemChange.getProductId());
                    pricesystemDetail.setPricesystemId(pricesystemChange.getPricesystemId());
                }
                pricesystemDetail.setPrice(pricesystemChange.getNewPrice());
                pricesystemDetailRepository.save(pricesystemDetail);
                pricesystemChange.setLocked(true);
            }
            pricesystemChange.setAuditBy(RequestUtils.getAccountId());
            pricesystemChange.setStatus(pass ? AuditStatusEnum.已通过.name() : AuditStatusEnum.未通过.name());
            pricesystemChange.setAuditDate(LocalDateTime.now());
            pricesystemChangeRepository.save(pricesystemChange);
        }
    }


}
