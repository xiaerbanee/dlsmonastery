package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.AdPricesystem;
import net.myspring.future.modules.crm.mapper.AdPricesystemMapper;
import net.myspring.future.modules.crm.mapper.DepotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class AdPricesystemService {

    @Autowired
    private AdPricesystemMapper adPricesystemMapper;
    @Autowired
    private DepotMapper depotMapper;

    public AdPricesystem findOne(String id){
        AdPricesystem adPricesystem=adPricesystemMapper.findOne(id);
        return adPricesystem;
    }

    public Page<AdPricesystem> findPage(Pageable pageable, Map<String, Object> map) {
        Page<AdPricesystem> page = adPricesystemMapper.findPage(pageable, map);
        return page;
    }

    public List<AdPricesystem> findFilter(Map<String, Object> map){
        List<AdPricesystem> adPricesystemList = adPricesystemMapper.findFilter(map);
        return adPricesystemList;
    }

    @Transactional
    public void save(AdPricesystem adPricesystem){
    }

    public void delete(AdPricesystem adPricesystem){
        adPricesystem.setEnabled(false);
        adPricesystemMapper.update(adPricesystem);
    }

}
