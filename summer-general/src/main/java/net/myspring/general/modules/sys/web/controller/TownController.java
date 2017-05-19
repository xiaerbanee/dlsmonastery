package net.myspring.general.modules.sys.web.controller;

import com.google.common.collect.Lists;
import net.myspring.general.modules.sys.dto.TownDto;
import net.myspring.general.modules.sys.service.TownService;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "sys/town")
public class TownController {

    @Autowired
    private TownService townService;

    @RequestMapping(value = "search")
    public List<TownDto> search(String name){
        List<TownDto> townDtoList= Lists.newArrayList();
        if(StringUtils.isNotBlank(name)){
            townDtoList=townService.findByNameLike(name);
        }
        return townDtoList;
    }

    @RequestMapping(value = "findOne")
    public TownDto findOne(String id){
        TownDto townDtoList=townService.findDto(id);
        return townDtoList;
    }

}
