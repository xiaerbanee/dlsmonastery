package net.myspring.general.modules.sys.service;

import net.myspring.general.modules.sys.domain.District;
import net.myspring.general.modules.sys.dto.DistrictDto;
import net.myspring.general.modules.sys.mapper.DistrictMapper;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DistrictService {

    @Autowired
    private DistrictMapper districtMapper;

    @Transactional(readOnly = true)
    public List<DistrictDto> findByNameLike(String name){
        List<District> citys = districtMapper.findByNameLike(name);
        List<DistrictDto> districtDtos= BeanUtil.map(citys,DistrictDto.class);
        return districtDtos;
    }

    @Transactional(readOnly = true)
    public List<DistrictDto> searchFullText(String name){
        List<District> districts = districtMapper.searchFullText(name);

        List<DistrictDto> districtDtos= BeanUtil.map(districts,DistrictDto.class);
        if(districtDtos!=null){
            for(DistrictDto dd : districtDtos){
                dd.refreshFullName();
            }
        }
        return districtDtos;
    }

}
