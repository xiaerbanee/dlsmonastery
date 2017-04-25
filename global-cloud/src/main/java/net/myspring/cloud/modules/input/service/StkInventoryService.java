package net.myspring.cloud.modules.input.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.input.domain.StkInventory;
import net.myspring.cloud.modules.input.mapper.StkInventoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lihx on 2017/4/6.
 */
@Service
@KingdeeDataSource
public class StkInventoryService {
    @Autowired
    private StkInventoryMapper stkInventoryMapper;

    public List<StkInventory> findByfStockIds(List<String> stockIds){
        return stkInventoryMapper.findByfStockIds(stockIds);
    }

    public StkInventory findByStockIdAndMaterialId(String depotId,String productId){
        return stkInventoryMapper.findByStockIdAndMaterialId(depotId,productId);
    }
}
