package net.myspring.basic.modules.sys.web.controller;

import net.myspring.basic.modules.sys.domain.District;
import net.myspring.basic.modules.sys.dto.DistrictDto;
import net.myspring.basic.modules.sys.service.DistrictService;
import net.myspring.util.json.ObjectMapperUtils;
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

}
