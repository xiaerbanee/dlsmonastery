package net.myspring.hr.modules.sys.mapper;

import net.myspring.hr.common.mybatis.MyMapper;
import net.myspring.hr.modules.sys.domain.Monitor;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MonitorMapper extends MyMapper<Monitor,String> {

}
