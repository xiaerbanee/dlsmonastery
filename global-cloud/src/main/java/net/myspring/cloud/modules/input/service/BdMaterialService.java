package net.myspring.cloud.modules.input.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.input.domain.BdMaterial;
import net.myspring.cloud.modules.input.mapper.BdMaterialMapper;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
@Service
@KingdeeDataSource
public class BdMaterialService {
    @Autowired
    private BdMaterialMapper bdMaterialMapper;

    public List<BdMaterial> findAll(){
        return bdMaterialMapper.findAll();
    }

    public List<BdMaterial> findByDate(LocalDateTime maxOutDate){
        return bdMaterialMapper.findByDate(maxOutDate);
    }

    public List<String> getNameByNameLike(String name){
        return bdMaterialMapper.findNameByNameLike(name);
    }

    public List<NameNumberDto> getNameAndNumber(){
        return bdMaterialMapper.findNameAndNumber();
    }

}
