package net.myspring.general.modules.sys.service;

import net.myspring.basic.modules.sys.domain.District;
import net.myspring.basic.modules.sys.dto.DistrictDto;
import net.myspring.general.common.utils.CacheUtils;
import net.myspring.general.modules.sys.mapper.DistrictMapper;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService {

    @Autowired
    private DistrictMapper districtMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public District findOne(String id){
        District district=districtMapper.findOne(id);
        return district;
    }

    public List<DistrictDto> findByNameLike(String name){
        List<District> citys = districtMapper.findByLikeName(name);
        List<DistrictDto> districtDtos= BeanUtil.map(citys,DistrictDto.class);
        cacheUtils.initCacheInput(districtDtos);
        return districtDtos;
    }

    public List<District> findByProvince(){
        return districtMapper.findByProvince();
    }

}
