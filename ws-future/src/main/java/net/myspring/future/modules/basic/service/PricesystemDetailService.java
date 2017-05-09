package net.myspring.future.modules.basic.service;

import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.basic.mapper.PricesystemDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PricesystemDetailService {

    @Autowired
    private PricesystemDetailMapper pricesystemDetailMapper;

    public PricesystemDetail findOne(String id){
        return pricesystemDetailMapper.findOne(id);
    }

    public Map<String, PricesystemDetail> findProductPricesystemDetailMap(String depotId){
        List<PricesystemDetail> list =  pricesystemDetailMapper.findByDepotId(depotId);
        Map<String, PricesystemDetail> result = new HashMap<>();
        if(list!=null){
            for(PricesystemDetail each : list){
                result.put(each.getProductId(), each);
            }
        }
        return result;
    }
}
