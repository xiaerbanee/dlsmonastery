package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.Dealer;
import net.myspring.future.modules.crm.mapper.DealerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DealerService {

    @Autowired
    private DealerMapper dealerMapper;

    public Dealer findOne(String id){
        Dealer dealer=dealerMapper.findOne(id);
        return dealer;
    }

    public Page<Dealer> findPage(Pageable pageable, Map<String, Object> map) {
        Page<Dealer> page = dealerMapper.findPage(pageable, map);
        return page;
    }

    public Dealer save(Dealer dealer) {
        dealerMapper.save(dealer);
        return dealer;
    }

}
