package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.FaAssetType;
import net.myspring.cloud.modules.kingdee.repository.FaAssetTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 资产类别
 * Created by lihx on 2017/8/9.
 */
@Service
@KingdeeDataSource
@Transactional(readOnly = true)
public class FaAssetTypeService {
    @Autowired
    private FaAssetTypeRepository faAssetTypeRepository;

    public List<FaAssetType> findAll(){
        return faAssetTypeRepository.findAll();
    }
}
