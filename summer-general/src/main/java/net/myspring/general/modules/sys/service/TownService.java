package net.myspring.general.modules.sys.service;

import net.myspring.general.modules.sys.domain.Town;
import net.myspring.general.modules.sys.dto.TownDto;
import net.myspring.general.modules.sys.repository.TownRepository;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TownService {

    @Autowired
    private TownRepository townRepository;

    public List<TownDto> findByNameLike(String name){
        List<Town> townList=townRepository.findTop20ByTownNameContaining(name);
        List<TownDto> townDtoList= BeanUtil.map(townList,TownDto.class);
        return townDtoList;
    }

    public List<TownDto> findByIds(List<String> ids){
        List<Town> townList=townRepository.findByIdIn(ids);
        List<TownDto> townDtoList=BeanUtil.map(townList,TownDto.class);
        return townDtoList;
    }

    public TownDto findOne(String id){
        Town town=townRepository.findOne(id);
        TownDto townDto=BeanUtil.map(town,TownDto.class);
        return townDto;
    }
}
