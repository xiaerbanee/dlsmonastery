package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.AfterSaleStoreAllot;
import net.myspring.future.modules.crm.mapper.AfterSaleStoreAllotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AfterSaleStoreAllotService {

    @Autowired
    private AfterSaleStoreAllotMapper afterSaleStoreAllotMapper;

    public Page findPage(Pageable pageable, Map<String,Object> map){
        Page<AfterSaleStoreAllot> page=afterSaleStoreAllotMapper.findPage(pageable,map);
        return page;
    }

    public AfterSaleStoreAllot findOne(String id){
        AfterSaleStoreAllot afterSaleStoreAllot=afterSaleStoreAllotMapper.findOne(id);
        return afterSaleStoreAllot;
    }
}
