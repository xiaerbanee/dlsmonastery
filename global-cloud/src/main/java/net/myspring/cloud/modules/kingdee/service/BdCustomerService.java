package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.kingdee.mapper.BdCustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lihx on 2017/5/12.
 */
@Service
@KingdeeDataSource
public class BdCustomerService {
    @Autowired
    private BdCustomerMapper bdCustomerMapper;

    public List<String> getNameByNameLike(String name){
        return bdCustomerMapper.findNameByNameLike(name);
    }

    public List<BdCustomer> getCustomerGroupList(){
        return bdCustomerMapper.findPrimaryGroupAndPrimaryGroupName();
    }

    public List<BdCustomer> getByNameLike(String name){
        return bdCustomerMapper.findByNameLike(name);
    }
}
