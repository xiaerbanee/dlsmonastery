package net.myspring.future.modules.crm.service;

import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.dto.PricesystemDto;
import net.myspring.future.modules.basic.repository.PricesystemRepository;
import net.myspring.future.modules.crm.domain.PricesystemChange;
import net.myspring.future.modules.crm.dto.PricesystemChangeDto;
import net.myspring.future.modules.crm.repository.PricesystemChangeRepository;
import net.myspring.future.modules.crm.web.form.PricesystemChangeForm;
import net.myspring.future.modules.crm.web.query.PricesystemChangeQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class PricesystemChangeService {

    @Autowired
    private PricesystemChangeRepository pricesystemChangeRepository;
    @Autowired
    private PricesystemRepository pricesystemRepository;
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
        pricesystemChangeQuery.setStatusList(AuditStatusEnum.getList());
        pricesystemChangeQuery.setPricesystems(BeanUtil.map(pricesystemRepository.findAllEnabled(), PricesystemDto.class));
        return pricesystemChangeQuery;
    }

    public void save(PricesystemChangeForm pricesystemChangeForm){

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
                pricesystemChange.setStatus(AuditStatusEnum.已通过.name());
            }else{
                pricesystemChange.setStatus(AuditStatusEnum.未通过.name());
            }
            pricesystemChangeRepository.save(pricesystemChange);
        }
    }

    public List<List<Object>> getPricesystemDetail(List<String> productIdList){
        return null;
    }

}
