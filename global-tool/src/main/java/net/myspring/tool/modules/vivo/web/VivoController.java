package net.myspring.tool.modules.vivo.web;

import net.myspring.tool.modules.vivo.domain.VivoPlantProducts;
import net.myspring.tool.modules.vivo.service.VivoPlantProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by guolm on 2017/4/15.
 */
@RestController
@RequestMapping(value = "vivo")
public class VivoController {

    @Autowired
    private VivoPlantProductsService vivoPlantProductsService;

    @RequestMapping(value="findPlantProducts")
    public void findPlantProducts() {
        List<VivoPlantProducts> vivoPlantProductses = vivoPlantProductsService.findPlantProducts();
    }
}
