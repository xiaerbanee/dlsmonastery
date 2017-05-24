package net.myspring.general.modules.sys.web.controller;

import net.myspring.general.modules.sys.dto.DistrictDto;
import net.myspring.general.modules.sys.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping(value = "findOne")
    public DistrictDto findOne(String id) {
        return districtService.findOne(id);
    }
}
