package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdDepartment;
import net.myspring.cloud.modules.kingdee.mapper.BdDepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lihx on 2017/5/17.
 */
@Service
@KingdeeDataSource
public class BdDepartmentService {
    @Autowired
    private BdDepartmentMapper bdDepartmentMapper;

    public List<BdDepartment> findByNameLike(String name) {
        return bdDepartmentMapper.findByNameLike(name);
    }
}
