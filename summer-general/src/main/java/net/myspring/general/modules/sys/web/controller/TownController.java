package net.myspring.general.modules.sys.web.controller;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.domain.Town;
import net.myspring.basic.modules.sys.dto.TownDto;
import net.myspring.general.modules.sys.service.TownService;
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

}
