package net.myspring.tool.modules.vivo.service;

import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.modules.vivo.domain.VivoPlantElectronicsn;
import net.myspring.tool.modules.vivo.domain.VivoPlantProducts;
import net.myspring.tool.modules.vivo.mapper.VivoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by guolm on 2017/4/15.
 */
@Service
@LocalDataSource
public class VivoPlantProductsService {

    @Autowired
    private VivoMapper vivoMapper;

    @FactoryDataSource
    public List<VivoPlantProducts> findPlantProducts() {
        return vivoMapper.findPlantProducts();
    }

    @LocalDataSource
    public List<VivoPlantElectronicsn>  findVivoPlantElectronicsns(){
        return null;
    }

}
