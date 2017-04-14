package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.Pricesystem;
import net.myspring.future.modules.crm.mapper.PricesystemMapper;
import net.myspring.future.modules.crm.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PricesystemService {

    @Autowired
    private PricesystemMapper pricesystemMapper;
    @Autowired
    private ProductMapper productMapper;



    public Page<Pricesystem> findPage(Pageable pageable, Map<String, Object> map) {
        Page<Pricesystem> page = pricesystemMapper.findPage(pageable, map);
        return page;
    }

    public List<Pricesystem> findAll(){
        return pricesystemMapper.findAll();
    }

    public void logicDeleteOne(Pricesystem pricesystem) {
        pricesystem.setName(pricesystem.getName()+"(废弃时间"+new Date()+")");
        pricesystem.setEnabled(false);
        pricesystemMapper.update(pricesystem);
    }

    public Pricesystem save(Pricesystem pricesystem) {
        return pricesystem;
    }

    public Pricesystem findOne(String id) {
        Pricesystem pricesystem = pricesystemMapper.findOne(id);
        return pricesystem;
    }

    public void initPricesystemDetail(Pricesystem pricesystem){
    }

}
