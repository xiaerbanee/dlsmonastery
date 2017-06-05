package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import net.myspring.cloud.modules.kingdee.repository.BdMaterialRepository;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lihx on 2017/5/12.
 */
@Service
@KingdeeDataSource
public class BdMaterialService {
    @Autowired
    private BdMaterialRepository bdMaterialRepository;

    public BdMaterial findByName(String name){
        if (StringUtils.isNotBlank(name)){
            return bdMaterialRepository.findByName(name);
        }
        return null;
    }

}
