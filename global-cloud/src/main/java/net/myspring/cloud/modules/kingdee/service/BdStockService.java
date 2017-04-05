package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.modules.kingdee.domain.BdStock;
import net.myspring.cloud.modules.kingdee.mapper.BdStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
@Service
public class BdStockService {
    @Autowired
    private BdStockMapper bdStockMapper;

    public List<BdStock> findAll(LocalDateTime maxOutDate){
        return bdStockMapper.findAll(maxOutDate);
    }

}
