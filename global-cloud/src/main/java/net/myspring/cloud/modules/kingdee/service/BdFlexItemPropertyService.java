package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemProperty;
import net.myspring.cloud.modules.kingdee.repository.BdFlexItemPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 核算维度
 * Created by lihx on 2017/6/22.
 */
@Service
@KingdeeDataSource
public class BdFlexItemPropertyService {
    @Autowired
    private BdFlexItemPropertyRepository bdFlexItemPropertyRepository;

    public List<BdFlexItemProperty> findAll(){
        return bdFlexItemPropertyRepository.findAll();
    }
}
