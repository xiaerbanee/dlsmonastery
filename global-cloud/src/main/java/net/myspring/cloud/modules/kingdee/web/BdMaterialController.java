package net.myspring.cloud.modules.kingdee.web;

import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import net.myspring.cloud.modules.kingdee.service.BdMaterialService;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
@RestController
@RequestMapping(value = "input/bdMaterial")
public class BdMaterialController {
    @Autowired
    private BdMaterialService bdMaterialService;

    @RequestMapping(value = "getMaterialList", method = RequestMethod.GET)
    public List<BdMaterial> getMaterialList(String maxOutDate) {
        LocalDateTime localDateTime = null;
        if(StringUtils.isNotBlank(maxOutDate)){
            localDateTime= LocalDateTimeUtils.parse(maxOutDate);
        }
        List<BdMaterial>  materialList = bdMaterialService.findByDate(localDateTime);
        return materialList;
    }

    @RequestMapping(value = "getNameByNameLike")
    public List<String> getNameByNameLike(String name){
        return bdMaterialService.getNameByNameLike(name);
    }

    @RequestMapping(value = "getNumberByNumberLike")
    public List<String> getNumberByNumberLike(String number){
        return bdMaterialService.getNumberByNumberLike(number);
    }

    @RequestMapping(value = "getNameAndNumber")
    public List<NameNumberDto> getNameAndNumber(){
        return bdMaterialService.getNameAndNumber();
    }
}
