package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdDepartment;
import net.myspring.cloud.modules.kingdee.repository.BdDepartmentRepository;
import net.myspring.cloud.modules.kingdee.web.query.BdDepartmentQuery;
import net.myspring.cloud.modules.kingdee.web.query.BdSupplierQuery;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lihx on 2017/5/17.
 */
@Service
@KingdeeDataSource
@Transactional(readOnly = true)
public class BdDepartmentService {
    @Autowired
    private BdDepartmentRepository bdDepartmentRepository;

    public Page<BdDepartment> findPageIncludeForbid(Pageable pageable, BdDepartmentQuery bdDepartmentQuery) {
        bdDepartmentQuery.setSort("t1.fdeptid,DESC");
        Page<BdDepartment> bdDepartmentPage = bdDepartmentRepository.findPageIncludeForbid(pageable,bdDepartmentQuery);
        return bdDepartmentPage;
    }

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

    //应付报表
    public BdDepartmentQuery getQueryForSupplierPayable(){
        BdDepartmentQuery bdDepartmentQuery = new BdDepartmentQuery();
        bdDepartmentQuery.setSort("t1.fdeptid,DESC");
        return bdDepartmentQuery;
    }
}
