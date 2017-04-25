package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.kingdee.mapper.BdCustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
@Service
@KingdeeDataSource
public class BdCustomerService {
    @Autowired
    private BdCustomerMapper bdCustomerMapper;

    public List<BdCustomer> findAll(LocalDateTime maxOutDate){
        return bdCustomerMapper.findAll(maxOutDate);
    }

    public List<BdCustomer> findByName(String name){
        return bdCustomerMapper.findByName(name);
    }

    public List<String> findName(){
        return bdCustomerMapper.findName();
    }

}
