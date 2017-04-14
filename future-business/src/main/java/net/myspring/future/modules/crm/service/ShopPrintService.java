package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.ShopPrint;
import net.myspring.future.modules.crm.mapper.ShopPrintMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(readOnly = false)
public class ShopPrintService {

    @Autowired
    private ShopPrintMapper shopPrintMapper;


    public Page<ShopPrint> findPage(Pageable pageable, Map<String, Object> map) {
        Page<ShopPrint> page = shopPrintMapper.findPage(pageable, map);
        return page;
    }

    public ShopPrint findOne(String id){
        ShopPrint shopPrint = shopPrintMapper.findOne(id);
        return shopPrint;
    }

    public ShopPrint save(ShopPrint shopPrint) {
        shopPrintMapper.save(shopPrint);
        return shopPrint;
    }

    public void audit(ShopPrint shopPrint, boolean pass, String comment) {
        shopPrintMapper.update(shopPrint);
    }

    public void notify(ShopPrint shopPrint) {
    }
}
