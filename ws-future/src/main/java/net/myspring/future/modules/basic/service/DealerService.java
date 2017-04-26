package net.myspring.future.modules.basic.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Dealer;
import net.myspring.future.modules.basic.dto.DealerDto;
import net.myspring.future.modules.basic.manager.DealerManager;
import net.myspring.future.modules.basic.mapper.DealerMapper;
import net.myspring.future.modules.basic.web.query.DealerQuery;
import net.myspring.future.modules.basic.web.form.DealerForm;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DealerService {

    @Autowired
    private DealerMapper dealerMapper;
    @Autowired
    private DealerManager dealerManager;
    @Autowired
    private CacheUtils cacheUtils;

    public Dealer findOne(String id){
        Dealer dealer=dealerManager.findOne(id);
        return dealer;
    }

    public Page<DealerDto> findPage(Pageable pageable, DealerQuery dealerQuery) {
        Page<DealerDto> page = dealerMapper.findPage(pageable, dealerQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public Dealer save(DealerForm dealerForm) {
        Dealer dealer=BeanUtil.map(dealerForm,Dealer.class);
        dealer=dealerManager.save(dealer);
        return dealer;
    }

    public DealerForm findForm(DealerForm dealerForm){
        if(!dealerForm.isCreate()){
            Dealer dealer=dealerManager.findOne(dealerForm.getId());
            dealerForm= BeanUtil.map(dealer,DealerForm.class);
            cacheUtils.initCacheInput(dealerForm);
       }
       return dealerForm;
    }

}
