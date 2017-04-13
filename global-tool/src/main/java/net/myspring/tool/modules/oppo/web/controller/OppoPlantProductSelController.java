package net.myspring.tool.modules.oppo.web.controller;

import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel;
import net.myspring.tool.modules.oppo.service.OppoPlantProductSelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by liuj on 2017/4/5.
 */
@RestController
@RequestMapping(value = "oppo/plantProductSel")
public class OppoPlantProductSelController {
    @Autowired
    private OppoPlantProductSelService oppoPlantProductSelService;

    @RequestMapping(value = "findFromCompany")
    public List<OppoPlantProductSel> findFromFactory(String companyId, String password, String branchId) {
        return oppoPlantProductSelService.findFromFactory(companyId,password,branchId);
    }
}
