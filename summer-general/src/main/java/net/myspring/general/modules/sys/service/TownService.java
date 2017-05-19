package net.myspring.general.modules.sys.service;

import net.myspring.general.modules.sys.domain.Town;
import net.myspring.general.modules.sys.dto.TownDto;
import net.myspring.general.modules.sys.mapper.TownMapper;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TownService {

    @Autowired
    private TownMapper townMapper;

    public List<TownDto> findByNameLike(String name){
        List<Town> townList=townMapper.findByNameLike(name);
        List<TownDto> townDtoList= BeanUtil.map(townList,TownDto.class);
        return townDtoList;
    }

    public TownDto findDto(String id){
        Town town=townMapper.findOne(id);
        TownDto townDto=BeanUtil.map(town,TownDto.class);
        return townDto;
    }
}
