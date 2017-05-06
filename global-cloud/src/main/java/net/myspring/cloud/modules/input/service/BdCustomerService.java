package net.myspring.cloud.modules.input.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.input.domain.BdCustomer;
import net.myspring.cloud.modules.input.mapper.BdCustomerMapper;
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
public class BdCustomerService {
    @Autowired
    private BdCustomerMapper bdCustomerMapper;

    public List<BdCustomer> findAll(){
        return bdCustomerMapper.findAll();
    }

    public List<BdCustomer> findByDate(LocalDateTime maxOutDate){
        return bdCustomerMapper.findByDate(maxOutDate);
    }

    public List<BdCustomer> findByName(String name){
        return bdCustomerMapper.findByName(name);
    }

    public List<String> findNameByNameLike(String name){
        return bdCustomerMapper.findNameByNameLike(name);
    }

    public List<NameNumberDto> findPrimaryGroupAndPrimaryGroupName(){
        return bdCustomerMapper.findPrimaryGroupAndPrimaryGroupName();
    }

}
