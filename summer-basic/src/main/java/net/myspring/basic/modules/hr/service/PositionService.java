package net.myspring.basic.modules.hr.service;

import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.basic.modules.hr.dto.PositionDto;
import net.myspring.basic.modules.hr.manager.PositionManager;
import net.myspring.util.mapper.BeanMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import net.myspring.basic.modules.hr.mapper.PositionMapper;

import java.util.List;

@Service
public class PositionService {

    @Autowired
    private PositionManager positionManager;

    public List<PositionDto> findAll(){
        List<Position> positionList=positionManager.findAll();
        List<PositionDto> positionDtoList= BeanMapper.convertDtoList(positionList,PositionDto.class);
        return positionDtoList;
    }

}
