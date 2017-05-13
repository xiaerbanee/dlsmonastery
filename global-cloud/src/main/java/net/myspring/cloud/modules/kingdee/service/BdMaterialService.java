package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import net.myspring.cloud.modules.kingdee.mapper.BdMaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lihx on 2017/5/12.
 */
@Service
@KingdeeDataSource
public class BdMaterialService {
    @Autowired
    private BdMaterialMapper bdMaterialMapper;

    public List<String> getNameByNameLike(String name){
        return bdMaterialMapper.findNameByNameLike(name);
    }

    public BdMaterial getByName(String name){
        return bdMaterialMapper.findByName(name);
    }

}
