package net.myspring.cloud.modules.kingdee.web;

import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import net.myspring.cloud.modules.kingdee.service.BdMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lihx on 2017/5/12.
 */
@RestController
@RequestMapping(value = "kingdee/bdMaterial")
public class BdMaterialController {
    @Autowired
    private BdMaterialService bdMaterialService;

    @RequestMapping(value = "getNameByNameLike")
    public List<String> getNameByNameLike(String name){
        return bdMaterialService.getNameByNameLike(name);
    }

    @RequestMapping(value = "getByName")
    public List<BdMaterial> getByName(String name){
        return bdMaterialService.getByName(name);
    }

}
