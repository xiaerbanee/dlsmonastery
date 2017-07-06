package net.myspring.tool.modules.oppo.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.tool.common.client.ProductClient;
import net.myspring.tool.common.domain.ProductEntity;
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
@RequestMapping(value = "oppo/oppoPlantSendImeiPpsel")
public class OppoPlantSendImeiPpselController {
    @Autowired
    private ProductClient productClient;

}