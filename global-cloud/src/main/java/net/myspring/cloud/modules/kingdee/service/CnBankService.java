package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.CnBank;
import net.myspring.cloud.modules.kingdee.mapper.CnBankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
@Service
@KingdeeDataSource
public class CnBankService {
    @Autowired
    private CnBankMapper cnBankMapper;

    public List<CnBank> findAll(){
        return cnBankMapper.findAll();
    }

    public List<CnBank> findByDate(LocalDateTime maxOutDate){
        return cnBankMapper.findByDate(maxOutDate);
    }

}
