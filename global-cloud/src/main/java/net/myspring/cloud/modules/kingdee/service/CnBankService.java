package net.myspring.cloud.modules.kingdee.service;

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
public class CnBankService {
    @Autowired
    private CnBankMapper cnBankMapper;

    public List<CnBank> findAll(LocalDateTime maxOutDate){
        return cnBankMapper.findAll(maxOutDate);
    }

}
