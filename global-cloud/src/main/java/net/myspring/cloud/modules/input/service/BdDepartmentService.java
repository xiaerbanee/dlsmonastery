package net.myspring.cloud.modules.input.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.input.domain.BdDepartment;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import net.myspring.cloud.modules.input.mapper.BdDepartmentMapper;
import org.codehaus.jackson.sym.NameN;
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

    public List<NameNumberDto> findNameNumber(){
        return bdDepartmentMapper.findNameAndNumber();
    }

    public List<NameNumberDto> findByNameLike(String name){
        return bdDepartmentMapper.findByNameLike(name);
    }
}
