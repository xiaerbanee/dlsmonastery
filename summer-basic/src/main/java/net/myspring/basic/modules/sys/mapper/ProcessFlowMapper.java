package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.sys.domain.ProcessFlow;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProcessFlowMapper extends MyMapper<ProcessFlow,String> {

}
