package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdDepartment;
import net.myspring.cloud.modules.kingdee.repository.BdDepartmentRepository;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by lihx on 2017/5/17.
 */
@Service
@KingdeeDataSource
@Transactional
public class BdDepartmentService {
    @Autowired
    private BdDepartmentRepository bdDepartmentRepository;

    public List<BdDepartment> findByNameLike(String name) {
        if (StringUtils.isNotBlank(name)){
            return bdDepartmentRepository.findByNameLike(name);
        }
        return null;
    }

    public List<BdDepartment> findAll() {
        return bdDepartmentRepository.findAll();
    }

    public BdDepartment findByCustId(String custId){
        return bdDepartmentRepository.findByCustId(custId);
    }
}
