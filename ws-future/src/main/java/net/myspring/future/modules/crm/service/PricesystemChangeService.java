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
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
//        PricesystemChange pricesystemChange=pricesystemChangeRepository.findOne(pricesystemChangeForm.getProductId());
//        ReflectionUtil.copyProperties(pricesystemChangeForm,pricesystemChange);
//        TODO 需要修改该方法，不要在sql里写update
//        pricesystemChangeRepository.updateRemark(pricesystemChangeForm);
    }


    public void batchAudit(String[] ids,Boolean pass){
//        TODO 需要修改该方法，不要在sql里写update
//        pricesystemChangeRepository.audit(ids,pass);
    }

    public void audit(String id,Boolean pass){
        //        TODO 需要修改该方法，不要在sql里写update
//        pricesystemChangeRepository.auditOperation(id, pass);
    }

    public List<List<Object>> getPricesystemDetail(List<String> productIdList){
        return null;
    }

}
