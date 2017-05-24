package net.myspring.general.modules.sys.service;

import net.myspring.general.modules.sys.domain.District;
import net.myspring.general.modules.sys.dto.DistrictDto;
import net.myspring.general.modules.sys.repository.DistrictRepository;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DistrictService {

    @Autowired
    private DistrictRepository districtRepository;

    @Transactional(readOnly = true)
    public List<DistrictDto> findByNameLike(String name){
        List<District> districts = districtRepository.findByNameLike(name);
        List<DistrictDto> districtDtos= BeanUtil.map(districts, DistrictDto.class);
        return districtDtos;
    }

    @Transactional(readOnly = true)
    public DistrictDto findOne(String id){
        District district = districtRepository.findOne(id);
        DistrictDto districtDto= BeanUtil.map(district,DistrictDto.class);
        return districtDto;
    }
}
