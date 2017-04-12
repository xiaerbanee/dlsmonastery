package net.myspring.tool.modules.oppo.service;


import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel;
import net.myspring.tool.modules.oppo.mapper.OppoPlantProductSelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@LocalDataSource
public class OppoPlantProductSelService {

    @Autowired
    private OppoPlantProductSelMapper oppoPlantProductSelMapper;

    @FactoryDataSource
    public List<OppoPlantProductSel> findFromFactory(String companyId, String password, String branchId) {
        return oppoPlantProductSelMapper.findFromFactory(companyId,password,branchId);
    }


}
