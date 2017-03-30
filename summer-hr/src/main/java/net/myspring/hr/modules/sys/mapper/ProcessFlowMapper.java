package net.myspring.hr.modules.sys.mapper;

import net.myspring.hr.common.mybatis.MyMapper;
import net.myspring.hr.modules.sys.domain.ProcessFlow;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProcessFlowMapper extends MyMapper<ProcessFlow,String> {

}
