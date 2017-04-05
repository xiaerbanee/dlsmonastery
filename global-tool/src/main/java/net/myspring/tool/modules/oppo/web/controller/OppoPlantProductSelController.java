package net.myspring.tool.modules.oppo.web.controller;

import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel;
import net.myspring.tool.modules.oppo.dto.OppoPlantProductSelDto;
import net.myspring.tool.modules.oppo.service.OppoPlantProductSelService;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuj on 2017/4/5.
 */
@RestController
@RequestMapping(value = "oppo/plantProductSel")
public class OppoPlantProductSelController {
    @Autowired
    private OppoPlantProductSelService oppoPlantProductSelService;

    @RequestMapping(value = "findOne")
    public String findOne(String id) {
        OppoPlantProductSelDto oppoPlantProductSelDto = oppoPlantProductSelService.findOne(id);
        return ObjectMapperUtils.writeValueAsString(oppoPlantProductSelDto);
    }
}
