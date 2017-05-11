package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdStock;
import net.myspring.cloud.modules.kingdee.mapper.BdStockMapper;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
@Service
@KingdeeDataSource
public class BdStockService {
    @Autowired
    private BdStockMapper bdStockMapper;

    public List<BdStock> findAll(){
        return bdStockMapper.findAll();
    }

    public List<BdStock> findByDate(LocalDateTime maxOutDate){
        return bdStockMapper.findByDate(maxOutDate);
    }

    public List<NameNumberDto> findNameAndNumber(){
        return bdStockMapper.findNameAndNumber();
    }

    public List<String> findNameByNameLike(String name){
        return bdStockMapper.findNameByNameLike(name);
    }
}
