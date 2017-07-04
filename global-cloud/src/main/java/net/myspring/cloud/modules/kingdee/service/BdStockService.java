package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdStock;
import net.myspring.cloud.modules.kingdee.repository.BdStockRepository;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 仓库
 * Created by lihx on 2017/5/12.
 */
@Service
@KingdeeDataSource
@Transactional(readOnly = true)
public class BdStockService {
    @Autowired
    private BdStockRepository bdStockRepository;

    public List<BdStock> findByNameLike(String name){
        if(StringUtils.isNotBlank(name)){
            return bdStockRepository.findByNameLike(name);
        }
        return null;
    }

    public List<BdStock> findAll(){
        return bdStockRepository.findAll();
    }

    public List<BdStock> findByMaxModifyDate(LocalDateTime modifyDate){
        return bdStockRepository.findByMaxModifyDate(modifyDate);
    }
}
