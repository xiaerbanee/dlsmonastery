package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.AfterSaleProductAllot;
import net.myspring.future.modules.crm.mapper.AfterSaleProductAllotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AfterSaleProductAllotService {

    @Autowired
    private AfterSaleProductAllotMapper afterSaleProductAllotMapper;

    public Page findPage(Pageable pageable, Map<String,Object> map){
        Page<AfterSaleProductAllot> page=afterSaleProductAllotMapper.findPage(pageable,map);
        return page;
    }

    public AfterSaleProductAllot findOne(String id){
        AfterSaleProductAllot afterSaleProductAllot=afterSaleProductAllotMapper.findOne(id);
        return afterSaleProductAllot;
    }

}
