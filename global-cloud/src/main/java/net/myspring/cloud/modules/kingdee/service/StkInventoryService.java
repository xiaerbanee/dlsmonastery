package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.StkInventory;
import net.myspring.cloud.modules.kingdee.repository.StkInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 其他出库单
 * Created by lihx on 2017/6/16.
 */
@Service
@KingdeeDataSource
public class StkInventoryService {
    @Autowired
    private StkInventoryRepository stkInventoryRepository;

    public List<StkInventory> findByStockIds(List<String> stockIds){
        return stkInventoryRepository.findByStockIds(stockIds);
    }
}
