package net.myspring.hr.modules.sys.mapper;

import net.myspring.hr.common.mybatis.MyMapper;
import net.myspring.hr.modules.sys.domain.District;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DistrictMapper extends MyMapper<District,String> {

}
