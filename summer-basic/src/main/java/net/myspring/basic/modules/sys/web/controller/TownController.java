package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.domain.Town;
import net.myspring.basic.modules.sys.service.TownService;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "sys/town")
public class TownController {

    @Autowired
    private TownService townService;

    @RequestMapping(value = "search")
    public String search(String name) {
        List<Town> townList = Lists.newArrayList();
        if (StringUtils.isNotBlank(name)) {
            townList = townService.findByLikeName(name);
        }
        return ObjectMapperUtils.writeValueAsString(townList);
    }
}
