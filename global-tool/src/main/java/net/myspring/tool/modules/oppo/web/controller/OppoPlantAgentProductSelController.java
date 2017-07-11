package net.myspring.tool.modules.oppo.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.tool.modules.oppo.dto.OppoPlantAgentProductSelDto;
import net.myspring.tool.modules.oppo.service.OppoPlantAgentProductSelService;
import net.myspring.tool.modules.oppo.web.form.OppoPlantAgentProductSqlForm;
import net.myspring.tool.modules.oppo.web.query.OppoPlantAgentProductSelQuery;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "factory/oppo/oppoPlantAgentProductSel")
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
        return oppoPlantAgentProductSelQuery;
    }

    @RequestMapping(value = "form")
    public OppoPlantAgentProductSqlForm form(OppoPlantAgentProductSqlForm oppoPlantAgentProductSqlForm){
        return oppoPlantAgentProductSelService.form(oppoPlantAgentProductSqlForm);
    }

    @RequestMapping(value = "save")
    public RestResponse save(String data){
        if(StringUtils.isNotBlank(data)){
            oppoPlantAgentProductSelService.save(data);
        }
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

}