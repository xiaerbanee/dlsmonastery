package net.myspring.tool.modules.oppo.service;


import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel;
import net.myspring.tool.modules.oppo.dto.OppoPlantProductSelDto;
import net.myspring.tool.modules.oppo.manager.GreetingManager;
import net.myspring.tool.modules.oppo.mapper.OppoPlantProductSelMapper;
import net.myspring.util.mapper.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@LocalDataSource
public class OppoPlantProductSelService {

    @Autowired
    private OppoPlantProductSelMapper oppoPlantProductSelMapper;

    @Autowired
    private GreetingManager greetingManager;

    public OppoPlantProductSelDto findOne(String id) {
        greetingManager.greet();
        OppoPlantProductSel oppoPlantProductSel = oppoPlantProductSelMapper.findOne(id);
        OppoPlantProductSelDto oppoPlantProductSelDto = BeanMapper.convertDto(oppoPlantProductSel,OppoPlantProductSelDto.class);
        return oppoPlantProductSelDto;
    }
}
