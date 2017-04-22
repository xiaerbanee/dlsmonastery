package net.myspring.general.modules.sys.mapper;

import net.myspring.general.common.mybatis.MyMapper;
import net.myspring.general.modules.sys.domain.District;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DistrictMapper {

    List<District> findByParentId(String parentId);

    District findByName(String name);

    List<District> findByNameLike(String name);

    District findByProvinceAndCityAndCounty(@Param("province") String province, @Param("city") String city, @Param("county") String county);
}
