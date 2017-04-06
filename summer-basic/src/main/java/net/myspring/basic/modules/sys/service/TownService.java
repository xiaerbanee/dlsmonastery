package net.myspring.basic.modules.sys.service;

import net.myspring.basic.modules.sys.domain.Town;
import net.myspring.basic.modules.sys.mapper.TownMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TownService {

    @Autowired
    private TownMapper townMapper;

    public Town findOne(String id){
        Town town=townMapper.findOne(id);
        return town;
    }

    public List<Town> findByLikeName(String name){
        List<Town> townList= townMapper.findByLikeName(name);
        return townList;
    }

}
