package net.myspring.tool.modules.oppo.web.controller;

import net.myspring.tool.modules.oppo.dto.OppoPlantAgentProductSelDto;
import net.myspring.tool.modules.oppo.service.OppoPlantAgentProductSelService;
import net.myspring.tool.modules.oppo.web.query.OppoPlantAgentProductSelQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "oppo/oppoPlantAgentProductSel")
public class OppoPlantAgentProductSelController {
    @Autowired
    private OppoPlantAgentProductSelService oppoPlantAgentProductSelService;

    @RequestMapping(value = "filter")
    public List<OppoPlantAgentProductSelDto> filter(OppoPlantAgentProductSelQuery oppoPlantAgentProductSelQuery){
        List<OppoPlantAgentProductSelDto> oppoPlantAgentProductSelDtoList = oppoPlantAgentProductSelService.findFilter(oppoPlantAgentProductSelQuery);
        return oppoPlantAgentProductSelDtoList;
    }

    @RequestMapping(value = "getQuery")
    public OppoPlantAgentProductSelQuery getQuery(OppoPlantAgentProductSelQuery oppoPlantAgentProductSelQuery){
        oppoPlantAgentProductSelQuery.setProductNameList(oppoPlantAgentProductSelService.findHasImeProduct());
        return oppoPlantAgentProductSelQuery;
    }
}