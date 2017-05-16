package net.myspring.cloud.modules.kingdee.web;

import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import net.myspring.cloud.modules.kingdee.service.BdMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lihx on 2017/5/12.
 */
@RestController
@RequestMapping(value = "kingdee/bdMaterial")
public class BdMaterialController {
    @Autowired
    private BdMaterialService bdMaterialService;

    @RequestMapping(value = "findByNameLike")
    public List<String> findByNameLike(String name){
        List<BdMaterial> bdMaterialList = bdMaterialService.findByNameLike(name);
        return bdMaterialList.stream().map(bdMaterial -> bdMaterial.getFName().trim()).collect(Collectors.toList());
    }

    @RequestMapping(value = "findByName")
    public BdMaterial findByName(String name){
        return bdMaterialService.findByName(name);
    }

}
