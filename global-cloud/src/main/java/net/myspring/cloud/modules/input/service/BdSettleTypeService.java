package net.myspring.cloud.modules.input.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.input.domain.BdSettleType;
import net.myspring.cloud.modules.input.mapper.BdSettleTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lihx on 2017/4/6.
 */
@Service
@KingdeeDataSource
public class BdSettleTypeService {
    @Autowired
    private BdSettleTypeMapper bdSettleTypeMapper;

    public List<BdSettleType> findAll(){
        return bdSettleTypeMapper.findAll();
    }
}
