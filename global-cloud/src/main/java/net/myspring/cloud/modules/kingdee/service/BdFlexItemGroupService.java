package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemGroup;
import net.myspring.cloud.modules.kingdee.repository.BdFlexItemGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * 核算维度组
 * Created by lihx on 2017/6/16.
 */
@Service
@KingdeeDataSource
@Transactional(readOnly = true)
public class BdFlexItemGroupService {
    @Autowired
    private BdFlexItemGroupRepository bdFlexItemGroupRepository;

    public List<BdFlexItemGroup> findAll(){
        return bdFlexItemGroupRepository.findAll();
    }

}
