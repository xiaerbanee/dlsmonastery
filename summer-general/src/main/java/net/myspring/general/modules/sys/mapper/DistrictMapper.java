package net.myspring.general.modules.sys.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.sys.domain.District;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DistrictMapper extends MyMapper<District,String> {

    List<District> findByParentId(String parentId);

    District findByName(String name);

    List<District> findByLikeName(String name);

    District findByProvinceAndCityAndCounty(@Param("province") String province, @Param("city") String city, @Param("county") String county);

    List<District> findByProvince();
}
