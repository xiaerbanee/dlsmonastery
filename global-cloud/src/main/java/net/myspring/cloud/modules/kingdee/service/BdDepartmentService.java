package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdDepartment;
import net.myspring.cloud.modules.kingdee.mapper.BdDepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by lihx on 2017/4/6.
 */
@Service
@KingdeeDataSource
public class BdDepartmentService {
    @Autowired
    private BdDepartmentMapper bdDepartmentMapper;

    public BdDepartment findByCustomerId(String customerId){
        return bdDepartmentMapper.findByCustomerId(customerId);
    }

    public List<BdDepartment> findAll(){
        return bdDepartmentMapper.findAll();
    }
}
