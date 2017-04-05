package net.myspring.tool.modules.oppo.service;


import net.myspring.mybatis.mapper.CrudMapper;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel;
import net.myspring.tool.modules.oppo.dto.OppoPlantProductSelDto;
import net.myspring.tool.modules.oppo.mapper.OppoPlantProductSelMapper;
import net.myspring.util.mapper.BeanMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OppoPlantProductSelService {

    @Autowired
    private OppoPlantProductSelMapper oppoPlantProductSelMapper;

    public OppoPlantProductSelDto findOne(String id) {
        OppoPlantProductSel oppoPlantProductSel = oppoPlantProductSelMapper.findOne(id);
        OppoPlantProductSelDto oppoPlantProductSelDto = BeanMapper.convertDto(oppoPlantProductSel,OppoPlantProductSelDto.class);
        return oppoPlantProductSelDto;
    }
}
