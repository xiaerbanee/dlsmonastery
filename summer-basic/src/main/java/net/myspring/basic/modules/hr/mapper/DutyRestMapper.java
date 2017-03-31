package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.DutyRest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DutyRestMapper extends MyMapper<DutyRest,String> {

}
