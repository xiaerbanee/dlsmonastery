package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.basic.mapper.PricesystemDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/5/15.
 */
@Component
public class PricesystemDetailManager {


    @Autowired
    private PricesystemDetailMapper pricesystemDetailMapper;

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
