package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.domain.Town;
import net.myspring.basic.modules.sys.dto.TownDto;
import net.myspring.basic.modules.sys.service.TownService;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
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
    public List<TownDto> search(String name) {
        List<TownDto> townDtoList = Lists.newArrayList();
        if (StringUtils.isNotBlank(name)) {
           List<Town> townList = townService.findByLikeName(name);
           townDtoList= BeanUtil.map(townList,TownDto.class);
        }
        return townDtoList;
    }
}
