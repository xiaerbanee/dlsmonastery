package net.myspring.general.modules.sys.service;

import net.myspring.general.common.utils.CacheUtils;
import net.myspring.general.modules.sys.domain.District;
import net.myspring.general.modules.sys.dto.DistrictDto;
import net.myspring.general.modules.sys.mapper.DistrictMapper;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService {

    @Autowired
    private DistrictMapper districtMapper;

    public List<DistrictDto> findByNameLike(String name){
        List<District> citys = districtMapper.findByNameLike(name);
        List<DistrictDto> districtDtos= BeanUtil.map(citys,DistrictDto.class);
        return districtDtos;
    }
}
