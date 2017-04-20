package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.crm.domain.Express;
import net.myspring.future.modules.crm.mapper.ExpressMapper;
import net.myspring.future.modules.crm.mapper.ExpressOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class ExpressService {

    @Autowired
    private ExpressMapper expressMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private ExpressOrderMapper expressOrderMapper;

    public Express findOne(String id){
        Express express=expressMapper.findOne(id);
        return express;
    }

    public Page<Express> findPage(Pageable pageable, Map<String, Object> map) {
        Page<Express> page = expressMapper.findPage(pageable, map);
        return page;
    }

    @Transactional
    public void save(Express express){
    }

    @Transactional
    public void delete(Express express){
        expressMapper.deleteById(express.getId());
    }
}
