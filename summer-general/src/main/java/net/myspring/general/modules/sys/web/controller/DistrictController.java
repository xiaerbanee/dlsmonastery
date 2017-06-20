package net.myspring.general.modules.sys.web.controller;

import net.myspring.general.modules.sys.dto.DistrictDto;
import net.myspring.general.modules.sys.dto.TownDto;
import net.myspring.general.modules.sys.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "sys/district")
public class DistrictController {

    @Autowired
    private DistrictService districtService;

    @RequestMapping(value = "search")
    public List<DistrictDto> search(String key) {
        List<DistrictDto> districtDtoList =districtService.findByNameLike(key);
        return districtDtoList;
    }

    @RequestMapping(value = "findByIds")
    public List<DistrictDto> findByIds(@RequestParam("idStr") List<String> ids){
        List<DistrictDto> districtDtoList=districtService.findByIds(ids);
        return districtDtoList;
    }

    @RequestMapping(value = "findOne")
    public DistrictDto findOne(String id) {
        return districtService.findOne(id);
    }
}
