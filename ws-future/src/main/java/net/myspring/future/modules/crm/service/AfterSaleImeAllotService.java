package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.AfterSaleImeAllot;
import net.myspring.future.modules.crm.mapper.AfterSaleImeAllotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AfterSaleImeAllotService {

    @Autowired
    private AfterSaleImeAllotMapper afterSaleImeAllotMapper;

    public Page findPage(Pageable pageable, Map<String,Object> map){
        Page<AfterSaleImeAllot> page=afterSaleImeAllotMapper.findPage(pageable,map);
        return page;
    }

    public AfterSaleImeAllot findOne(String id){
        AfterSaleImeAllot afterSaleImeAllot=afterSaleImeAllotMapper.findOne(id);
        return afterSaleImeAllot;
    }

}
