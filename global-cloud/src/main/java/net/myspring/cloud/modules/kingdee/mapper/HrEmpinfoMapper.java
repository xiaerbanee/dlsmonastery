package net.myspring.cloud.modules.kingdee.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by lihx on 2017/4/6.
 */
@Mapper
public interface HrEmpinfoMapper {

    String findFNumberByName(@Param("name")String Name);
}
