package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.enums.DateFormat;
import net.myspring.cloud.modules.input.domain.BdMaterial;
import net.myspring.cloud.modules.input.service.BdMaterialService;
import net.myspring.cloud.modules.report.dto.NameValueDto;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            localDateTime= LocalDateTime.parse(maxOutDate, DateTimeFormatter.ofPattern(DateFormat.DATE_TIME.getValue()));
        }
        List<BdMaterial>  materialList = bdMaterialService.findByDate(localDateTime);
        return materialList;
    }

    @RequestMapping(value = "getNameByNameLike")
    public List<String> getNameByNameLike(String name){
        return bdMaterialService.getNameByNameLike(name);
    }

    @RequestMapping(value = "getNameAndNumber")
    public List<NameValueDto> getNameAndNumber(){
        return bdMaterialService.getNameAndNumber();
    }
}
